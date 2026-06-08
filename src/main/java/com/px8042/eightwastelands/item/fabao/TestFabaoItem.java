package com.px8042.eightwastelands.item.fabao;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class TestFabaoItem extends AbstractFabaoItem {

    public TestFabaoItem(Properties properties) {
        super(properties, 6.0F, 2 * 20, 16.0D);
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
                "tooltip.eightwastelands.test_fabao.description"
        ).withStyle(style -> style.withColor(0x9AD6FF).withItalic(true)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.test_fabao.stats",
                this.getBaseDamage(stack),
                this.getCooldownTicks(stack) / 20.0F
        ).withStyle(ChatFormatting.GRAY));
    }
}
