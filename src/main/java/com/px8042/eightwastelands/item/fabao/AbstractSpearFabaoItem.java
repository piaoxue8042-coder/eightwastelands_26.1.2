package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractSpearFabaoItem extends AbstractWeaponFabaoItem {

    protected AbstractSpearFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, FabaoType.SPEAR, baseDamage, cooldownTicks, attackRange);
    }

    protected AbstractSpearFabaoItem(
            Properties properties,
            float baseDamage,
            int cooldownTicks,
            double attackRange,
            int attackCount,
            int maxPierceTargets
    ) {
        super(properties, FabaoType.SPEAR, baseDamage, cooldownTicks, attackRange, attackCount, maxPierceTargets);
    }
}
