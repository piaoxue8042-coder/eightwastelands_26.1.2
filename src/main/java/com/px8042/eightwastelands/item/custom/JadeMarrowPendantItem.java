package com.px8042.eightwastelands.item.custom;

import com.px8042.eightwastelands.EightWastelands;
import com.px8042.eightwastelands.item.artifact.AbstractHeavenlyArtifactItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class JadeMarrowPendantItem extends AbstractHeavenlyArtifactItem {

    public static final Identifier MAX_HEALTH_MODIFIER_ID =
            Identifier.fromNamespaceAndPath(
                    EightWastelands.MODID,
                    "jade_marrow_pendant_max_health"
            );

    public static final double MAX_HEALTH_BONUS = 20.0D;

    public static final int DAMAGE_TAKEN_DURABILITY_COST = 1;

    public JadeMarrowPendantItem(Properties properties) {
        super(properties);
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

        tooltipComponents.accept(Component.translatable("tooltip.eightwastelands.jade_marrow_pendant.title")
                .withStyle(style -> style.withColor(0x8FE8A8).withBold(true)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.jade_marrow_pendant.description"
        ).withStyle(style -> style.withColor(0xB7F2C6).withItalic(true)));

        tooltipComponents.accept(Component.empty());

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.jade_marrow_pendant.effect_1"
        ).withStyle(style -> style.withColor(0xA4FFB5)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.jade_marrow_pendant.effect_2"
        ).withStyle(style -> style.withColor(0xAAAAAA)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.jade_marrow_pendant.curse"
        ).withStyle(ChatFormatting.GRAY));
    }
}
