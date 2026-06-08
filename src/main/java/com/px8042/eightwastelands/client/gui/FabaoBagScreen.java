package com.px8042.eightwastelands.client.gui;

import com.px8042.eightwastelands.menu.FabaoBagMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class FabaoBagScreen extends AbstractContainerScreen<FabaoBagMenu> {

    private static final Identifier CONTAINER_BACKGROUND =
            Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");

    public FabaoBagScreen(FabaoBagMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 132);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int left = this.leftPos;
        int top = this.topPos;

        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, left, top, 0.0F, 0.0F, this.imageWidth, 35, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, left, top + 35, 0.0F, 126.0F, this.imageWidth, 96, 256, 256);
    }
}
