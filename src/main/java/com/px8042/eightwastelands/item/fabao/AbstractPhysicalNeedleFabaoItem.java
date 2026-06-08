package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractPhysicalNeedleFabaoItem extends AbstractNeedleFabaoItem {

    public static final int DEFAULT_COOLDOWN_TICKS = 5 * 20;

    protected AbstractPhysicalNeedleFabaoItem(Properties properties, float baseDamage, double attackRange) {
        this(properties, baseDamage, DEFAULT_COOLDOWN_TICKS, attackRange);
    }

    protected AbstractPhysicalNeedleFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, baseDamage, cooldownTicks, attackRange);
    }
}
