package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractControlNeedleFabaoItem extends AbstractNeedleFabaoItem {

    public static final int DEFAULT_COOLDOWN_TICKS = 14 * 20;

    protected AbstractControlNeedleFabaoItem(Properties properties, float baseDamage, double attackRange) {
        this(properties, baseDamage, DEFAULT_COOLDOWN_TICKS, attackRange);
    }

    protected AbstractControlNeedleFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, baseDamage, cooldownTicks, attackRange);
    }
}
