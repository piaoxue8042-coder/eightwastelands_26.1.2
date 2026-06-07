package com.px8042.eightwastelands.item.custom;

import com.px8042.eightwastelands.EightWastelands;
import com.px8042.eightwastelands.item.artifact.IHeavenlyArtifactItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import com.px8042.eightwastelands.item.artifact.AbstractHeavenlyArtifactItem;

import java.util.List;
import java.util.Properties;

public class FengxingBootsItem extends AbstractHeavenlyArtifactItem {

    public static final String SPRINT_TICKS =
            "eightwastelands_fengxing_boots_sprint_ticks";

    public static final Identifier SPEED_MODIFIER_ID =
            Identifier.fromNamespaceAndPath(
                    EightWastelands.MODID,
                    "fengxing_boots_speed"
            );

    public static final int STAGE_1_TICKS = 1 * 20;
    public static final int STAGE_2_TICKS = 4 * 20;
    public static final int STAGE_3_TICKS = 8 * 20;

    public static final double STAGE_1_SPEED_BONUS = 0.02D;
    public static final double STAGE_2_SPEED_BONUS = 0.04D;
    public static final double STAGE_3_SPEED_BONUS = 0.06D;

    public static final int DURABILITY_COST_INTERVAL = 2 * 20;
    public static final int DURABILITY_COST = 1;

    public FengxingBootsItem(Properties properties) {
        super(properties);
    }



    @Override
    public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            net.minecraft.world.item.component.TooltipDisplay tooltipDisplay,
            java.util.function.Consumer<Component> tooltipComponents,
            TooltipFlag tooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipComponents, tooltipFlag);

        tooltipComponents.accept(Component.literal("✦ 仙器 · 风行履 ✦")
                .withStyle(style -> style.withColor(0xA7F3FF).withBold(true)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.fengxing_boots.description"
        ).withStyle(style -> style.withColor(0xB8F7FF).withItalic(true)));

        tooltipComponents.accept(Component.empty());

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.fengxing_boots.effect_1"
        ).withStyle(style -> style.withColor(0x9BE7FF)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.fengxing_boots.effect_2"
        ).withStyle(style -> style.withColor(0x8FD6FF)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.fengxing_boots.effect_3"
        ).withStyle(style -> style.withColor(0xAAAAAA)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.fengxing_boots.curse"
        ).withStyle(ChatFormatting.GRAY));
    }
}