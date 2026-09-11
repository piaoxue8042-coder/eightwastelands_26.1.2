package com.px8042.eightwastelands.client.gui;

import com.px8042.eightwastelands.menu.RefiningFurnaceMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class RefiningFurnaceScreen extends AbstractContainerScreen<RefiningFurnaceMenu> {

    private static final Identifier CONTAINER_BACKGROUND =
            Identifier.withDefaultNamespace("textures/gui/container/furnace.png");
    private static final Identifier LIT_PROGRESS_SPRITE =
            Identifier.withDefaultNamespace("container/furnace/lit_progress");
    private static final Identifier BURN_PROGRESS_SPRITE =
            Identifier.withDefaultNamespace("container/furnace/burn_progress");

    public RefiningFurnaceScreen(RefiningFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 166);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                CONTAINER_BACKGROUND,
                this.leftPos,
                this.topPos,
                0.0F,
                0.0F,
                this.imageWidth,
                this.imageHeight,
                256,
                256
        );

        if (this.menu.isLit()) {
            int litHeight = Mth.ceil(this.menu.getLitProgress() * 13.0F) + 1;
            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    LIT_PROGRESS_SPRITE,
                    14,
                    14,
                    0,
                    14 - litHeight,
                    this.leftPos + 56,
                    this.topPos + 36 + 14 - litHeight,
                    14,
                    litHeight
            );
        }

        int burnWidth = Mth.ceil(this.menu.getBurnProgress() * 24.0F);
        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                BURN_PROGRESS_SPRITE,
                24,
                16,
                0,
                0,
                this.leftPos + 79,
                this.topPos + 34,
                burnWidth,
                16
        );
    }
}
