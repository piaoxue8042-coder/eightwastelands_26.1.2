package com.px8042.eightwastelands.cultivation;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class CultivationCombatManager {

    public static final String ENTITY_STAGE = "eightwastelands_entity_cultivation_stage";

    public static final double DAMAGE_STEP = 0.05D;
    public static final double MIN_DAMAGE_MULTIPLIER = 0.20D;
    public static final double MAX_DAMAGE_MULTIPLIER = 3.00D;
    private static final int REALM_STEP_UNITS = 5;
    private static final int SUB_STAGES_PER_REALM = 3;

    private CultivationCombatManager() {
    }

    public static CultivationStage getCombatStage(LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            return CultivationManager.getStage(player);
        }

        return CultivationStage.byIndex(entity.getPersistentData().getIntOr(ENTITY_STAGE, 0));
    }

    public static void setEntityStage(LivingEntity entity, CultivationStage stage) {
        if (entity instanceof ServerPlayer player) {
            CultivationManager.setStageForCommand(player, stage);
            return;
        }

        entity.getPersistentData().putInt(ENTITY_STAGE, stage.getIndex());
    }

    public static void clearEntityStage(LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            CultivationManager.clearForCommand(player);
            return;
        }

        entity.getPersistentData().remove(ENTITY_STAGE);
    }

    public static double getDamageMultiplier(LivingEntity attacker, LivingEntity target) {
        return getDamageMultiplier(getCombatStage(attacker), getCombatStage(target));
    }

    public static double getDamageMultiplier(CultivationStage attackerStage, CultivationStage targetStage) {
        int steps = getSuppressionSteps(attackerStage, targetStage);
        return clamp(1.0D + steps * DAMAGE_STEP, MIN_DAMAGE_MULTIPLIER, MAX_DAMAGE_MULTIPLIER);
    }

    public static int getSuppressionSteps(CultivationStage attackerStage, CultivationStage targetStage) {
        int realmDifference = getRealmIndex(attackerStage) - getRealmIndex(targetStage);
        int subStageDifference = getSubStageIndex(attackerStage) - getSubStageIndex(targetStage);

        if (realmDifference == 0) {
            return subStageDifference;
        }

        if (realmDifference > 0) {
            return realmDifference * REALM_STEP_UNITS + Math.max(0, subStageDifference);
        }

        return realmDifference * REALM_STEP_UNITS + Math.min(0, subStageDifference);
    }

    private static int getRealmIndex(CultivationStage stage) {
        return stage.getRealm()
                .map(CultivationRealm::getIndex)
                .orElse(0);
    }

    private static int getSubStageIndex(CultivationStage stage) {
        if (stage.isMortal()) {
            return 0;
        }

        return (stage.getIndex() - 1) % SUB_STAGES_PER_REALM;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
