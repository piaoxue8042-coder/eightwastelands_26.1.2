package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractSwordArrayFabaoItem extends AbstractSwordFabaoItem {

    public static final int DEFAULT_COOLDOWN_TICKS = 24 * 20;

    protected AbstractSwordArrayFabaoItem(Properties properties, float baseDamage, double attackRange, int attackCount) {
        this(properties, baseDamage, DEFAULT_COOLDOWN_TICKS, attackRange, attackCount);
    }

    protected AbstractSwordArrayFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange, int attackCount) {
        super(properties, baseDamage, cooldownTicks, attackRange, attackCount, 1);
    }
}
