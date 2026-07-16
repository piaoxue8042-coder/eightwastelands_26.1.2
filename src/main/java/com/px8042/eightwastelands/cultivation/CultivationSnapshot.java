package com.px8042.eightwastelands.cultivation;

import com.px8042.eightwastelands.alchemy.AlchemyMastery;

public record CultivationSnapshot(
        int stageIndex,
        int qi,
        int tunaiUsedToday,
        int tunaiDailyUses,
        long breakthroughCooldownTicks,
        int breakthroughFailureCount,
        double zhenyuan,
        double maxZhenyuan,
        double zhenyuanRegenPerSecond,
        double health,
        double maxHealth,
        double attackDamage,
        double attackSpeed,
        double movementSpeed,
        int alchemyRealmIndex,
        int alchemyMasteryIndex,
        int alchemyExperience,
        int alchemyRequiredExperience
) {
    public CultivationStage stage() {
        return CultivationStage.byIndex(this.stageIndex);
    }

    public CultivationRealm alchemyRealm() {
        return CultivationRealm.byIndex(this.alchemyRealmIndex)
                .orElse(CultivationRealm.QI_REFINING);
    }

    public AlchemyMastery alchemyMastery() {
        return AlchemyMastery.byIndex(this.alchemyMasteryIndex)
                .orElse(AlchemyMastery.BEGINNER);
    }

    public int requiredQi() {
        return stage().getRequiredQi();
    }

    public int remainingTunaiUses() {
        return Math.max(0, this.tunaiDailyUses - this.tunaiUsedToday);
    }

    public boolean isBreakthroughCoolingDown() {
        return this.breakthroughCooldownTicks > 0;
    }

    public double breakthroughChance(boolean hasPillBonus) {
        return CultivationManager.getBreakthroughChance(stage(), this.breakthroughFailureCount, hasPillBonus);
    }

    public double breakthroughChance(double pillChanceBonus) {
        return CultivationManager.getBreakthroughChance(stage(), this.breakthroughFailureCount, pillChanceBonus);
    }
}
