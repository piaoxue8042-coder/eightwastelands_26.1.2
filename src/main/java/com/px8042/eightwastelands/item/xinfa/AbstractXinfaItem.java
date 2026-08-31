package com.px8042.eightwastelands.item.xinfa;

import com.px8042.eightwastelands.xinfa.XinfaDefinition;
import com.px8042.eightwastelands.xinfa.XinfaManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public abstract class AbstractXinfaItem extends Item {

    private final XinfaDefinition definition;

    protected AbstractXinfaItem(Properties properties, XinfaDefinition definition) {
        super(properties);
        this.definition = definition;
    }

    public XinfaDefinition getDefinition() {
        return this.definition;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            XinfaManager.useXinfaItem(serverPlayer, this.definition, player.getItemInHand(hand));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            TooltipDisplay tooltipDisplay,
            Consumer<Component> tooltipComponents,
            TooltipFlag tooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipComponents, tooltipFlag);

        tooltipComponents.accept(Component.translatable("tooltip.eightwastelands.xinfa.book")
                .withStyle(ChatFormatting.GRAY));
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.xinfa.required_realm",
                this.definition.requiredRealm().getDisplayName()
        ).withStyle(ChatFormatting.GRAY));
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.xinfa.right_click"
        ).withStyle(ChatFormatting.DARK_AQUA));
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.xinfa.completion_bonus",
                formatPercent(this.definition.completionMultiplier() - 1.0D)
        ).withStyle(ChatFormatting.GRAY));
    }

    private static String formatPercent(double value) {
        return Integer.toString((int) Math.round(value * 100.0D));
    }
}
