package com.px8042.eightwastelands.item.fabao;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractFabaoItem extends Item implements IFabaoItem {

    private final FabaoType fabaoType;
    private final float baseDamage;
    private final int cooldownTicks;
    private final double attackRange;
    private final int attackCount;
    private final int maxPierceTargets;

    protected AbstractFabaoItem(Properties properties, FabaoType fabaoType, float baseDamage, int cooldownTicks, double attackRange) {
        this(properties, fabaoType, baseDamage, cooldownTicks, attackRange, 1, 1);
    }

    protected AbstractFabaoItem(
            Properties properties,
            FabaoType fabaoType,
            float baseDamage,
            int cooldownTicks,
            double attackRange,
            int attackCount,
            int maxPierceTargets
    ) {
        super(properties);
        this.fabaoType = fabaoType;
        this.baseDamage = baseDamage;
        this.cooldownTicks = cooldownTicks;
        this.attackRange = attackRange;
        this.attackCount = Math.max(1, attackCount);
        this.maxPierceTargets = Math.max(1, maxPierceTargets);
    }

    @Override
    public FabaoType getFabaoType(ItemStack stack) {
        return this.fabaoType;
    }

    @Override
    public float getBaseDamage(ItemStack stack) {
        return this.baseDamage;
    }

    @Override
    public int getCooldownTicks(ItemStack stack) {
        return this.cooldownTicks;
    }

    @Override
    public double getAttackRange(ItemStack stack) {
        return this.attackRange;
    }

    @Override
    public int getAttackCount(ItemStack stack) {
        return this.attackCount;
    }

    @Override
    public int getMaxPierceTargets(ItemStack stack) {
        return this.maxPierceTargets;
    }
}
