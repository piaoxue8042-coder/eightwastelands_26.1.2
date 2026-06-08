package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractSpinningAxeFabaoItem extends AbstractAxeFabaoItem {

    public static final int DEFAULT_COOLDOWN_TICKS = 8 * 20;

    protected AbstractSpinningAxeFabaoItem(Properties properties, float baseDamage, double attackRange) {
        this(properties, baseDamage, DEFAULT_COOLDOWN_TICKS, attackRange);
    }

    protected AbstractSpinningAxeFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, baseDamage, cooldownTicks, attackRange);
    }
}
