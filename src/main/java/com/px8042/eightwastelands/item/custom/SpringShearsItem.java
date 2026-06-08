package com.px8042.eightwastelands.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;


public class SpringShearsItem extends Item implements ICurioItem {
    public static final String NEXT_FATE_CUT_TIME =
            "eightwastelands_spring_shears_next_fate_cut_time";


    public static final int FATE_CUT_COOLDOWN = 240 * 20;
    public static final int FATE_CUT_DURATION = 10 * 20;

    public static final float FATE_CUT_DOT_MAX_HEALTH_RATIO = 0.02F;

    private static final float MIN_REMAINING_HEALTH = 1.0F;
    public static final float FATE_CUT_IMMEDIATE_MAX_HEALTH_RATIO = 0.8F;

    public static final int FATE_CUT_WEAKNESS_AMPLIFIER = 1; 
    public static final int FATE_CUT_WITHER_AMPLIFIER = 0;   

    public SpringShearsItem(Properties properties) {
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

        reverseLifeExhaustion(player);
    }

    private void reverseLifeExhaustion(Player player) {

        if (NineHeavensPunishmentItem.isLifeExhaustionReversed(player)) {
            return;
        }

        player.getPersistentData().putBoolean(
                NineHeavensPunishmentItem.LIFE_EXHAUSTION_REVERSED,
                true
        );

        player.sendOverlayMessage(
                Component.translatable("message.eightwastelands.life_exhaustion.reversed"));
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

        tooltipComponents.accept(Component.translatable("tooltip.eightwastelands.spring_shears.title")
                .withStyle(style -> style.withColor(0x99FF99).withBold(true)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.spring_shears.description"
        ).withStyle(style -> style.withColor(0x99FF99).withItalic(true)));

        tooltipComponents.accept(Component.empty());

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.spring_shears.effect_1"
        ).withStyle(style -> style.withColor(0xC0C0C0)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.spring_shears.effect_2"
        ).withStyle(style -> style.withColor(0xFF7777)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.spring_shears.curse"
        ).withStyle(ChatFormatting.GRAY));
    }
    public static boolean isFateCutReady(Player player) {

        long currentTime = player.level().getGameTime();
        long nextUseTime = player.getPersistentData().getLongOr(NEXT_FATE_CUT_TIME, 0L);

        return currentTime >= nextUseTime;
    }

    public static void startFateCutCooldown(Player player) {

        player.getPersistentData().putLong(
                NEXT_FATE_CUT_TIME,
                player.level().getGameTime() + FATE_CUT_COOLDOWN
        );
    }

    public static void cutHealthWithoutKilling(LivingEntity target, float amount) {

        if (target.getHealth() <= MIN_REMAINING_HEALTH) {
            return;
        }

        float newHealth = Math.max(
                MIN_REMAINING_HEALTH,
                target.getHealth() - amount
        );

        target.setHealth(newHealth);
    }
}
