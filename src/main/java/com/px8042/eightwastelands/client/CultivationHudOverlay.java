package com.px8042.eightwastelands.client;

import com.px8042.eightwastelands.cultivation.CultivationClientCache;
import com.px8042.eightwastelands.cultivation.CultivationSnapshot;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.Locale;

public final class CultivationHudOverlay {

    private static final int BAR_WIDTH = 182;
    private static final int BAR_HEIGHT = 8;
    private static final int BAR_GAP = 2;
    private static final int EXPERIENCE_BAR_TOP_OFFSET = 28;
    private static final int EXPERIENCE_BAR_GAP = 4;
    private static final int BORDER_COLOR = 0xCC10202A;
    private static final int BACKGROUND_COLOR = 0xCC061018;
    private static final int HEALTH_FILL_COLOR = 0xFFD84444;
    private static final int HEALTH_HIGHLIGHT_COLOR = 0xFFFF8A8A;
    private static final int ABSORPTION_FILL_COLOR = 0xFFE4B83B;
    private static final int ABSORPTION_HIGHLIGHT_COLOR = 0xFFFFE58A;
    private static final int ZHENYUAN_FILL_COLOR = 0xFF2FB7D3;
    private static final int ZHENYUAN_HIGHLIGHT_COLOR = 0xFF7CE7F4;
    private static final int TEXT_COLOR = 0xFFEAFDFF;

    private static float observedAbsorptionMaximum;

    private CultivationHudOverlay() {
    }

    public static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!shouldUseCultivationHud(minecraft)) {
            return;
        }

        CultivationSnapshot snapshot = CultivationClientCache.getSnapshot();
        float absorption = minecraft.player.getAbsorptionAmount();
        boolean hasAbsorption = absorption > 0.0F;
        float absorptionMaximum = updateAbsorptionMaximum(absorption);
        int x = graphics.guiWidth() / 2 - BAR_WIDTH / 2;
        int bottomY = graphics.guiHeight()
                - EXPERIENCE_BAR_TOP_OFFSET
                - EXPERIENCE_BAR_GAP
                - BAR_HEIGHT;
        int healthY = bottomY - BAR_HEIGHT - BAR_GAP;
        int absorptionY = healthY - BAR_HEIGHT - BAR_GAP;

        if (hasAbsorption) {
            drawBar(
                    graphics,
                    minecraft.font,
                    x,
                    absorptionY,
                    absorption,
                    absorptionMaximum,
                    Component.translatable(
                            "hud.eightwastelands.absorption",
                            formatCompact(absorption),
                            formatCompact(absorptionMaximum)
                    ),
                    ABSORPTION_FILL_COLOR,
                    ABSORPTION_HIGHLIGHT_COLOR
            );
        }

        drawBar(
                graphics,
                minecraft.font,
                x,
                healthY,
                minecraft.player.getHealth(),
                minecraft.player.getMaxHealth(),
                Component.translatable(
                        "hud.eightwastelands.health",
                        formatCompact(minecraft.player.getHealth()),
                        formatCompact(minecraft.player.getMaxHealth())
                ),
                HEALTH_FILL_COLOR,
                HEALTH_HIGHLIGHT_COLOR
        );
        drawBar(
                graphics,
                minecraft.font,
                x,
                bottomY,
                snapshot.zhenyuan(),
                snapshot.maxZhenyuan(),
                Component.translatable(
                        "hud.eightwastelands.zhenyuan",
                        formatCompact(snapshot.zhenyuan()),
                        formatCompact(snapshot.maxZhenyuan())
                ),
                ZHENYUAN_FILL_COLOR,
                ZHENYUAN_HIGHLIGHT_COLOR
        );

    }

    public static boolean shouldUseCultivationHud() {
        return shouldUseCultivationHud(Minecraft.getInstance());
    }

    private static boolean shouldUseCultivationHud(Minecraft minecraft) {
        if (minecraft.options.hideGui || minecraft.player == null || minecraft.player.isSpectator()) {
            return false;
        }

        CultivationSnapshot snapshot = CultivationClientCache.getSnapshot();
        return !snapshot.stage().isMortal() && snapshot.maxZhenyuan() > 0.0D;
    }

    private static float updateAbsorptionMaximum(float absorption) {
        if (absorption <= 0.0F) {
            observedAbsorptionMaximum = 0.0F;
            return 0.0F;
        }

        observedAbsorptionMaximum = Math.max(observedAbsorptionMaximum, absorption);
        return observedAbsorptionMaximum;
    }

    private static void drawBar(
            GuiGraphicsExtractor graphics,
            Font font,
            int x,
            int y,
            double current,
            double max,
            Component text,
            int fillColor,
            int highlightColor
    ) {
        double ratio = max <= 0.0D ? 0.0D : Math.max(0.0D, Math.min(1.0D, current / max));
        int fillWidth = (int) Math.round((BAR_WIDTH - 2) * ratio);

        graphics.fill(x - 1, y - 1, x + BAR_WIDTH + 1, y + BAR_HEIGHT + 1, BORDER_COLOR);
        graphics.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, BACKGROUND_COLOR);
        if (fillWidth > 0) {
            graphics.fill(x + 1, y + 1, x + 1 + fillWidth, y + BAR_HEIGHT - 1, fillColor);
            graphics.fill(x + 1, y + 1, x + 1 + fillWidth, y + 2, highlightColor);
        }

        graphics.centeredText(font, text, graphics.guiWidth() / 2, y, TEXT_COLOR);
    }

    private static String formatCompact(double value) {
        if (Math.abs(value - Math.rint(value)) < 0.005D) {
            return Integer.toString((int) Math.rint(value));
        }

        return String.format(Locale.ROOT, "%.2f", value);
    }
}
