package com.px8042.eightwastelands.item.cultivation;

import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Initializes gear stacks and applies the bonuses of currently equipped gear.
 */
public final class CultivationGearManager {

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    private CultivationGearManager() {
    }

    public static void initializeGearInstances(ServerPlayer player) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            initializeIfGear(player.getInventory().getItem(slot), player);
        }

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory ->
                curiosInventory.getStacksHandler("fabao").ifPresent(handler -> {
                    var stacks = handler.getStacks();
                    for (int slot = 0; slot < stacks.getSlots(); slot++) {
                        initializeIfGear(stacks.getStackInSlot(slot), player);
                    }
                })
        );
    }

    public static void applyAttributes(ServerPlayer player) {
        clearAttributeModifiers(player);

        Map<GearStat, Double> bonuses = collectBonuses(player);
        for (Map.Entry<GearStat, Double> entry : bonuses.entrySet()) {
            GearStat stat = entry.getKey();
            if (!stat.isAttribute() || entry.getValue() == 0.0D) {
                continue;
            }

            AttributeInstance instance = player.getAttribute(stat.getAttribute());
            if (instance != null) {
                instance.addOrUpdateTransientModifier(new AttributeModifier(
                        modifierId(stat),
                        entry.getValue(),
                        AttributeModifier.Operation.ADD_VALUE
                ));
            }
        }

        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    public static double getMaxZhenyuanBonus(ServerPlayer player) {
        return collectBonuses(player).getOrDefault(GearStat.MAX_ZHENYUAN, 0.0D);
    }

    public static double getZhenyuanRegenBonus(ServerPlayer player) {
        return collectBonuses(player).getOrDefault(GearStat.ZHENYUAN_REGEN, 0.0D);
    }

    public static double getHealthRegenPerSecond(ServerPlayer player) {
        return collectBonuses(player).getOrDefault(GearStat.HEALTH_REGEN, 0.0D);
    }

    public static void applyHealthRegeneration(ServerPlayer player) {
        double regeneration = getHealthRegenPerSecond(player);
        if (regeneration > 0.0D && player.getHealth() < player.getMaxHealth()) {
            player.heal((float) regeneration);
        }
    }

    private static Map<GearStat, Double> collectBonuses(ServerPlayer player) {
        Map<GearStat, Double> bonuses = new EnumMap<>(GearStat.class);
        for (ItemStack stack : getEquippedGear(player)) {
            if (!(stack.getItem() instanceof ICultivationGear gear) || !gear.canBeUsedBy(player)) {
                continue;
            }

            initializeIfGear(stack, player);
            for (GearModifierEntry entry : GearInstanceData.getFixedAffixes(stack)) {
                bonuses.merge(entry.affix().getStat(), entry.amount(), Double::sum);
            }
            for (GearModifierEntry entry : GearInstanceData.getRandomAffixes(stack)) {
                bonuses.merge(entry.affix().getStat(), entry.amount(), Double::sum);
            }
        }
        return bonuses;
    }

    private static List<ItemStack> getEquippedGear(ServerPlayer player) {
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(player.getMainHandItem());
        stacks.add(player.getOffhandItem());
        for (EquipmentSlot armorSlot : ARMOR_SLOTS) {
            stacks.add(player.getItemBySlot(armorSlot));
        }

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory ->
                curiosInventory.getStacksHandler("fabao").ifPresent(handler -> {
                    var curioStacks = handler.getStacks();
                    for (int slot = 0; slot < curioStacks.getSlots(); slot++) {
                        stacks.add(curioStacks.getStackInSlot(slot));
                    }
                })
        );
        return stacks;
    }

    private static void initializeIfGear(ItemStack stack, ServerPlayer player) {
        if (stack.getItem() instanceof ICultivationGear gear) {
            GearInstanceData.ensureGenerated(stack, gear, player.getRandom());
        }
    }

    private static void clearAttributeModifiers(ServerPlayer player) {
        for (GearStat stat : GearStat.values()) {
            if (!stat.isAttribute()) {
                continue;
            }

            AttributeInstance instance = player.getAttribute(stat.getAttribute());
            if (instance != null) {
                instance.removeModifier(modifierId(stat));
            }
        }
    }

    private static Identifier modifierId(GearStat stat) {
        return Identifier.fromNamespaceAndPath(
                EightWastelands.MODID,
                "gear_" + stat.getSerializedName()
        );
    }
}
