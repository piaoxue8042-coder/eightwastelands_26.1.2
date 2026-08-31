package com.px8042.eightwastelands.client.gui;

import com.px8042.eightwastelands.network.XinfaActivatePayload;
import com.px8042.eightwastelands.client.ModKeyMappings;
import com.px8042.eightwastelands.xinfa.XinfaBonus;
import com.px8042.eightwastelands.xinfa.XinfaClientCache;
import com.px8042.eightwastelands.xinfa.XinfaDefinition;
import com.px8042.eightwastelands.xinfa.XinfaDefinitions;
import com.px8042.eightwastelands.xinfa.XinfaNode;
import com.px8042.eightwastelands.xinfa.XinfaSpecialAttribute;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class XinfaScreen extends Screen {

    private static final int PANEL_WIDTH = 276;
    private static final int PANEL_HEIGHT = 196;
    private static final int NODE_WIDTH = 64;
    private static final int NODE_HEIGHT = 20;
    private static final int TEXT_COLOR = 0xFF404040;
    private static final int SUBTLE_TEXT_COLOR = 0xFF606060;
    private static final int ACTIVE_COLOR = 0xFF2E6B42;
    private static final int MAJOR_COLOR = 0xFF6B4D2E;
    private static final int LOCKED_COLOR = 0xFF8A8A8A;

    private final String xinfaId;
    private final XinfaDefinition definition;
    private final Map<String, Button> nodeButtons = new HashMap<>();
    private int leftPos;
    private int topPos;
    private int page;
    private Button previousPageButton;
    private Button nextPageButton;

    public XinfaScreen(String xinfaId) {
        super(XinfaDefinitions.byId(xinfaId)
                .map(XinfaDefinition::getDisplayName)
                .orElseGet(() -> Component.translatable("screen.eightwastelands.xinfa")));
        this.xinfaId = xinfaId;
        this.definition = XinfaDefinitions.byId(xinfaId).orElse(XinfaDefinitions.QI_REFINING_MANUAL);
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - PANEL_WIDTH) / 2;
        this.topPos = (this.height - PANEL_HEIGHT) / 2;
        this.page = getRecommendedPage();
        rebuildXinfaWidgets();
    }

    public void refreshFromCache() {
        this.page = getRecommendedPage();
        rebuildXinfaWidgets();
    }

    public String getXinfaId() {
        return this.xinfaId;
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
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int left = this.leftPos;
        int top = this.topPos;
        graphics.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xFF2F2F2F);
        graphics.fill(left + 4, top + 4, left + PANEL_WIDTH - 4, top + PANEL_HEIGHT - 4, 0xFFF1F1F1);
        graphics.fill(left + 8, top + 8, left + PANEL_WIDTH - 8, top + PANEL_HEIGHT - 8, 0xFFE4E4E4);
        graphics.fill(left + 16, top + 52, left + PANEL_WIDTH - 16, top + 55, 0xFF8A8A8A);
        drawConnections(graphics);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        Set<String> activatedNodeIds = getActivatedNodeIds();
        int activatedCount = this.definition.countActivatedNodes(activatedNodeIds);
        graphics.centeredText(this.font, this.title, this.leftPos + PANEL_WIDTH / 2, this.topPos + 16, TEXT_COLOR);
        graphics.centeredText(
                this.font,
                Component.translatable(
                        "screen.eightwastelands.xinfa.page",
                        this.page + 1,
                        this.definition.pageCount()
                ),
                this.leftPos + PANEL_WIDTH / 2,
                this.topPos + 34,
                SUBTLE_TEXT_COLOR
        );
        graphics.text(
                this.font,
                Component.translatable("screen.eightwastelands.xinfa.wudao_sand", XinfaClientCache.getWudaoSandCount()),
                this.leftPos + 20,
                this.topPos + 60,
                SUBTLE_TEXT_COLOR,
                false
        );

        for (XinfaNode node : this.definition.nodesOnPage(this.page)) {
            int titleColor = getNodeTextColor(node, activatedNodeIds);
            int centerX = this.leftPos + node.x() + NODE_WIDTH / 2;
            graphics.centeredText(
                    this.font,
                    this.definition.getNodeName(node),
                    centerX,
                    this.topPos + node.y() + NODE_HEIGHT + 8,
                    titleColor
            );
            graphics.centeredText(
                    this.font,
                    buildBonusText(node),
                    centerX,
                    this.topPos + node.y() + NODE_HEIGHT + 22,
                    SUBTLE_TEXT_COLOR
            );
        }

        graphics.centeredText(
                this.font,
                buildProgressText(activatedCount),
                this.leftPos + PANEL_WIDTH / 2,
                this.topPos + 142,
                SUBTLE_TEXT_COLOR
        );
    }

    private void rebuildXinfaWidgets() {
        this.clearWidgets();
        this.nodeButtons.clear();

        for (XinfaNode node : this.definition.nodesOnPage(this.page)) {
            Button button = this.addRenderableWidget(Button.builder(
                            Component.empty(),
                            pressed -> ClientPacketDistributor.sendToServer(new XinfaActivatePayload(this.xinfaId, node.id()))
                    )
                    .bounds(this.leftPos + node.x(), this.topPos + node.y(), NODE_WIDTH, NODE_HEIGHT)
                    .build());
            this.nodeButtons.put(node.id(), button);
        }

        this.previousPageButton = this.addRenderableWidget(Button.builder(
                        Component.literal("<"),
                        button -> {
                            this.page = Math.max(0, this.page - 1);
                            rebuildXinfaWidgets();
                        }
                )
                .bounds(this.leftPos + 20, this.topPos + 160, 28, 20)
                .build());

        this.nextPageButton = this.addRenderableWidget(Button.builder(
                        Component.literal(">"),
                        button -> {
                            this.page = Math.min(this.definition.pageCount() - 1, this.page + 1);
                            rebuildXinfaWidgets();
                        }
                )
                .bounds(this.leftPos + 54, this.topPos + 160, 28, 20)
                .build());

        this.addRenderableWidget(Button.builder(
                        Component.translatable("gui.done"),
                        button -> this.onClose()
                )
                .bounds(this.leftPos + PANEL_WIDTH - 68, this.topPos + 160, 48, 20)
                .build());

        updateButtons();
    }

    private void updateButtons() {
        Set<String> activatedNodeIds = getActivatedNodeIds();
        int wudaoSandCount = XinfaClientCache.getWudaoSandCount();
        for (XinfaNode node : this.definition.nodesOnPage(this.page)) {
            Button button = this.nodeButtons.get(node.id());
            if (button == null) {
                continue;
            }

            if (activatedNodeIds.contains(node.id())) {
                button.setMessage(Component.translatable("button.eightwastelands.xinfa.activated"));
                button.active = false;
            } else if (node.canActivate(activatedNodeIds)) {
                button.setMessage(Component.translatable("button.eightwastelands.xinfa.activate"));
                button.active = wudaoSandCount >= node.wudaoSandCost();
            } else {
                button.setMessage(Component.translatable("button.eightwastelands.xinfa.locked"));
                button.active = false;
            }
        }

        if (this.previousPageButton != null) {
            this.previousPageButton.active = this.page > 0;
        }

        if (this.nextPageButton != null) {
            this.nextPageButton.active = this.page + 1 < this.definition.pageCount();
        }
    }

    private void drawConnections(GuiGraphicsExtractor graphics) {
        Set<String> activatedNodeIds = getActivatedNodeIds();
        for (XinfaNode node : this.definition.nodesOnPage(this.page)) {
            for (String prerequisiteId : node.prerequisites()) {
                this.definition.nodeById(prerequisiteId).ifPresent(previous -> {
                    if (previous.pageIndex() == node.pageIndex()) {
                        drawConnection(graphics, previous, node, activatedNodeIds.contains(previous.id()));
                    }
                });
            }
        }
    }

    private void drawConnection(GuiGraphicsExtractor graphics, XinfaNode from, XinfaNode to, boolean active) {
        int color = active ? 0xFF6E9A72 : 0xFF9A9A9A;
        int startX = this.leftPos + from.x() + NODE_WIDTH;
        int startY = this.topPos + from.y() + NODE_HEIGHT / 2;
        int endX = this.leftPos + to.x();
        int endY = this.topPos + to.y() + NODE_HEIGHT / 2;
        int midX = startX + (endX - startX) / 2;

        drawHorizontalLine(graphics, startX, midX, startY, color);
        drawVerticalLine(graphics, midX, startY, endY, color);
        drawHorizontalLine(graphics, midX, endX, endY, color);
    }

    private void drawHorizontalLine(GuiGraphicsExtractor graphics, int x1, int x2, int y, int color) {
        int left = Math.min(x1, x2);
        int right = Math.max(x1, x2);
        graphics.fill(left, y - 1, right, y + 1, color);
    }

    private void drawVerticalLine(GuiGraphicsExtractor graphics, int x, int y1, int y2, int color) {
        int top = Math.min(y1, y2);
        int bottom = Math.max(y1, y2);
        graphics.fill(x - 1, top, x + 1, bottom, color);
    }

    private int getRecommendedPage() {
        return this.definition.recommendedPage(getActivatedNodeIds());
    }

    private Set<String> getActivatedNodeIds() {
        if (!this.xinfaId.equals(XinfaClientCache.getXinfaId())) {
            return Set.of();
        }

        return XinfaClientCache.getActivatedNodeIds();
    }

    private int getNodeTextColor(XinfaNode node, Set<String> activatedNodeIds) {
        if (activatedNodeIds.contains(node.id())) {
            return ACTIVE_COLOR;
        }

        if (node.canActivate(activatedNodeIds)) {
            return node.major() ? MAJOR_COLOR : TEXT_COLOR;
        }

        return LOCKED_COLOR;
    }

    private Component buildBonusText(XinfaNode node) {
        if (node.bonuses().isEmpty()) {
            return Component.empty();
        }

        MutableComponent result = Component.empty();
        for (int index = 0; index < node.bonuses().size(); index++) {
            if (index > 0) {
                result.append(Component.literal(", "));
            }

            result.append(buildSingleBonusText(node.bonuses().get(index)));
        }

        return result;
    }

    private Component buildSingleBonusText(XinfaBonus bonus) {
        if (bonus.isAttributeBonus()) {
            return Component.translatable(
                    "screen.eightwastelands.xinfa.bonus." + bonus.attribute().name().toLowerCase(Locale.ROOT),
                    isPercentAttribute(bonus) ? formatPercent(bonus.amount()) : formatAmount(bonus.amount())
            );
        }

        XinfaSpecialAttribute specialAttribute = bonus.specialAttribute();
        return Component.translatable(
                "screen.eightwastelands.xinfa.bonus." + specialAttribute.getSerializedName(),
                formatAmount(bonus.amount())
        );
    }

    private Component buildProgressText(int activatedCount) {
        if (this.definition.isComplete(getActivatedNodeIds())) {
            return Component.translatable(
                    "screen.eightwastelands.xinfa.complete",
                    formatAmount((this.definition.completionMultiplier() - 1.0D) * 100.0D)
            ).withStyle(ChatFormatting.DARK_AQUA);
        }

        return Component.translatable(
                "screen.eightwastelands.xinfa.progress",
                activatedCount,
                this.definition.totalNodes()
        );
    }

    private static String formatAmount(double amount) {
        double abs = Math.abs(amount);
        if (abs == Math.rint(abs)) {
            return Integer.toString((int) amount);
        }

        return String.format(Locale.ROOT, "%.2f", amount);
    }

    private static String formatPercent(double amount) {
        return Integer.toString((int) Math.round(amount * 100.0D)) + "%";
    }

    private static boolean isPercentAttribute(XinfaBonus bonus) {
        return switch (bonus.attribute()) {
            case CRIT_CHANCE, CRIT_DAMAGE_ADDITIVE, VULNERABLE_DAMAGE_ADDITIVE,
                 FIRE_DAMAGE_ADDITIVE, COLD_DAMAGE_ADDITIVE, LIGHTNING_DAMAGE_ADDITIVE,
                 MAGIC_DAMAGE_ADDITIVE, WITHER_DAMAGE_ADDITIVE, POISON_DAMAGE_ADDITIVE,
                 MELEE_DAMAGE_ADDITIVE, PROJECTILE_DAMAGE_ADDITIVE, KINETIC_DAMAGE_ADDITIVE,
                 DODGE_CHANCE, HEALING_RECEIVED -> true;
            default -> false;
        };
    }
}
