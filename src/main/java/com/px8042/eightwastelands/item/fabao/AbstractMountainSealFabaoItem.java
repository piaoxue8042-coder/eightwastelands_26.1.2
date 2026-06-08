package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractMountainSealFabaoItem extends AbstractSealFabaoItem {

    public static final int DEFAULT_COOLDOWN_TICKS = 20 * 20;

    protected AbstractMountainSealFabaoItem(Properties properties, float baseDamage, double attackRange) {
        this(properties, baseDamage, DEFAULT_COOLDOWN_TICKS, attackRange);
    }

    protected AbstractMountainSealFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, baseDamage, cooldownTicks, attackRange);
    }
}
