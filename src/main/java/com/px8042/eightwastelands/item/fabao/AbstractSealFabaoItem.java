package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractSealFabaoItem extends AbstractWeaponFabaoItem {

    protected AbstractSealFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, FabaoType.SEAL, baseDamage, cooldownTicks, attackRange);
    }

    protected AbstractSealFabaoItem(
            Properties properties,
            float baseDamage,
            int cooldownTicks,
            double attackRange,
            int attackCount,
            int maxPierceTargets
    ) {
        super(properties, FabaoType.SEAL, baseDamage, cooldownTicks, attackRange, attackCount, maxPierceTargets);
    }
}
