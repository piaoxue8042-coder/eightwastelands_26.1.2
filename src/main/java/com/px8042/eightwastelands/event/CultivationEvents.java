package com.px8042.eightwastelands.event;

import com.px8042.eightwastelands.alchemy.AlchemyManager;
import com.px8042.eightwastelands.cultivation.CultivationManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class CultivationEvents {

    private static final int ATTRIBUTE_REFRESH_INTERVAL = 20;

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CultivationManager.applyAttributes(player);
            CultivationManager.syncTo(player);
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CultivationManager.applyAttributes(player);
            CultivationManager.syncTo(player);
        }
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CultivationManager.applyAttributes(player);
            CultivationManager.syncTo(player);
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        CultivationManager.copyData(
                event.getOriginal().getPersistentData(),
                event.getEntity().getPersistentData()
        );
        AlchemyManager.copyData(
                event.getOriginal().getPersistentData(),
                event.getEntity().getPersistentData()
        );
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (player.level().getGameTime() % ATTRIBUTE_REFRESH_INTERVAL != 0L) {
            return;
        }

        boolean zhenyuanChanged = CultivationManager.tickZhenyuan(player);
        CultivationManager.applyAttributes(player);
        if (zhenyuanChanged) {
            CultivationManager.syncTo(player);
        }
    }
}
