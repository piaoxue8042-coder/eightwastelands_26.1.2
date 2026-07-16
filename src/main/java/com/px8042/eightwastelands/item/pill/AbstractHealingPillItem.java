package com.px8042.eightwastelands.item.pill;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractHealingPillItem extends AbstractPillItem {

    private final float healAmount;

    protected AbstractHealingPillItem(Properties properties, float healAmount) {
        super(properties);
        this.healAmount = healAmount;
    }

    @Override
    protected boolean canUse(Player player) {
        return player.getHealth() < player.getMaxHealth();
    }

    @Override
    protected void applyPillEffect(ServerPlayer player) {
        player.heal(this.healAmount);
        player.sendOverlayMessage(Component.translatable(
                "message.eightwastelands.pill.healed",
                format(this.healAmount)
        ));
    }

    @Override
    protected Component getCannotUseMessage() {
        return Component.translatable("message.eightwastelands.pill.full_health");
    }

    @Override
    protected void appendPillTooltip(java.util.function.Consumer<Component> tooltipComponents) {
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.pill.healing",
                format(this.healAmount)
        ).withStyle(ChatFormatting.RED));
    }

    private static String format(float value) {
        if (value == (int) value) {
            return Integer.toString((int) value);
        }

        return String.format("%.1f", value);
    }
}
