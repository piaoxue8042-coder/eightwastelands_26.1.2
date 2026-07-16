package com.px8042.eightwastelands.client;

import com.px8042.eightwastelands.client.gui.CultivationScreen;
import com.px8042.eightwastelands.network.CultivationActionPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

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
}
