package com.px8042.eightwastelands.item.fabao;

public abstract class AbstractBattleAxeFabaoItem extends AbstractAxeFabaoItem {

    public static final int DEFAULT_COOLDOWN_TICKS = 18 * 20;

    protected AbstractBattleAxeFabaoItem(Properties properties, float baseDamage, double attackRange) {
        this(properties, baseDamage, DEFAULT_COOLDOWN_TICKS, attackRange);
    }

    protected AbstractBattleAxeFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, baseDamage, cooldownTicks, attackRange);
    }
}
