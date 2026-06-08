package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractNegativeNeedleFabaoItem extends AbstractNeedleFabaoItem {

    public static final int DEFAULT_COOLDOWN_TICKS = 8 * 20;

    protected AbstractNegativeNeedleFabaoItem(Properties properties, float baseDamage, double attackRange) {
        this(properties, baseDamage, DEFAULT_COOLDOWN_TICKS, attackRange);
    }

    protected AbstractNegativeNeedleFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, baseDamage, cooldownTicks, attackRange);
    }
}
