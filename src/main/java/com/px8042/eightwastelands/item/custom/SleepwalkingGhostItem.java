package com.px8042.eightwastelands.item.custom;

import com.px8042.eightwastelands.item.artifact.AbstractGhostArtifactItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class SleepwalkingGhostItem extends AbstractGhostArtifactItem {

    public static final String COOLDOWN_END_TIME =
            "eightwastelands_sleepwalking_ghost_cooldown_end_time";

    public static final int COOLDOWN_TIME = 5 * 20;

    public static final int DURABILITY_COST = 20;

    public SleepwalkingGhostItem(Properties properties) {
        super(properties);
    }

    public static boolean isReady(Player player) {
        long cooldownEndTime = player.getPersistentData().getLongOr(COOLDOWN_END_TIME, 0L);
        return player.level().getGameTime() >= cooldownEndTime;
    }

    public static void startCooldown(Player player) {
        player.getPersistentData().putLong(
                COOLDOWN_END_TIME,
                player.level().getGameTime() + COOLDOWN_TIME
        );
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

        tooltipComponents.accept(Component.translatable("tooltip.eightwastelands.sleepwalking_ghost.title")
                .withStyle(style -> style.withColor(0xB388FF).withBold(true)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.sleepwalking_ghost.description"
        ).withStyle(style -> style.withColor(0xC9A6FF).withItalic(true)));

        tooltipComponents.accept(Component.empty());

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.sleepwalking_ghost.effect_1"
        ).withStyle(style -> style.withColor(0xE9D5FF)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.sleepwalking_ghost.effect_2"
        ).withStyle(style -> style.withColor(0xFF7777)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.sleepwalking_ghost.curse"
        ).withStyle(ChatFormatting.GRAY));
    }
}
