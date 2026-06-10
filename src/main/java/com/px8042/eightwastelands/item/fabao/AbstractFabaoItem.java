package com.px8042.eightwastelands.item.fabao;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.Locale;
import java.util.function.Consumer;

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

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            TooltipDisplay tooltipDisplay,
            Consumer<Component> tooltipComponents,
            TooltipFlag tooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipComponents, tooltipFlag);

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.fabao.stats",
                formatDecimal(this.getBaseDamage(stack)),
                this.getAttackCount(stack),
                formatDecimal(this.getCooldownTicks(stack) / 20.0D)
        ).withStyle(ChatFormatting.GRAY));
    }

    private static String formatDecimal(double value) {
        double rounded = Math.rint(value);
        if (Math.abs(value - rounded) < 0.001D) {
            return Integer.toString((int) rounded);
        }
        return String.format(Locale.ROOT, "%.1f", value);
    }
}
