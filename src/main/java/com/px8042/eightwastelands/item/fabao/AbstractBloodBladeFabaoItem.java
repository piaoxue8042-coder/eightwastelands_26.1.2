package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractBloodBladeFabaoItem extends AbstractBladeFabaoItem {

    public static final int DEFAULT_COOLDOWN_TICKS = 12 * 20;

    protected AbstractBloodBladeFabaoItem(Properties properties, float baseDamage, double attackRange) {
        this(properties, baseDamage, DEFAULT_COOLDOWN_TICKS, attackRange);
    }

    protected AbstractBloodBladeFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, baseDamage, cooldownTicks, attackRange);
    }
}
