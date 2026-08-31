package com.px8042.eightwastelands.cultivation;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class CultivationMonsterManager {

    private static final String ASSIGNED = "eightwastelands_cultivation_monster_assigned";
    private static final double PLAYER_SEARCH_RANGE = 64.0D;

    private CultivationMonsterManager() {
    }

    public static void assignOnSpawn(Monster monster) {
        if (monster.level().isClientSide()) {
            return;
        }

        if (monster.getPersistentData().getBooleanOr(ASSIGNED, false)) {
            return;
        }

        CultivationStage stage = rollStageFor(monster);
        monster.getPersistentData().putBoolean(ASSIGNED, true);
        CultivationCombatManager.setEntityStage(monster, stage);
        applyStageAttributes(monster, stage);
        applyStageName(monster, stage);
    }

    public static void dropMaterialsFor(LivingEntity entity) {
        if (!(entity instanceof Monster monster) || monster.level().isClientSide()) {
            return;
        }

        CultivationStage stage = CultivationCombatManager.getCombatStage(monster);
        for (ItemStack drop : CultivationMonsterLootTable.rollDrops(stage, monster.getRandom())) {
            dropItem(monster, drop);
        }
    }

    private static void dropItem(Monster monster, ItemStack drop) {
        if (drop.isEmpty()) {
            return;
        }

        ItemEntity itemEntity = new ItemEntity(
                monster.level(),
                monster.getX(),
                monster.getY() + 0.5D,
                monster.getZ(),
                drop
        );
        monster.level().addFreshEntity(itemEntity);
    }

    private static CultivationStage rollStageFor(Monster monster) {
        ServerPlayer nearestPlayer = getNearestPlayer(monster);
        CultivationStage playerStage = nearestPlayer == null
                ? CultivationStage.MORTAL
                : CultivationManager.getStage(nearestPlayer);

        return CultivationMonsterConfigTable.rollMonsterStage(playerStage, monster.getRandom());
    }

    private static ServerPlayer getNearestPlayer(Monster monster) {
        Level level = monster.level();
        if (!(level instanceof ServerLevel serverLevel)) {
            return null;
        }

        Player nearestPlayer = serverLevel.getNearestPlayer(monster, PLAYER_SEARCH_RANGE);
        return nearestPlayer instanceof ServerPlayer serverPlayer ? serverPlayer : null;
    }

    private static void applyStageAttributes(Monster monster, CultivationStage stage) {
        CultivationMonsterConfigTable.MonsterAttributes attributes =
                CultivationMonsterConfigTable.getAttributes(stage);
        if (attributes.isDefault()) {
            return;
        }

        multiplyBaseValue(monster.getAttribute(Attributes.MAX_HEALTH), attributes.maxHealthMultiplier());
        multiplyBaseValue(monster.getAttribute(Attributes.ATTACK_DAMAGE), attributes.attackDamageMultiplier());
        addBaseValue(monster.getAttribute(Attributes.ARMOR), attributes.armorBonus());

        monster.setHealth(monster.getMaxHealth());
    }

    private static void multiplyBaseValue(AttributeInstance attribute, double multiplier) {
        if (attribute != null) {
            attribute.setBaseValue(attribute.getBaseValue() * multiplier);
        }
    }

    private static void addBaseValue(AttributeInstance attribute, double amount) {
        if (attribute != null && amount != 0.0D) {
            attribute.setBaseValue(attribute.getBaseValue() + amount);
        }
    }

    private static void applyStageName(Monster monster, CultivationStage stage) {
        if (monster.hasCustomName()) {
            return;
        }

        Component realmName = stage.getRealm()
                .map(CultivationRealm::getDisplayName)
                .orElseGet(() -> Component.translatable("cultivation.eightwastelands.realm.mortal"));

        monster.setCustomName(Component.translatable(
                "entity.eightwastelands.cultivation_monster_name",
                realmName,
                monster.getType().getDescription()
        ));
    }
}
