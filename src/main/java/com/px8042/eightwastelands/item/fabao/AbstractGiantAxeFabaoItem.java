package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractGiantAxeFabaoItem extends AbstractAxeFabaoItem {

    public static final int DEFAULT_COOLDOWN_TICKS = 24 * 20;

    protected AbstractGiantAxeFabaoItem(Properties properties, float baseDamage, double attackRange) {
        this(properties, baseDamage, DEFAULT_COOLDOWN_TICKS, attackRange);
    }

    protected AbstractGiantAxeFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, baseDamage, cooldownTicks, attackRange);
    }
}
