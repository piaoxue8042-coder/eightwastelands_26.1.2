package com.px8042.eightwastelands.item.custom;

import com.px8042.eightwastelands.item.artifact.IHeavenlyArtifactItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import com.px8042.eightwastelands.item.artifact.AbstractHeavenlyArtifactItem;

import java.util.List;

public class BihuoPearlItem extends AbstractHeavenlyArtifactItem {

    public static final int FIRE_BLOCK_DURABILITY_COST = 1;

    public static final float FREEZE_DAMAGE_MULTIPLIER = 1.5F;

    public BihuoPearlItem(Properties properties) {
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

        tooltipComponents.accept(Component.translatable("tooltip.eightwastelands.bihuo_pearl.title")
                .withStyle(style -> style.withColor(0xFFAA33).withBold(true)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.bihuo_pearl.description"
        ).withStyle(style -> style.withColor(0xFFD27F).withItalic(true)));

        tooltipComponents.accept(Component.empty());

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.bihuo_pearl.effect_1"
        ).withStyle(style -> style.withColor(0xFF8844)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.bihuo_pearl.effect_2"
        ).withStyle(style -> style.withColor(0x88CCFF)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.bihuo_pearl.effect_3"
        ).withStyle(style -> style.withColor(0xAAAAAA)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.bihuo_pearl.curse"
        ).withStyle(ChatFormatting.GRAY));
    }
}
