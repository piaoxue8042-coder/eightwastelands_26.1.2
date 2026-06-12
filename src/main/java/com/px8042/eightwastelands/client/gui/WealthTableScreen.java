package com.px8042.eightwastelands.client.gui;

import com.px8042.eightwastelands.EightWastelands;
import com.px8042.eightwastelands.menu.WealthTableMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class WealthTableScreen extends AbstractContainerScreen<WealthTableMenu> {

    private static final Identifier CONTAINER_BACKGROUND =
            Identifier.fromNamespaceAndPath(EightWastelands.MODID, "textures/gui/wealth_table.png");
    private static final int TEXTURE_WIDTH = 176;
    private static final int TEXTURE_HEIGHT = 166;
    private static final int VARIANT_PREVIOUS_BUTTON_X = 56;
    private static final int VARIANT_NEXT_BUTTON_X = 76;
    private static final int VARIANT_BUTTON_Y = 61;
    private static final int VARIANT_BUTTON_SIZE = 16;
    private static final int VARIANT_LABEL_CENTER_X = 74;
    private static final int VARIANT_LABEL_Y = 51;

    private Button previousVariantButton;
    private Button nextVariantButton;

    public WealthTableScreen(WealthTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    @Override
    protected void init() {
        super.init();

        this.previousVariantButton = this.addRenderableWidget(Button.builder(
                        Component.literal("<"),
                        button -> sendVariantButton(WealthTableMenu.BUTTON_PREVIOUS_VARIANT)
                )
                .bounds(
                        this.leftPos + VARIANT_PREVIOUS_BUTTON_X,
                        this.topPos + VARIANT_BUTTON_Y,
                        VARIANT_BUTTON_SIZE,
                        VARIANT_BUTTON_SIZE
                )
                .tooltip(Tooltip.create(Component.translatable(
                        "container.eightwastelands.wealth_table.previous_variant"
                )))
                .build());

        this.nextVariantButton = this.addRenderableWidget(Button.builder(
                        Component.literal(">"),
                        button -> sendVariantButton(WealthTableMenu.BUTTON_NEXT_VARIANT)
                )
                .bounds(
                        this.leftPos + VARIANT_NEXT_BUTTON_X,
                        this.topPos + VARIANT_BUTTON_Y,
                        VARIANT_BUTTON_SIZE,
                        VARIANT_BUTTON_SIZE
                )
                .tooltip(Tooltip.create(Component.translatable(
                        "container.eightwastelands.wealth_table.next_variant"
                )))
                .build());

        updateVariantButtons();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int left = this.leftPos;
        int top = this.topPos;

        updateVariantButtons();

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                CONTAINER_BACKGROUND,
                left,
                top,
                0.0F,
                0.0F,
                this.imageWidth,
                this.imageHeight,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);

        if (!this.menu.hasMultipleVariants()) {
            return;
        }

        String variantLabel = (this.menu.getSelectedVariantIndex() + 1) + "/" + this.menu.getVariantCount();
        graphics.centeredText(
                this.font,
                Component.literal(variantLabel),
                VARIANT_LABEL_CENTER_X,
                VARIANT_LABEL_Y,
                0x404040
        );
    }

    private void updateVariantButtons() {
        boolean visible = this.menu.hasMultipleVariants();

        if (this.previousVariantButton != null) {
            this.previousVariantButton.visible = visible;
            this.previousVariantButton.active = visible;
        }

        if (this.nextVariantButton != null) {
            this.nextVariantButton.visible = visible;
            this.nextVariantButton.active = visible;
        }
    }

    private void sendVariantButton(int buttonId) {
        if (this.minecraft == null || this.minecraft.gameMode == null) {
            return;
        }

        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, buttonId);
    }
}
