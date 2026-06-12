package com.px8042.eightwastelands.item.custom;

import com.px8042.eightwastelands.item.artifact.AbstractGhostArtifactItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class GhostBloodItem extends AbstractGhostArtifactItem {

    public static final String COOLDOWN_END_TIME =
            "eightwastelands_ghost_blood_cooldown_end_time";

    public static final int COOLDOWN_TIME = 30 * 20;

    public static final int DURABILITY_COST = 400;

    public static final float TRIGGER_HEALTH_RATIO = 0.2F;

    public static final int BUFF_DURATION = 10 * 20;

    public static final int BUFF_AMPLIFIER = 1;

    public GhostBloodItem(Properties properties) {
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

        tooltipComponents.accept(Component.translatable("tooltip.eightwastelands.ghost_blood.title")
                .withStyle(style -> style.withColor(0xB00020).withBold(true)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.ghost_blood.description"
        ).withStyle(style -> style.withColor(0xD85C6A).withItalic(true)));

        tooltipComponents.accept(Component.empty());

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.ghost_blood.effect_1"
        ).withStyle(style -> style.withColor(0xFF8A80)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.ghost_blood.effect_2"
        ).withStyle(style -> style.withColor(0xFF7777)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.ghost_blood.curse"
        ).withStyle(ChatFormatting.GRAY));
    }
}
