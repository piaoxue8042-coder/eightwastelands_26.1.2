package com.px8042.eightwastelands.item.fabao;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractFabaoItem extends Item implements IFabaoItem {

    private final float baseDamage;
    private final int cooldownTicks;
    private final double attackRange;

    protected AbstractFabaoItem(Properties properties, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties);
        this.baseDamage = baseDamage;
        this.cooldownTicks = cooldownTicks;
        this.attackRange = attackRange;
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
}
