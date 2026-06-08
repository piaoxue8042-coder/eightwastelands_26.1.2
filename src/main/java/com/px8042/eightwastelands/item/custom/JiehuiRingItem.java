package com.px8042.eightwastelands.item.custom;

import com.px8042.eightwastelands.item.artifact.IHeavenlyArtifactItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import com.px8042.eightwastelands.item.artifact.AbstractHeavenlyArtifactItem;

import java.util.List;

public class JiehuiRingItem extends AbstractHeavenlyArtifactItem {

    public static final String COOLDOWN_END_TIME =
            "eightwastelands_jiehui_ring_cooldown_end_time";

    public static final int COOLDOWN_TIME = 2 * 60 * 20;

    public static final int DURABILITY_COST = 100;

    public static final int RESISTANCE_DURATION = 8 * 20;

    public static final int FIRE_RESISTANCE_DURATION = 8 * 20;

    public static final int SLOWNESS_DURATION = 5 * 20;

    public JiehuiRingItem(Properties properties) {
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

        tooltipComponents.accept(Component.translatable("tooltip.eightwastelands.jiehui_ring.title")
                .withStyle(style -> style.withColor(0xFFD27F).withBold(true)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.jiehui_ring.description"
        ).withStyle(style -> style.withColor(0xE0C097).withItalic(true)));

        tooltipComponents.accept(Component.empty());

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.jiehui_ring.effect_1"
        ).withStyle(style -> style.withColor(0xFFF2B0)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.jiehui_ring.effect_2"
        ).withStyle(style -> style.withColor(0xFFAA66)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.jiehui_ring.effect_3"
        ).withStyle(style -> style.withColor(0xAAAAAA)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.jiehui_ring.curse"
        ).withStyle(ChatFormatting.GRAY));
    }
}
