package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractSpearShadowFabaoItem extends AbstractSpearFabaoItem {

    public static final int DEFAULT_COOLDOWN_TICKS = 6 * 20;

    protected AbstractSpearShadowFabaoItem(Properties properties, float baseDamage, double attackRange, int maxPierceTargets) {
        this(properties, baseDamage, DEFAULT_COOLDOWN_TICKS, attackRange, maxPierceTargets);
    }

    protected AbstractSpearShadowFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange, int maxPierceTargets) {
        super(properties, baseDamage, cooldownTicks, attackRange, 1, maxPierceTargets);
    }
}
