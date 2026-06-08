package com.px8042.eightwastelands.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class HeartDemonMaskItem extends Item implements ICurioItem {

    public HeartDemonMaskItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.identifier().equals("calamity");
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {

        if (!(slotContext.entity() instanceof Player player)) {
            return false;
        }

        return player.isCreative() || player.isSpectator();
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

        tooltipComponents.accept(Component.translatable("tooltip.eightwastelands.heart_demon_mask.title")
                .withStyle(style -> style.withColor(0xC71585).withBold(true)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.heart_demon_mask.description"
        ).withStyle(style -> style.withColor(0xC71585).withItalic(true)));

        tooltipComponents.accept(Component.empty());

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.heart_demon_mask.effect_1"
        ).withStyle(style -> style.withColor(0xFF5555)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.heart_demon_mask.effect_2"
        ).withStyle(style -> style.withColor(0xAA77FF)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.heart_demon_mask.curse"
        ).withStyle(ChatFormatting.GRAY));
    }
}
