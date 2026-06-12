package com.px8042.eightwastelands.item.custom;

import com.px8042.eightwastelands.item.artifact.AbstractGhostArtifactItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class GhostBrainItem extends AbstractGhostArtifactItem {

    public static final int DURABILITY_COST = 800;

    public GhostBrainItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            net.minecraft.world.item.component.TooltipDisplay tooltipDisplay,
            java.util.function.Consumer<Component> tooltipComponents,
            TooltipFlag tooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipComponents, tooltipFlag);

        tooltipComponents.accept(Component.translatable("tooltip.eightwastelands.ghost_brain.title")
                .withStyle(style -> style.withColor(0x9B5DE5).withBold(true)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.ghost_brain.description"
        ).withStyle(style -> style.withColor(0xC9A6FF).withItalic(true)));

        tooltipComponents.accept(Component.empty());

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.ghost_brain.effect_1"
        ).withStyle(style -> style.withColor(0xE9D5FF)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.ghost_brain.effect_2"
        ).withStyle(style -> style.withColor(0xFF7777)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.ghost_brain.curse"
        ).withStyle(ChatFormatting.GRAY));
    }
}
