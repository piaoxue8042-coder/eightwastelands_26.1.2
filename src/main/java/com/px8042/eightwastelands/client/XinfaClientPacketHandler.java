package com.px8042.eightwastelands.client;

import com.px8042.eightwastelands.client.gui.XinfaScreen;
import com.px8042.eightwastelands.client.gui.XinfaListScreen;
import com.px8042.eightwastelands.network.XinfaListSyncPayload;
import com.px8042.eightwastelands.network.XinfaSyncPayload;
import com.px8042.eightwastelands.xinfa.XinfaClientCache;
import net.minecraft.client.Minecraft;

public final class XinfaClientPacketHandler {

    private XinfaClientPacketHandler() {
    }

    public static void handleSync(XinfaSyncPayload payload) {
        XinfaClientCache.set(payload.xinfaId(), payload.activatedNodeIds(), payload.wudaoSandCount());

        Minecraft minecraft = Minecraft.getInstance();
        if (payload.openScreen() || minecraft.screen instanceof XinfaScreen) {
            if (minecraft.screen instanceof XinfaScreen screen && screen.getXinfaId().equals(payload.xinfaId())) {
                screen.refreshFromCache();
            } else {
                minecraft.setScreen(new XinfaScreen(payload.xinfaId()));
            }
        }
    }

    public static void handleListSync(XinfaListSyncPayload payload) {
        XinfaClientCache.setLearnedXinfaEntries(payload.entries());

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof XinfaListScreen screen) {
            screen.refreshFromCache();
        } else {
            minecraft.setScreen(new XinfaListScreen());
        }
    }
}
