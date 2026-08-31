package com.px8042.eightwastelands.client.gui;

import com.px8042.eightwastelands.cultivation.CultivationClientCache;
import com.px8042.eightwastelands.cultivation.CultivationManager;
import com.px8042.eightwastelands.cultivation.CultivationRealm;
import com.px8042.eightwastelands.cultivation.CultivationSnapshot;
import com.px8042.eightwastelands.cultivation.CultivationStage;
import com.px8042.eightwastelands.client.ModKeyMappings;
import com.px8042.eightwastelands.item.pill.AbstractBreakthroughPillItem;
import com.px8042.eightwastelands.network.CultivationActionPayload;
import io.github.naimjeg.damagenexus.registry.ModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class CultivationScreen extends Screen {

    private static final int PANEL_WIDTH = 256;
    private static final int PANEL_HEIGHT = 210;
    private static final int TAB_TOP_OFFSET = 34;
    private static final int CONTENT_TOP_OFFSET = 68;
    private static final int BUTTON_TOP_OFFSET = 176;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_GAP = 8;
    private static final int TAB_WIDTH = 58;
    private static final int PRIMARY_BUTTON_WIDTH = 88;
    private static final int BREAKTHROUGH_BUTTON_WIDTH = 76;
    private static final int DONE_BUTTON_WIDTH = 48;
    private static final int LINE_HEIGHT = 14;
    private static final int CONTENT_VISIBLE_LINES = 7;
    private static final int TEXT_COLOR = 0xFF404040;
    private static final int SUBTLE_TEXT_COLOR = 0xFF606060;

    private int leftPos;
    private int topPos;
    private Page page = Page.TRAINING;
    private Button trainingTabButton;
    private Button attributesTabButton;
    private Button xinfaButton;
    private Button primaryButton;
    private Button breakthroughButton;
    private int syncTicker;
    private int scrollOffset;

    public CultivationScreen() {
        super(Component.translatable("screen.eightwastelands.cultivation"));
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - PANEL_WIDTH) / 2;
        this.topPos = (this.height - PANEL_HEIGHT) / 2;

        int buttonLeft = getButtonRowLeft();
        int buttonTop = this.topPos + BUTTON_TOP_OFFSET;

        this.trainingTabButton = this.addRenderableWidget(Button.builder(
                        Component.translatable("button.eightwastelands.cultivation.tab_training"),
                        button -> switchPage(Page.TRAINING)
                )
                .bounds(this.leftPos + 22, this.topPos + TAB_TOP_OFFSET, TAB_WIDTH, BUTTON_HEIGHT)
                .build());

        this.attributesTabButton = this.addRenderableWidget(Button.builder(
                        Component.translatable("button.eightwastelands.cultivation.tab_attributes"),
                        button -> switchPage(Page.ATTRIBUTES)
                )
                .bounds(
                        this.leftPos + 22 + TAB_WIDTH + BUTTON_GAP,
                        this.topPos + TAB_TOP_OFFSET,
                        TAB_WIDTH,
                        BUTTON_HEIGHT
                )
                .build());

        this.xinfaButton = this.addRenderableWidget(Button.builder(
                        Component.translatable("button.eightwastelands.cultivation.xinfa"),
                        button -> ClientPacketDistributor.sendToServer(new CultivationActionPayload(
                                CultivationActionPayload.ACTION_OPEN_XINFA_LIST
                        ))
                )
                .bounds(
                        this.leftPos + 22 + (TAB_WIDTH + BUTTON_GAP) * 2,
                        this.topPos + TAB_TOP_OFFSET,
                        TAB_WIDTH,
                        BUTTON_HEIGHT
                )
                .build());

        this.primaryButton = this.addRenderableWidget(Button.builder(
                        getPrimaryButtonText(),
                        button -> ClientPacketDistributor.sendToServer(new CultivationActionPayload(
                                CultivationActionPayload.ACTION_MAIN_BUTTON
                        ))
                )
                .bounds(buttonLeft, buttonTop, PRIMARY_BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        this.breakthroughButton = this.addRenderableWidget(Button.builder(
                        getBreakthroughButtonText(),
                        button -> ClientPacketDistributor.sendToServer(new CultivationActionPayload(
                                CultivationActionPayload.ACTION_BREAKTHROUGH
                        ))
                )
                .bounds(
                        buttonLeft + PRIMARY_BUTTON_WIDTH + BUTTON_GAP,
                        buttonTop,
                        BREAKTHROUGH_BUTTON_WIDTH,
                        BUTTON_HEIGHT
                )
                .build());

        this.addRenderableWidget(Button.builder(
                        Component.translatable("gui.done"),
                        button -> this.onClose()
                )
                .bounds(
                        buttonLeft + PRIMARY_BUTTON_WIDTH + BREAKTHROUGH_BUTTON_WIDTH + BUTTON_GAP * 2,
                        buttonTop,
                        DONE_BUTTON_WIDTH,
                        BUTTON_HEIGHT
                )
                .build());

        requestSync();
        updateButtons();
    }

    @Override
    public void tick() {
        CultivationClientCache.tickCooldown();
        this.syncTicker++;
        if (this.syncTicker >= 20) {
            this.syncTicker = 0;
            requestSync();
        }

        updateButtons();
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
        drawPanel(graphics);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        CultivationSnapshot snapshot = CultivationClientCache.getSnapshot();
        CultivationStage stage = snapshot.stage();

        graphics.centeredText(
                this.font,
                this.title,
                this.leftPos + PANEL_WIDTH / 2,
                this.topPos + 12,
                TEXT_COLOR
        );

        int contentTop = this.topPos + CONTENT_TOP_OFFSET;
        if (stage.isMortal()) {
            renderMortalPage(graphics, contentTop, snapshot, stage);
        } else if (this.page == Page.ATTRIBUTES) {
            renderAttributesPage(graphics, contentTop, snapshot, stage);
        } else {
            renderTrainingPage(graphics, contentTop, snapshot, stage);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (isMouseInContentArea(mouseX, mouseY)) {
            int maxScroll = getMaxScroll(getCurrentContentLineCount());
            if (maxScroll > 0) {
                int direction = scrollY > 0.0D ? -1 : 1;
                this.scrollOffset = Math.max(0, Math.min(this.scrollOffset + direction, maxScroll));
                return true;
            }
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private void drawPanel(GuiGraphicsExtractor graphics) {
        int left = this.leftPos;
        int top = this.topPos;

        graphics.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xFFC6C6C6);
        graphics.fill(left + 4, top + 4, left + PANEL_WIDTH - 4, top + PANEL_HEIGHT - 4, 0xFFE6E6E6);
        graphics.outline(left, top, PANEL_WIDTH, PANEL_HEIGHT, 0xFF373737);
        graphics.outline(left + 3, top + 3, PANEL_WIDTH - 6, PANEL_HEIGHT - 6, 0xFFFFFFFF);
        graphics.outline(left + 8, top + 60, PANEL_WIDTH - 16, 1, 0xFF8B8B8B);
        graphics.outline(left + 8, top + BUTTON_TOP_OFFSET - 10, PANEL_WIDTH - 16, 1, 0xFF8B8B8B);
    }

    private void renderMortalPage(
            GuiGraphicsExtractor graphics,
            int contentTop,
            CultivationSnapshot snapshot,
            CultivationStage stage
    ) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable("screen.eightwastelands.cultivation.stage", stage.getDisplayName()));
        lines.add(Component.translatable("screen.eightwastelands.cultivation.mortal_hint"));
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.alchemy_level",
                snapshot.alchemyRealm().getDisplayName(),
                snapshot.alchemyMastery().getDisplayName()
        ));
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.alchemy_experience",
                snapshot.alchemyExperience(),
                snapshot.alchemyRequiredExperience()
        ));
        renderScrollableLines(graphics, contentTop, lines);
    }

    private void renderTrainingPage(
            GuiGraphicsExtractor graphics,
            int contentTop,
            CultivationSnapshot snapshot,
            CultivationStage stage
    ) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable("screen.eightwastelands.cultivation.stage", stage.getDisplayName()));

        if (stage.isMaxStage()) {
            lines.add(Component.translatable(
                    "screen.eightwastelands.cultivation.qi_current",
                    snapshot.qi()
            ));
        } else {
            lines.add(Component.translatable(
                    "screen.eightwastelands.cultivation.qi",
                    snapshot.qi(),
                    stage.getRequiredQi()
            ));
        }

        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.tunai",
                snapshot.tunaiUsedToday(),
                snapshot.tunaiDailyUses()
        ));

        if (snapshot.isBreakthroughCoolingDown()) {
            lines.add(Component.translatable(
                    "screen.eightwastelands.cultivation.breakthrough_cooldown",
                    CultivationManager.formatTicks(snapshot.breakthroughCooldownTicks())
            ));
        } else if (!stage.isMaxStage()) {
            lines.add(Component.translatable(
                    "screen.eightwastelands.cultivation.breakthrough_chance",
                    formatChance(snapshot.breakthroughChance(getBreakthroughPillChanceBonusInInventory()))
            ));
        }

        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.alchemy_level",
                snapshot.alchemyRealm().getDisplayName(),
                snapshot.alchemyMastery().getDisplayName()
        ));
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.alchemy_experience",
                snapshot.alchemyExperience(),
                snapshot.alchemyRequiredExperience()
        ));

        renderScrollableLines(graphics, contentTop, lines);
    }

    private void renderAttributesPage(
            GuiGraphicsExtractor graphics,
            int contentTop,
            CultivationSnapshot snapshot,
            CultivationStage stage
    ) {
        renderScrollableLines(graphics, contentTop, buildAttributeLines(snapshot));
    }

    private List<Component> buildAttributeLines(CultivationSnapshot snapshot) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable("screen.eightwastelands.cultivation.attribute_section_basic"));
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.attribute_health",
                formatCompact(snapshot.health()),
                formatCompact(snapshot.maxHealth())
        ));
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.attribute_health_regen",
                Component.translatable("screen.eightwastelands.cultivation.attribute_health_regen_vanilla")
        ));
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.attribute_zhenyuan",
                formatCompact(snapshot.zhenyuan()),
                formatCompact(snapshot.maxZhenyuan())
        ));
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.attribute_zhenyuan_regen",
                formatFixed(snapshot.zhenyuanRegenPerSecond(), 2)
        ));
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.attribute_attack",
                formatCompact(snapshot.attackDamage())
        ));
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.attribute_attack_speed",
                formatFixed(snapshot.attackSpeed(), 2)
        ));
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.attribute_movement_speed",
                formatFixed(snapshot.movementSpeed(), 2)
        ));

        lines.add(Component.translatable("screen.eightwastelands.cultivation.attribute_section_combat"));
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.attribute_crit_chance",
                formatPercent(getDamageNexusAttribute(ModAttributes.CRIT_CHANCE))
        ));
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.attribute_crit_damage",
                formatSignedPercent(getDamageNexusAttribute(ModAttributes.CRIT_DAMAGE_ADDITIVE))
        ));
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.attribute_vulnerable_damage",
                formatSignedPercent(getDamageNexusAttribute(ModAttributes.VULNERABLE_DAMAGE_ADDITIVE))
        ));
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.attribute_dodge",
                formatPercent(getDamageNexusAttribute(ModAttributes.DODGE_CHANCE))
        ));
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.attribute_thorns",
                formatCompact(getDamageNexusAttribute(ModAttributes.THORNS))
        ));
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.attribute_healing_received",
                formatPercent(getDamageNexusAttribute(ModAttributes.HEALING_RECEIVED))
        ));

        lines.add(Component.translatable("screen.eightwastelands.cultivation.attribute_section_damage_bonus"));
        addDamageBonusLine(lines, "melee", getDamageNexusAttribute(ModAttributes.MELEE_DAMAGE_ADDITIVE));
        addDamageBonusLine(lines, "projectile", getDamageNexusAttribute(ModAttributes.PROJECTILE_DAMAGE_ADDITIVE));
        addDamageBonusLine(lines, "fire", getDamageNexusAttribute(ModAttributes.FIRE_DAMAGE_ADDITIVE));
        addDamageBonusLine(lines, "cold", getDamageNexusAttribute(ModAttributes.COLD_DAMAGE_ADDITIVE));
        addDamageBonusLine(lines, "lightning", getDamageNexusAttribute(ModAttributes.LIGHTNING_DAMAGE_ADDITIVE));
        addDamageBonusLine(lines, "magic", getDamageNexusAttribute(ModAttributes.MAGIC_DAMAGE_ADDITIVE));
        addDamageBonusLine(lines, "poison", getDamageNexusAttribute(ModAttributes.POISON_DAMAGE_ADDITIVE));
        addDamageBonusLine(lines, "wither", getDamageNexusAttribute(ModAttributes.WITHER_DAMAGE_ADDITIVE));
        addDamageBonusLine(lines, "kinetic", getDamageNexusAttribute(ModAttributes.KINETIC_DAMAGE_ADDITIVE));

        lines.add(Component.translatable("screen.eightwastelands.cultivation.attribute_section_resistance"));
        addResistanceLine(lines, "physical", getDamageNexusAttribute(ModAttributes.RESISTANCE_PHYSICAL));
        addResistanceLine(lines, "melee", getDamageNexusAttribute(ModAttributes.RESISTANCE_MELEE));
        addResistanceLine(lines, "projectile", getDamageNexusAttribute(ModAttributes.RESISTANCE_PROJECTILE));
        addResistanceLine(lines, "fire", getDamageNexusAttribute(ModAttributes.RESISTANCE_FIRE));
        addResistanceLine(lines, "cold", getDamageNexusAttribute(ModAttributes.RESISTANCE_COLD));
        addResistanceLine(lines, "lightning", getDamageNexusAttribute(ModAttributes.RESISTANCE_LIGHTNING));
        addResistanceLine(lines, "magic", getDamageNexusAttribute(ModAttributes.RESISTANCE_MAGIC));
        addResistanceLine(lines, "poison", getDamageNexusAttribute(ModAttributes.RESISTANCE_POISON));
        addResistanceLine(lines, "wither", getDamageNexusAttribute(ModAttributes.RESISTANCE_WITHER));
        addResistanceLine(lines, "kinetic", getDamageNexusAttribute(ModAttributes.RESISTANCE_KINETIC));

        return lines;
    }

    private void renderScrollableLines(
            GuiGraphicsExtractor graphics,
            int contentTop,
            List<Component> lines
    ) {
        int maxScroll = getMaxScroll(lines.size());
        this.scrollOffset = Math.max(0, Math.min(this.scrollOffset, maxScroll));

        int textLeft = this.leftPos + 22;
        for (int index = 0; index < CONTENT_VISIBLE_LINES; index++) {
            int lineIndex = this.scrollOffset + index;
            if (lineIndex >= lines.size()) {
                break;
            }

            int color = lineIndex == 0 ? TEXT_COLOR : SUBTLE_TEXT_COLOR;
            graphics.text(this.font, lines.get(lineIndex), textLeft, contentTop + index * LINE_HEIGHT, color, false);
        }

        if (maxScroll > 0) {
            int barLeft = this.leftPos + PANEL_WIDTH - 18;
            int barTop = contentTop;
            int barHeight = CONTENT_VISIBLE_LINES * LINE_HEIGHT - 2;
            int thumbHeight = Math.max(8, barHeight / (maxScroll + 1));
            int thumbTop = barTop + (barHeight - thumbHeight) * this.scrollOffset / maxScroll;

            graphics.fill(barLeft, barTop, barLeft + 3, barTop + barHeight, 0xFFB0B0B0);
            graphics.fill(barLeft, thumbTop, barLeft + 3, thumbTop + thumbHeight, 0xFF606060);
        }
    }

    private boolean isMouseInContentArea(double mouseX, double mouseY) {
        return mouseX >= this.leftPos + 8
                && mouseX < this.leftPos + PANEL_WIDTH - 8
                && mouseY >= this.topPos + CONTENT_TOP_OFFSET
                && mouseY < this.topPos + BUTTON_TOP_OFFSET - 12;
    }

    private int getCurrentContentLineCount() {
        CultivationSnapshot snapshot = CultivationClientCache.getSnapshot();
        CultivationStage stage = snapshot.stage();

        if (stage.isMortal()) {
            return 4;
        }

        if (this.page == Page.ATTRIBUTES) {
            return buildAttributeLines(snapshot).size();
        }

        int lineCount = 5;
        if (snapshot.isBreakthroughCoolingDown() || !stage.isMaxStage()) {
            lineCount++;
        }

        return lineCount;
    }

    private int getMaxScroll(int lineCount) {
        return Math.max(0, lineCount - CONTENT_VISIBLE_LINES);
    }

    private void addDamageBonusLine(List<Component> lines, String id, double value) {
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.attribute_damage_" + id,
                formatSignedPercent(value)
        ));
    }

    private void addResistanceLine(List<Component> lines, String id, double value) {
        lines.add(Component.translatable(
                "screen.eightwastelands.cultivation.attribute_resistance_" + id,
                formatSignedCompact(value)
        ));
    }

    private double getDamageNexusAttribute(Holder<Attribute> attribute) {
        if (this.minecraft == null || this.minecraft.player == null) {
            return 0.0D;
        }

        if (this.minecraft.player.getAttribute(attribute) == null) {
            return 0.0D;
        }

        return this.minecraft.player.getAttributeValue(attribute);
    }

    private Component getPrimaryButtonText() {
        CultivationSnapshot snapshot = CultivationClientCache.getSnapshot();
        CultivationStage stage = snapshot.stage();

        if (stage.isMortal()) {
            return Component.translatable("button.eightwastelands.cultivation.induce_qi");
        }

        if (snapshot.remainingTunaiUses() <= 0) {
            return Component.translatable("button.eightwastelands.cultivation.tunai_empty");
        }

        return Component.translatable("button.eightwastelands.cultivation.tunai");
    }

    private Component getBreakthroughButtonText() {
        CultivationSnapshot snapshot = CultivationClientCache.getSnapshot();
        CultivationStage stage = snapshot.stage();

        if (snapshot.isBreakthroughCoolingDown()) {
            return Component.translatable("button.eightwastelands.cultivation.breakthrough_cooldown");
        }

        if (stage.isMaxStage()) {
            return Component.translatable("button.eightwastelands.cultivation.max_stage");
        }

        return Component.translatable("button.eightwastelands.cultivation.breakthrough");
    }

    private void updateButtons() {
        if (this.primaryButton == null || this.breakthroughButton == null || this.xinfaButton == null) {
            return;
        }

        CultivationSnapshot snapshot = CultivationClientCache.getSnapshot();
        CultivationStage stage = snapshot.stage();

        this.trainingTabButton.active = this.page != Page.TRAINING;
        this.attributesTabButton.active = this.page != Page.ATTRIBUTES;
        this.xinfaButton.visible = !stage.isMortal();
        this.xinfaButton.active = this.xinfaButton.visible;

        this.primaryButton.setMessage(getPrimaryButtonText());
        this.primaryButton.visible = this.page == Page.TRAINING;
        this.primaryButton.active = this.page == Page.TRAINING
                && (stage.isMortal() || snapshot.remainingTunaiUses() > 0);

        this.breakthroughButton.visible = this.page == Page.TRAINING && !stage.isMortal();
        this.breakthroughButton.setMessage(getBreakthroughButtonText());
        this.breakthroughButton.active = this.page == Page.TRAINING
                && !snapshot.isBreakthroughCoolingDown()
                && !stage.isMaxStage()
                && snapshot.qi() >= stage.getRequiredQi();
    }

    private void switchPage(Page newPage) {
        this.page = newPage;
        this.scrollOffset = 0;
        updateButtons();
    }

    private void requestSync() {
        ClientPacketDistributor.sendToServer(new CultivationActionPayload(
                CultivationActionPayload.ACTION_REQUEST_SYNC
        ));
    }

    private double getBreakthroughPillChanceBonusInInventory() {
        if (this.minecraft == null || this.minecraft.player == null) {
            return 0.0D;
        }

        CultivationStage stage = CultivationClientCache.getSnapshot().stage();
        Optional<CultivationRealm> requiredRealm = CultivationManager.getRequiredBreakthroughPillRealm(stage);
        if (requiredRealm.isEmpty()) {
            return 0.0D;
        }

        AbstractBreakthroughPillItem bestPill = null;
        Inventory inventory = this.minecraft.player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            if (inventory.getItem(slot).getItem() instanceof AbstractBreakthroughPillItem pillItem
                    && pillItem.getRealm().filter(requiredRealm.get()::equals).isPresent()
                    && (bestPill == null || pillGradePriority(pillItem) > pillGradePriority(bestPill))) {
                bestPill = pillItem;
            }
        }

        return bestPill == null ? 0.0D : bestPill.getChanceBonus();
    }

    private int pillGradePriority(AbstractBreakthroughPillItem pillItem) {
        return switch (pillItem.getGrade()) {
            case IMMORTAL -> 4;
            case HIGH -> 3;
            case MIDDLE -> 2;
            case LOW -> 1;
            case NONE -> 0;
        };
    }

    private int getButtonRowLeft() {
        int rowWidth = PRIMARY_BUTTON_WIDTH
                + BREAKTHROUGH_BUTTON_WIDTH
                + DONE_BUTTON_WIDTH
                + BUTTON_GAP * 2;
        return this.leftPos + (PANEL_WIDTH - rowWidth) / 2;
    }

    private String formatCompact(double value) {
        if (Math.abs(value - Math.rint(value)) < 0.005D) {
            return Integer.toString((int) Math.rint(value));
        }

        return String.format(Locale.ROOT, "%.2f", value);
    }

    private String formatFixed(double value, int decimals) {
        return String.format(Locale.ROOT, "%." + decimals + "f", value);
    }

    private String formatPercent(double value) {
        return formatCompact(value * 100.0D) + "%";
    }

    private String formatSignedPercent(double value) {
        return formatSign(value) + formatPercent(Math.abs(value));
    }

    private String formatSignedCompact(double value) {
        return formatSign(value) + formatCompact(Math.abs(value));
    }

    private String formatSign(double value) {
        return value < 0.0D ? "-" : "+";
    }

    private String formatChance(double chance) {
        return Integer.toString((int) Math.round(chance * 100.0D));
    }

    private enum Page {
        TRAINING,
        ATTRIBUTES
    }
}
