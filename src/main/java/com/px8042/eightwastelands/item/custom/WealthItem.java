package com.px8042.eightwastelands.item.custom;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class WealthItem extends Item implements ICurioItem {

    public WealthItem(Properties properties) {
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
    public void curioTick(SlotContext slotContext, ItemStack stack) {

        if (!slotContext.identifier().equals("calamity")) {
            return;
        }

        if (!(slotContext.entity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        if (!NineHeavensPunishmentItem.isLuckDeprivationReversed(player)) {
            player.getPersistentData().putBoolean(
                    NineHeavensPunishmentItem.LUCK_DEPRIVATION_REVERSED,
                    true
            );

            player.sendOverlayMessage(
                    net.minecraft.network.chat.Component.translatable("message.eightwastelands.luck_deprivation.reversed"));
        }
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

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.wealth.description"
        ).withStyle(style -> style.withColor(0xB8860B).withItalic(true)));

        tooltipComponents.accept(Component.empty());

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.wealth.effect"
        ).withStyle(style -> style.withColor(0xFFD700)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.wealth.curse"
        ).withStyle(ChatFormatting.GRAY));
    }
}
