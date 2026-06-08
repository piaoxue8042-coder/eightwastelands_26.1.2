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

public class HeavenlyThunderSealItem extends Item implements ICurioItem {

    public static final String HEAVENLY_THUNDER_COUNT =
            "eightwastelands_heavenly_thunder_seal_count";

    public static final int REQUIRED_THUNDER_COUNT = 9;

    public static final String NEXT_THUNDER_STRIKE_TIME =
            "eightwastelands_heavenly_thunder_seal_next_strike_time";

    public static final int THUNDER_STRIKE_COOLDOWN = 5 * 20;

    public static final double THUNDER_STRIKE_RANGE = 25.0D;

    public static final float THUNDER_STRIKE_DAMAGE = 200.0F;  

    public HeavenlyThunderSealItem(Properties properties) {
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

        tooltipComponents.accept(Component.translatable("tooltip.eightwastelands.heavenly_thunder_seal.title")
                .withStyle(style -> style.withColor(0x33CCFF).withBold(true)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.heavenly_thunder_seal.description"
        ).withStyle(style -> style.withColor(0x33CCFF).withItalic(true)));

        tooltipComponents.accept(Component.empty());

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.heavenly_thunder_seal.effect_1"
        ).withStyle(style -> style.withColor(0x66FFFF)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.heavenly_thunder_seal.effect_2"
        ).withStyle(style -> style.withColor(0xAAAAFF)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.heavenly_thunder_seal.effect_3"
        ).withStyle(style -> style.withColor(0xFFFF99)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.heavenly_thunder_seal.effect_4"
        ).withStyle(style -> style.withColor(0xFFFF55)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.heavenly_thunder_seal.curse"
        ).withStyle(ChatFormatting.GRAY));
    }
}
