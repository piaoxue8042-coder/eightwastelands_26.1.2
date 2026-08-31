package com.px8042.eightwastelands.network;

import com.px8042.eightwastelands.EightWastelands;
import com.px8042.eightwastelands.cultivation.CultivationClientCache;
import com.px8042.eightwastelands.cultivation.CultivationManager;
import com.px8042.eightwastelands.cultivation.CultivationSnapshot;
import com.px8042.eightwastelands.xinfa.XinfaManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetwork {

    private static final String NETWORK_VERSION = "8";

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
        registrar.playToServer(
                XinfaActivatePayload.TYPE,
                XinfaActivatePayload.STREAM_CODEC,
                ModNetwork::handleXinfaActivate
        );
        registrar.playToServer(
                XinfaOpenPayload.TYPE,
                XinfaOpenPayload.STREAM_CODEC,
                ModNetwork::handleXinfaOpen
        );
        registrar.playToClient(
                XinfaSyncPayload.TYPE,
                XinfaSyncPayload.STREAM_CODEC,
                ModNetwork::handleXinfaSync
        );
        registrar.playToClient(
                XinfaListSyncPayload.TYPE,
                XinfaListSyncPayload.STREAM_CODEC,
                ModNetwork::handleXinfaListSync
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
            return;
        }

        if (payload.action() == CultivationActionPayload.ACTION_OPEN_XINFA_LIST) {
            XinfaManager.openXinfaList(player);
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

    private static void handleXinfaActivate(XinfaActivatePayload payload, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player) {
            XinfaManager.handleActivateNode(player, payload.xinfaId(), payload.nodeId());
        }
    }

    private static void handleXinfaOpen(XinfaOpenPayload payload, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player) {
            XinfaManager.openXinfa(player, payload.xinfaId());
        }
    }

    private static void handleXinfaSync(XinfaSyncPayload payload, IPayloadContext context) {
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            com.px8042.eightwastelands.client.XinfaClientPacketHandler.handleSync(payload);
        }
    }

    private static void handleXinfaListSync(XinfaListSyncPayload payload, IPayloadContext context) {
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            com.px8042.eightwastelands.client.XinfaClientPacketHandler.handleListSync(payload);
        }
    }
}
