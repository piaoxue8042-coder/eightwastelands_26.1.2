package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractLongSpearFabaoItem extends AbstractSpearFabaoItem {

    public static final int DEFAULT_COOLDOWN_TICKS = 10 * 20;

    protected AbstractLongSpearFabaoItem(Properties properties, float baseDamage, double attackRange) {
        this(properties, baseDamage, DEFAULT_COOLDOWN_TICKS, attackRange);
    }

    protected AbstractLongSpearFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, baseDamage, cooldownTicks, attackRange);
    }
}
