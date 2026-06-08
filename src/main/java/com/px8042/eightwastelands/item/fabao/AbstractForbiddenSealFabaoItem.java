package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractForbiddenSealFabaoItem extends AbstractSealFabaoItem {

    public static final int DEFAULT_COOLDOWN_TICKS = 22 * 20;

    protected AbstractForbiddenSealFabaoItem(Properties properties, float baseDamage, double attackRange) {
        this(properties, baseDamage, DEFAULT_COOLDOWN_TICKS, attackRange);
    }

    protected AbstractForbiddenSealFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, baseDamage, cooldownTicks, attackRange);
    }
}
