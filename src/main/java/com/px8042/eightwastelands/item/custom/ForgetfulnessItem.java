package com.px8042.eightwastelands.item.custom;

import com.px8042.eightwastelands.item.artifact.IGhostArtifactItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import com.px8042.eightwastelands.item.artifact.AbstractGhostArtifactItem;

import java.util.List;

public class ForgetfulnessItem extends AbstractGhostArtifactItem {

    public static final String FORGOTTEN_HEALTH_RESTORE_TIME =
            "eightwastelands_forgetfulness_restore_time";

    public static final String FORGOTTEN_HEALTH_BEFORE_DAMAGE =
            "eightwastelands_forgetfulness_health_before_damage";

    public static final String BUFF_MEMORY =
            "eightwastelands_forgetfulness_buff_memory";

    public static final int DAMAGE_RESTORE_DELAY = 2 * 20;

    public static final int DAMAGE_MEMORY_DURABILITY_COST = 8;

    public static final int BUFF_RENEW_DURABILITY_COST = 1;

    public static final int BUFF_RENEW_THRESHOLD = 20;

    public ForgetfulnessItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.identifier().equals(IGhostArtifactItem.SLOT_ID);
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

        tooltipComponents.accept(Component.literal("✦ 鬼器 · 遗忘 ✦")
                .withStyle(style -> style.withColor(0xB388FF).withBold(true)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.forgetfulness.description"
        ).withStyle(style -> style.withColor(0xC9A6FF).withItalic(true)));

        tooltipComponents.accept(Component.empty());

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.forgetfulness.effect_1"
        ).withStyle(style -> style.withColor(0xD6C2FF)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.forgetfulness.effect_2"
        ).withStyle(style -> style.withColor(0xA8FFEE)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.forgetfulness.effect_3"
        ).withStyle(style -> style.withColor(0xFF7777)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.forgetfulness.curse"
        ).withStyle(ChatFormatting.GRAY));
    }
}