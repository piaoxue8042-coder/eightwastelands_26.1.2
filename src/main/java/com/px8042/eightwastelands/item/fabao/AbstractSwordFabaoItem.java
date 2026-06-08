package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractSwordFabaoItem extends AbstractWeaponFabaoItem {

    protected AbstractSwordFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, FabaoType.SWORD, baseDamage, cooldownTicks, attackRange);
    }

    protected AbstractSwordFabaoItem(
            Properties properties,
            float baseDamage,
            int cooldownTicks,
            double attackRange,
            int attackCount,
            int maxPierceTargets
    ) {
        super(properties, FabaoType.SWORD, baseDamage, cooldownTicks, attackRange, attackCount, maxPierceTargets);
    }
}
