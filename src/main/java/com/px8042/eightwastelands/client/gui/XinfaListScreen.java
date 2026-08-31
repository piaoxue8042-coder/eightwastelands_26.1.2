package com.px8042.eightwastelands.client.gui;

import com.px8042.eightwastelands.client.ModKeyMappings;
import com.px8042.eightwastelands.network.XinfaListSyncPayload;
import com.px8042.eightwastelands.network.XinfaOpenPayload;
import com.px8042.eightwastelands.xinfa.XinfaClientCache;
import com.px8042.eightwastelands.xinfa.XinfaDefinition;
import com.px8042.eightwastelands.xinfa.XinfaDefinitions;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.List;

public class XinfaListScreen extends Screen {

    private static final int PANEL_WIDTH = 236;
    private static final int PANEL_HEIGHT = 170;
    private static final int LIST_TOP_OFFSET = 48;
    private static final int ENTRY_HEIGHT = 24;
    private static final int VISIBLE_ENTRIES = 4;
    private static final int TEXT_COLOR = 0xFF404040;
    private static final int SUBTLE_TEXT_COLOR = 0xFF606060;

    private int leftPos;
    private int topPos;
    private int scrollOffset;
    private Button upButton;
    private Button downButton;

    public XinfaListScreen() {
        super(Component.translatable("screen.eightwastelands.xinfa_list"));
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - PANEL_WIDTH) / 2;
        this.topPos = (this.height - PANEL_HEIGHT) / 2;
        rebuildXinfaListWidgets();
    }

    public void refreshFromCache() {
        rebuildXinfaListWidgets();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (this.minecraft != null && (this.minecraft.options.keyInventory.matches(keyEvent)
                || ModKeyMappings.OPEN_CULTIVATION.matches(keyEvent))) {
            this.onClose();
            return true;
        }

        return super.keyPressed(keyEvent);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int maxScroll = getMaxScroll();
        if (maxScroll <= 0) {
            return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }

        int direction = scrollY > 0.0D ? -1 : 1;
        this.scrollOffset = Math.max(0, Math.min(this.scrollOffset + direction, maxScroll));
        rebuildXinfaListWidgets();
        return true;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int left = this.leftPos;
        int top = this.topPos;
        graphics.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xFF2F2F2F);
        graphics.fill(left + 4, top + 4, left + PANEL_WIDTH - 4, top + PANEL_HEIGHT - 4, 0xFFF1F1F1);
        graphics.fill(left + 8, top + 8, left + PANEL_WIDTH - 8, top + PANEL_HEIGHT - 8, 0xFFE4E4E4);
        graphics.fill(left + 16, top + 36, left + PANEL_WIDTH - 16, top + 39, 0xFF8A8A8A);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        List<XinfaListSyncPayload.Entry> entries = XinfaClientCache.getLearnedXinfaEntries();
        graphics.centeredText(this.font, this.title, this.leftPos + PANEL_WIDTH / 2, this.topPos + 16, TEXT_COLOR);

        if (entries.isEmpty()) {
            graphics.centeredText(
                    this.font,
                    Component.translatable("screen.eightwastelands.xinfa_list.empty"),
                    this.leftPos + PANEL_WIDTH / 2,
                    this.topPos + 76,
                    SUBTLE_TEXT_COLOR
            );
            return;
        }

        for (int index = 0; index < VISIBLE_ENTRIES; index++) {
            int entryIndex = this.scrollOffset + index;
            if (entryIndex >= entries.size()) {
                break;
            }

            XinfaListSyncPayload.Entry entry = entries.get(entryIndex);
            graphics.text(
                    this.font,
                    Component.translatable(
                            "screen.eightwastelands.xinfa_list.progress",
                            entry.activatedNodes(),
                            entry.totalNodes()
                    ),
                    this.leftPos + 170,
                    this.topPos + LIST_TOP_OFFSET + index * ENTRY_HEIGHT + 6,
                    SUBTLE_TEXT_COLOR,
                    false
            );
        }
    }

    private void rebuildXinfaListWidgets() {
        this.clearWidgets();

        List<XinfaListSyncPayload.Entry> entries = XinfaClientCache.getLearnedXinfaEntries();
        this.scrollOffset = Math.max(0, Math.min(this.scrollOffset, getMaxScroll()));

        for (int index = 0; index < VISIBLE_ENTRIES; index++) {
            int entryIndex = this.scrollOffset + index;
            if (entryIndex >= entries.size()) {
                break;
            }

            XinfaListSyncPayload.Entry entry = entries.get(entryIndex);
            this.addRenderableWidget(Button.builder(
                            getXinfaName(entry.xinfaId()),
                            button -> ClientPacketDistributor.sendToServer(new XinfaOpenPayload(entry.xinfaId()))
                    )
                    .bounds(
                            this.leftPos + 20,
                            this.topPos + LIST_TOP_OFFSET + index * ENTRY_HEIGHT,
                            140,
                            20
                    )
                    .build());
        }

        this.upButton = this.addRenderableWidget(Button.builder(Component.literal("^"), button -> {
                    this.scrollOffset = Math.max(0, this.scrollOffset - 1);
                    rebuildXinfaListWidgets();
                })
                .bounds(this.leftPos + PANEL_WIDTH - 44, this.topPos + 116, 24, 20)
                .build());
        this.downButton = this.addRenderableWidget(Button.builder(Component.literal("v"), button -> {
                    this.scrollOffset = Math.min(getMaxScroll(), this.scrollOffset + 1);
                    rebuildXinfaListWidgets();
                })
                .bounds(this.leftPos + PANEL_WIDTH - 44, this.topPos + 140, 24, 20)
                .build());

        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> this.onClose())
                .bounds(this.leftPos + 20, this.topPos + 140, 48, 20)
                .build());

        updateScrollButtons();
    }

    private void updateScrollButtons() {
        int maxScroll = getMaxScroll();
        if (this.upButton != null) {
            this.upButton.visible = maxScroll > 0;
            this.upButton.active = this.scrollOffset > 0;
        }

        if (this.downButton != null) {
            this.downButton.visible = maxScroll > 0;
            this.downButton.active = this.scrollOffset < maxScroll;
        }
    }

    private int getMaxScroll() {
        return Math.max(0, XinfaClientCache.getLearnedXinfaEntries().size() - VISIBLE_ENTRIES);
    }

    private Component getXinfaName(String xinfaId) {
        return XinfaDefinitions.byId(xinfaId)
                .map(XinfaDefinition::getDisplayName)
                .orElseGet(() -> Component.translatable("screen.eightwastelands.xinfa_list.unknown"));
    }
}
