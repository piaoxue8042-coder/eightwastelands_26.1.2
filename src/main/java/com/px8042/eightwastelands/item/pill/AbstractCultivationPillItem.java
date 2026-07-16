package com.px8042.eightwastelands.item.pill;

import com.px8042.eightwastelands.cultivation.CultivationManager;
import com.px8042.eightwastelands.cultivation.CultivationRealm;
import com.px8042.eightwastelands.cultivation.CultivationStage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public abstract class AbstractCultivationPillItem extends AbstractPillItem {

    private final CultivationRealm realm;
    private final PillGrade grade;
    private final int qiGain;

    protected AbstractCultivationPillItem(Properties properties, CultivationRealm realm, int qiGain) {
        this(properties, realm, PillGrade.NONE, qiGain);
    }

    protected AbstractCultivationPillItem(
            Properties properties,
            CultivationRealm realm,
            PillGrade grade,
            int qiGain
    ) {
        super(properties);
        this.realm = realm;
        this.grade = Objects.requireNonNullElse(grade, PillGrade.NONE);
        this.qiGain = qiGain;
    }

    @Override
    protected boolean canUse(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            CultivationStage stage = CultivationManager.getStage(serverPlayer);
            if (stage.isMortal()) {
                return false;
            }

            return this.realm == null || stage.getRealm()
                    .map(currentRealm -> currentRealm.isAtLeast(this.realm))
                    .orElse(false);
        }

        return true;
    }

    @Override
    protected void applyPillEffect(ServerPlayer player) {
        CultivationManager.addQi(player, this.qiGain);
        player.sendOverlayMessage(Component.translatable(
                "message.eightwastelands.pill.cultivation_gain",
                this.qiGain,
                CultivationManager.getQiAmount(player)
        ));
    }

    @Override
    protected Component getCannotUseMessage() {
        return Component.translatable("message.eightwastelands.pill.cultivation_unavailable");
    }

    @Override
    protected void appendPillTooltip(java.util.function.Consumer<Component> tooltipComponents) {
        if (this.realm != null) {
            tooltipComponents.accept(Component.translatable(
                    "tooltip.eightwastelands.pill.realm",
                    this.realm.getDisplayName()
            ).withStyle(ChatFormatting.GRAY));
        }

        if (this.grade != null) {
            tooltipComponents.accept(Component.translatable(
                    "tooltip.eightwastelands.pill.grade",
                    this.grade.getDisplayName()
            ).withStyle(ChatFormatting.GRAY));
        }

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.pill.cultivation",
                this.qiGain
        ).withStyle(ChatFormatting.AQUA));
    }
}
