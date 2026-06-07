package com.px8042.eightwastelands.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.resources.Identifier;

import java.util.List;

public class ShengZhuangItem extends Item implements ICurioItem {
    public static final Identifier KNOCKBACK_RESISTANCE_MODIFIER_ID =
            Identifier.fromNamespaceAndPath(
                    EightWastelands.MODID,
                    "sheng_zhuang_knockback_resistance"
            );

    public static final double KNOCKBACK_RESISTANCE_BONUS = 1.0D;







    public ShengZhuangItem(Properties properties) {
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

        tooltipComponents.accept(Component.literal("✦ 以身镇地 ✦")
                .withStyle(style -> style.withColor(0x8B0000).withBold(true)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.sheng_zhuang.description"
        ).withStyle(style -> style.withColor(0x8B7355).withItalic(true)));

        tooltipComponents.accept(Component.empty());

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.sheng_zhuang.effect_1"
        ).withStyle(style -> style.withColor(0xAA0000)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.sheng_zhuang.effect_2"
        ).withStyle(style -> style.withColor(0x8B7355)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.sheng_zhuang.effect_3"
        ).withStyle(style -> style.withColor(0xC0C0C0)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.sheng_zhuang.curse"
        ).withStyle(ChatFormatting.GRAY));
    }
}