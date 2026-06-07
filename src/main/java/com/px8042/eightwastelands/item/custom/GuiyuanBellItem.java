package com.px8042.eightwastelands.item.custom;

import com.px8042.eightwastelands.effect.ModMobEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.List;

public class GuiyuanBellItem extends Item implements ICurioItem {

    public static final String NEXT_DISPEL_TIME =
            "eightwastelands_guiyuan_bell_next_dispel_time";

    private static final int DISPEL_COOLDOWN = 30 * 20;

    public GuiyuanBellItem(Properties properties) {
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

        reverseSpiritExhaustion(player);
        tickDispelNegativeEffect(player);
    }

    private void reverseSpiritExhaustion(Player player) {

        if (NineHeavensPunishmentItem.isSpiritExhaustionReversed(player)) {
            return;
        }

        player.getPersistentData().putBoolean(
                NineHeavensPunishmentItem.SPIRIT_EXHAUSTION_REVERSED,
                true
        );

        player.sendOverlayMessage(
                Component.literal("绝灵已反转：一铃归元，灵机复苏。"));
    }

    private void tickDispelNegativeEffect(Player player) {

        long currentTime = player.level().getGameTime();
        long nextDispelTime = player.getPersistentData().getLongOr(NEXT_DISPEL_TIME, 0L);

        if (currentTime < nextDispelTime) {
            return;
        }

        Holder<MobEffect> effectToRemove = findDispellableNegativeEffect(player);

        if (effectToRemove == null) {
            return;
        }

        player.removeEffect(effectToRemove);

        player.getPersistentData().putLong(
                NEXT_DISPEL_TIME,
                currentTime + DISPEL_COOLDOWN
        );

        player.sendOverlayMessage(
                Component.literal("归元铃：已驱散一个负面效果。"));
    }

    private Holder<MobEffect> findDispellableNegativeEffect(Player player) {

        for (MobEffectInstance effectInstance : new ArrayList<>(player.getActiveEffects())) {

            Holder<MobEffect> effect = effectInstance.getEffect();

            if (effect.value().getCategory() != MobEffectCategory.HARMFUL) {
                continue;
            }

            if (isTribulationEffect(effect)) {
                continue;
            }

            return effect;
        }

        return null;
    }

    private boolean isTribulationEffect(Holder<MobEffect> effect) {

        MobEffect mobEffect = effect.value();

        return mobEffect == ModMobEffects.HEAVENLY_TRIBULATION.get()
                || mobEffect == ModMobEffects.EARTH_DISASTER.get()
                || mobEffect == ModMobEffects.KARMA.get()
                || mobEffect == ModMobEffects.HEART_DEMON.get()
                || mobEffect == ModMobEffects.SPIRIT_EXHAUSTION.get()
                || mobEffect == ModMobEffects.WITHERED_BLOOD.get()
                || mobEffect == ModMobEffects.LUCK_DEPRIVATION.get()
                || mobEffect == ModMobEffects.WIND_EVIL.get()
                || mobEffect == ModMobEffects.LIFE_EXHAUSTION.get();
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

        tooltipComponents.accept(Component.literal("✦ 一铃归元 ✦")
                .withStyle(style -> style.withColor(0x66FFAA).withBold(true)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.guiyuan_bell.description"
        ).withStyle(style -> style.withColor(0x66FFAA).withItalic(true)));

        tooltipComponents.accept(Component.empty());

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.guiyuan_bell.effect_1"
        ).withStyle(style -> style.withColor(0x3F7F5F)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.guiyuan_bell.effect_2"
        ).withStyle(style -> style.withColor(0x99FFCC)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.guiyuan_bell.curse"
        ).withStyle(ChatFormatting.GRAY));
    }
}