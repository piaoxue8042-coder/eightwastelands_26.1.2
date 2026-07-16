package com.px8042.eightwastelands.network;

import com.px8042.eightwastelands.EightWastelands;
import com.px8042.eightwastelands.cultivation.CultivationClientCache;
import com.px8042.eightwastelands.cultivation.CultivationManager;
import com.px8042.eightwastelands.cultivation.CultivationSnapshot;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetwork {

    private static final String NETWORK_VERSION = "5";

    private ModNetwork() {
    }

    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(EightWastelands.MODID).versioned(NETWORK_VERSION);

        registrar.playToServer(
                CultivationActionPayload.TYPE,
                CultivationActionPayload.STREAM_CODEC,
                ModNetwork::handleCultivationAction
        );
        registrar.playToClient(
                CultivationSyncPayload.TYPE,
                CultivationSyncPayload.STREAM_CODEC,
                ModNetwork::handleCultivationSync
        );
    }

    private static void handleCultivationAction(CultivationActionPayload payload, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }

        if (payload.action() == CultivationActionPayload.ACTION_REQUEST_SYNC) {
            CultivationManager.syncTo(player);
            return;
        }

        if (payload.action() == CultivationActionPayload.ACTION_MAIN_BUTTON) {
            CultivationManager.handleMainButton(player);
            return;
        }

        if (payload.action() == CultivationActionPayload.ACTION_BREAKTHROUGH) {
            CultivationManager.handleBreakthrough(player);
        }
    }

    private static void handleCultivationSync(CultivationSyncPayload payload, IPayloadContext context) {
        CultivationClientCache.setSnapshot(new CultivationSnapshot(
                payload.stageIndex(),
                payload.qi(),
                payload.tunaiUsedToday(),
                payload.tunaiDailyUses(),
                payload.breakthroughCooldownTicks(),
                payload.breakthroughFailureCount(),
                payload.zhenyuan(),
                payload.maxZhenyuan(),
                payload.zhenyuanRegenPerSecond(),
                payload.health(),
                payload.maxHealth(),
                payload.attackDamage(),
                payload.attackSpeed(),
                payload.movementSpeed(),
                payload.alchemyRealmIndex(),
                payload.alchemyMasteryIndex(),
                payload.alchemyExperience(),
                payload.alchemyRequiredExperience()
        ));
    }
}
