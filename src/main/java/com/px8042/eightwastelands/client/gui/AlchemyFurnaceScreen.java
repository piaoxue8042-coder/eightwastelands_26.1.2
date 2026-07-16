package com.px8042.eightwastelands.client.gui;

import com.px8042.eightwastelands.menu.AlchemyFurnaceMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class AlchemyFurnaceScreen extends AbstractContainerScreen<AlchemyFurnaceMenu> {

    private static final Identifier CONTAINER_BACKGROUND =
            Identifier.withDefaultNamespace("textures/gui/container/furnace.png");
    private static final Identifier LIT_PROGRESS_SPRITE =
            Identifier.withDefaultNamespace("container/furnace/lit_progress");
    private static final Identifier BURN_PROGRESS_SPRITE =
            Identifier.withDefaultNamespace("container/furnace/burn_progress");
    private static final int TEXTURE_WIDTH = 176;
    private static final int TEXTURE_HEIGHT = 166;
    private static final int VANILLA_TEXTURE_SIZE = 256;
    private static final int LIT_PROGRESS_WIDTH = 14;
    private static final int LIT_PROGRESS_HEIGHT = 14;
    private static final int BURN_PROGRESS_WIDTH = 24;
    private static final int BURN_PROGRESS_HEIGHT = 16;
    private static final int SLOT_FRAME_U = 55;
    private static final int SLOT_FRAME_V = 16;
    private static final int SLOT_FRAME_SIZE = 18;
    private static final int GUI_BACKGROUND_COLOR = 0xFFC6C6C6;
    private static final int VANILLA_OUTPUT_COVER_X = 110;
    private static final int VANILLA_OUTPUT_COVER_Y = 29;
    private static final int VANILLA_OUTPUT_COVER_WIDTH = 50;
    private static final int VANILLA_OUTPUT_COVER_HEIGHT = 34;
    private static final int OUTPUT_SLOT_START_X = 116;
    private static final int OUTPUT_SLOT_START_Y = 25;
    private static final int SLOT_SPACING = 18;

    public AlchemyFurnaceScreen(AlchemyFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int left = this.leftPos;
        int top = this.topPos;

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                CONTAINER_BACKGROUND,
                left,
                top,
                0.0F,
                0.0F,
                this.imageWidth,
                this.imageHeight,
                VANILLA_TEXTURE_SIZE,
                VANILLA_TEXTURE_SIZE
        );

        coverVanillaOutputSlot(graphics, left, top);

        for (int slot = 0; slot < 4; slot++) {
            int column = slot % 2;
            int row = slot / 2;
            drawSlotFrame(
                    graphics,
                    left + OUTPUT_SLOT_START_X + column * SLOT_SPACING - 1,
                    top + OUTPUT_SLOT_START_Y + row * SLOT_SPACING - 1
            );
        }

        if (this.menu.isLit()) {
            int litProgressHeight = Mth.ceil(this.menu.getLitProgress() * 13.0F) + 1;
            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    LIT_PROGRESS_SPRITE,
                    LIT_PROGRESS_WIDTH,
                    LIT_PROGRESS_HEIGHT,
                    0,
                    LIT_PROGRESS_HEIGHT - litProgressHeight,
                    left + 56,
                    top + 36 + LIT_PROGRESS_HEIGHT - litProgressHeight,
                    LIT_PROGRESS_WIDTH,
                    litProgressHeight
            );
        }

        int burnProgressWidth = Mth.ceil(this.menu.getBurnProgress() * BURN_PROGRESS_WIDTH);
        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                BURN_PROGRESS_SPRITE,
                BURN_PROGRESS_WIDTH,
                BURN_PROGRESS_HEIGHT,
                0,
                0,
                left + 79,
                top + 34,
                burnProgressWidth,
                BURN_PROGRESS_HEIGHT
        );
    }

    private static void coverVanillaOutputSlot(GuiGraphicsExtractor graphics, int left, int top) {
        graphics.fill(
                left + VANILLA_OUTPUT_COVER_X,
                top + VANILLA_OUTPUT_COVER_Y,
                left + VANILLA_OUTPUT_COVER_X + VANILLA_OUTPUT_COVER_WIDTH,
                top + VANILLA_OUTPUT_COVER_Y + VANILLA_OUTPUT_COVER_HEIGHT,
                GUI_BACKGROUND_COLOR
        );
    }

    private static void drawSlotFrame(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                CONTAINER_BACKGROUND,
                x,
                y,
                SLOT_FRAME_U,
                SLOT_FRAME_V,
                SLOT_FRAME_SIZE,
                SLOT_FRAME_SIZE,
                VANILLA_TEXTURE_SIZE,
                VANILLA_TEXTURE_SIZE
        );
    }
}
