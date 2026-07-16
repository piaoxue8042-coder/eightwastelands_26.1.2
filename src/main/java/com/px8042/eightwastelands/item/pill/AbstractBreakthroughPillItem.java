package com.px8042.eightwastelands.item.pill;

import com.px8042.eightwastelands.cultivation.CultivationManager;
import com.px8042.eightwastelands.cultivation.CultivationRealm;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;
import java.util.Optional;

public abstract class AbstractBreakthroughPillItem extends AbstractPillItem {

    private final CultivationRealm realm;
    private final PillGrade grade;
    private final double chanceBonus;
    private final long cooldownReductionTicks;

    protected AbstractBreakthroughPillItem(Properties properties) {
        this(
                properties,
                null,
                PillGrade.NONE,
                CultivationManager.BREAKTHROUGH_PILL_CHANCE_BONUS,
                CultivationManager.BREAKTHROUGH_PILL_COOLDOWN_REDUCTION_TICKS
        );
    }

    protected AbstractBreakthroughPillItem(
            Properties properties,
            PillGrade grade,
            double chanceBonus,
            long cooldownReductionTicks
    ) {
        this(properties, null, grade, chanceBonus, cooldownReductionTicks);
    }

    protected AbstractBreakthroughPillItem(
            Properties properties,
            CultivationRealm realm,
            PillGrade grade,
            double chanceBonus,
            long cooldownReductionTicks
    ) {
        super(properties);
        this.realm = realm;
        this.grade = Objects.requireNonNullElse(grade, PillGrade.NONE);
        this.chanceBonus = Math.max(0.0D, chanceBonus);
        this.cooldownReductionTicks = Math.max(0L, cooldownReductionTicks);
    }

    public Optional<CultivationRealm> getRealm() {
        return Optional.ofNullable(this.realm);
    }

    public PillGrade getGrade() {
        return this.grade;
    }

    public double getChanceBonus() {
        return this.chanceBonus;
    }

    public long getCooldownReductionTicks() {
        return this.cooldownReductionTicks;
    }

    @Override
    protected boolean canUse(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            return CultivationManager.canUseBreakthroughPill(serverPlayer, this);
        }

        return true;
    }

    @Override
    protected void applyPillEffect(ServerPlayer player) {
        if (CultivationManager.getBreakthroughCooldownTicks(player) > 0L) {
            CultivationManager.reduceBreakthroughCooldownWithPill(player, this.cooldownReductionTicks);
            return;
        }

        CultivationManager.handleBreakthroughWithPill(player, this.chanceBonus);
    }

    @Override
    protected Component getCannotUseMessage() {
        return Component.translatable("message.eightwastelands.pill.breakthrough_unavailable");
    }

    @Override
    protected void appendPillTooltip(java.util.function.Consumer<Component> tooltipComponents) {
        if (this.realm != null) {
            tooltipComponents.accept(Component.translatable(
                    "tooltip.eightwastelands.pill.realm",
                    this.realm.getDisplayName()
            ).withStyle(ChatFormatting.GRAY));
        }

        if (this.grade.isGraded()) {
            tooltipComponents.accept(Component.translatable(
                    "tooltip.eightwastelands.pill.grade",
                    this.grade.getDisplayName()
            ).withStyle(ChatFormatting.GRAY));
        }

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.pill.breakthrough"
        ).withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.pill.breakthrough_chance",
                formatPercent(this.chanceBonus)
        ).withStyle(ChatFormatting.AQUA));
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.pill.breakthrough_cooldown_reduction",
                CultivationManager.formatTicks(this.cooldownReductionTicks)
        ).withStyle(ChatFormatting.AQUA));
    }

    private static String formatPercent(double value) {
        return Integer.toString((int) Math.round(value * 100.0D));
    }
}
