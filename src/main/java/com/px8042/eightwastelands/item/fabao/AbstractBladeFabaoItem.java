package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractBladeFabaoItem extends AbstractWeaponFabaoItem {

    protected AbstractBladeFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, FabaoType.BLADE, baseDamage, cooldownTicks, attackRange);
    }

    protected AbstractBladeFabaoItem(
            Properties properties,
            float baseDamage,
            int cooldownTicks,
            double attackRange,
            int attackCount,
            int maxPierceTargets
    ) {
        super(properties, FabaoType.BLADE, baseDamage, cooldownTicks, attackRange, attackCount, maxPierceTargets);
    }
}
