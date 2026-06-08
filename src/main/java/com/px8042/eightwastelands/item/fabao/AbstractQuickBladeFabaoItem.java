package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractQuickBladeFabaoItem extends AbstractBladeFabaoItem {

    public static final int DEFAULT_COOLDOWN_TICKS = 6 * 20;

    protected AbstractQuickBladeFabaoItem(Properties properties, float baseDamage, double attackRange, int attackCount) {
        this(properties, baseDamage, DEFAULT_COOLDOWN_TICKS, attackRange, attackCount);
    }

    protected AbstractQuickBladeFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange, int attackCount) {
        super(properties, baseDamage, cooldownTicks, attackRange, attackCount, 1);
    }
}
