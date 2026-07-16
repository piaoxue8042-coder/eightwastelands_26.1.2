package com.px8042.eightwastelands.client;

import com.px8042.eightwastelands.alchemy.AlchemyFormula;
import com.px8042.eightwastelands.alchemy.AlchemyFormulas;
import com.px8042.eightwastelands.alchemy.AlchemyManager;
import com.px8042.eightwastelands.cultivation.CultivationClientCache;
import com.px8042.eightwastelands.cultivation.CultivationSnapshot;
import com.px8042.eightwastelands.client.gui.CultivationScreen;
import com.px8042.eightwastelands.network.CultivationActionPayload;
import com.px8042.eightwastelands.item.pill.PillGrade;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class CultivationClientEvents {

    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof InventoryScreen inventoryScreen)) {
            return;
        }

        int buttonX = inventoryScreen.width / 2 - 88;
        int buttonY = inventoryScreen.height / 2 - 102;

        event.addListener(Button.builder(
                        Component.translatable("button.eightwastelands.cultivation.bookmark"),
                        button -> {
                            Minecraft.getInstance().setScreen(new CultivationScreen());
                            ClientPacketDistributor.sendToServer(new CultivationActionPayload(
                                    CultivationActionPayload.ACTION_REQUEST_SYNC
                            ));
                        }
                )
                .bounds(buttonX, buttonY, 48, 18)
                .tooltip(Tooltip.create(Component.translatable("screen.eightwastelands.cultivation")))
                .build());
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (event.getEntity() == null) {
            return;
        }

        Optional<AlchemyFormula> formula = AlchemyFormulas.find(event.getItemStack());
        if (formula.isEmpty()) {
            return;
        }

        appendAlchemyChanceTooltip(event.getToolTip(), formula.get());
    }

    private static void appendAlchemyChanceTooltip(List<Component> tooltip, AlchemyFormula formula) {
        CultivationSnapshot snapshot = CultivationClientCache.getSnapshot();
        Optional<AlchemyManager.AlchemyGradeChances> chances = AlchemyManager.getGradeChances(
                snapshot.alchemyRealm(),
                snapshot.alchemyMastery(),
                formula.realm()
        );

        tooltip.add(Component.empty());
        tooltip.add(Component.translatable(
                "tooltip.eightwastelands.alchemy_pill_base.current_alchemy",
                snapshot.alchemyRealm().getDisplayName(),
                snapshot.alchemyMastery().getDisplayName()
        ).withStyle(ChatFormatting.DARK_AQUA));

        if (chances.isEmpty()) {
            tooltip.add(Component.translatable(
                    "tooltip.eightwastelands.alchemy_pill_base.chance_unavailable"
            ).withStyle(ChatFormatting.RED));
            return;
        }

        AlchemyManager.AlchemyGradeChances chanceValues = chances.get();
        if (chanceValues.forcedLowGrade()) {
            tooltip.add(Component.translatable(
                    "tooltip.eightwastelands.alchemy_pill_base.chance_forced_low"
            ).withStyle(ChatFormatting.YELLOW));
        }

        tooltip.add(Component.translatable(
                "tooltip.eightwastelands.alchemy_pill_base.chance_header"
        ).withStyle(ChatFormatting.DARK_AQUA));

        for (PillGrade grade : PillGrade.gradedValues()) {
            tooltip.add(Component.translatable(
                    "tooltip.eightwastelands.alchemy_pill_base.chance_entry",
                    grade.getDisplayName(),
                    chanceValues.get(grade)
            ).withStyle(ChatFormatting.AQUA));
        }
    }
}
