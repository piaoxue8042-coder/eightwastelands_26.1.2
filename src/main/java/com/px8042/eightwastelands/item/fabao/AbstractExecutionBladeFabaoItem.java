package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractExecutionBladeFabaoItem extends AbstractBladeFabaoItem {

    public static final int DEFAULT_COOLDOWN_TICKS = 15 * 20;

    protected AbstractExecutionBladeFabaoItem(Properties properties, float baseDamage, double attackRange) {
        this(properties, baseDamage, DEFAULT_COOLDOWN_TICKS, attackRange);
    }

    protected AbstractExecutionBladeFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, baseDamage, cooldownTicks, attackRange);
    }
}
