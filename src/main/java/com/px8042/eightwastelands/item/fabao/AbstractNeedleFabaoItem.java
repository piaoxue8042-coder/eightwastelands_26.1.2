package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractNeedleFabaoItem extends AbstractWeaponFabaoItem {

    protected AbstractNeedleFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, FabaoType.NEEDLE, baseDamage, cooldownTicks, attackRange);
    }

    protected AbstractNeedleFabaoItem(
            Properties properties,
            float baseDamage,
            int cooldownTicks,
            double attackRange,
            int attackCount,
            int maxPierceTargets
    ) {
        super(properties, FabaoType.NEEDLE, baseDamage, cooldownTicks, attackRange, attackCount, maxPierceTargets);
    }
}
