package com.px8042.eightwastelands.event;

import com.px8042.eightwastelands.item.cultivation.ICultivationGear;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class CultivationGearEvents {

    private static final int EQUIPMENT_CHECK_INTERVAL = 10;
    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        ItemStack weapon = player.getMainHandItem();
        if (!(weapon.getItem() instanceof ICultivationGear gear) || gear.canBeUsedBy(player)) {
            return;
        }

        event.setCanceled(true);
        notifyRealmRequired(player, gear);
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || player.level().getGameTime() % EQUIPMENT_CHECK_INTERVAL != 0L) {
            return;
        }

        for (EquipmentSlot armorSlot : ARMOR_SLOTS) {
            ItemStack armor = player.getItemBySlot(armorSlot);
            if (!(armor.getItem() instanceof ICultivationGear gear) || gear.canBeUsedBy(player)) {
                continue;
            }

            player.setItemSlot(armorSlot, ItemStack.EMPTY);
            if (!player.getInventory().add(armor)) {
                player.drop(armor, false);
            }
            notifyRealmRequired(player, gear);
        }
    }

    private static void notifyRealmRequired(ServerPlayer player, ICultivationGear gear) {
        player.sendOverlayMessage(Component.translatable(
                "message.eightwastelands.cultivation_gear.realm_required",
                gear.getRequiredRealm().getDisplayName()
        ));
    }
}
