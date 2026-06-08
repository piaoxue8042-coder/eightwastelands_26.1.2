package com.px8042.eightwastelands.item.fabao;

import net.minecraft.world.item.ItemStack;

public abstract class AbstractWeaponFabaoItem extends AbstractFabaoItem {

    protected AbstractWeaponFabaoItem(Properties properties, FabaoType fabaoType, float baseDamage, int cooldownTicks, double attackRange) {
        super(properties, fabaoType, baseDamage, cooldownTicks, attackRange);
    }

    protected AbstractWeaponFabaoItem(
            Properties properties,
            FabaoType fabaoType,
            float baseDamage,
            int cooldownTicks,
            double attackRange,
            int attackCount,
            int maxPierceTargets
    ) {
        super(properties, fabaoType, baseDamage, cooldownTicks, attackRange, attackCount, maxPierceTargets);
    }

    public boolean isWeaponFabao(ItemStack stack) {
        return true;
    }
}
