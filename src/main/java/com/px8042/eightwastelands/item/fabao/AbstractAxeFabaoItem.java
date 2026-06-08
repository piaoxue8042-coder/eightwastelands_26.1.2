package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractAxeFabaoItem extends AbstractWeaponFabaoItem {

    protected AbstractAxeFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, FabaoType.AXE, baseDamage, cooldownTicks, attackRange);
    }

    protected AbstractAxeFabaoItem(
            Properties properties,
            float baseDamage,
            int cooldownTicks,
            double attackRange,
            int attackCount,
            int maxPierceTargets
    ) {
        super(properties, FabaoType.AXE, baseDamage, cooldownTicks, attackRange, attackCount, maxPierceTargets);
    }
}
