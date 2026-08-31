package com.px8042.eightwastelands.network;

import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public record XinfaSyncPayload(
        String xinfaId,
        List<String> activatedNodeIds,
        int wudaoSandCount,
        boolean openScreen
) implements CustomPacketPayload {

    public XinfaSyncPayload {
        activatedNodeIds = List.copyOf(activatedNodeIds);
    }

    public static final Type<XinfaSyncPayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(EightWastelands.MODID, "xinfa_sync")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, XinfaSyncPayload> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public XinfaSyncPayload decode(RegistryFriendlyByteBuf buffer) {
                    String xinfaId = buffer.readUtf();
                    int count = buffer.readInt();
                    List<String> activatedNodeIds = new ArrayList<>(count);
                    for (int index = 0; index < count; index++) {
                        activatedNodeIds.add(buffer.readUtf());
                    }

                    return new XinfaSyncPayload(
                            xinfaId,
                            activatedNodeIds,
                            buffer.readInt(),
                            buffer.readBoolean()
                    );
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buffer, XinfaSyncPayload payload) {
                    buffer.writeUtf(payload.xinfaId());
                    buffer.writeInt(payload.activatedNodeIds().size());
                    for (String nodeId : payload.activatedNodeIds()) {
                        buffer.writeUtf(nodeId);
                    }

                    buffer.writeInt(payload.wudaoSandCount());
                    buffer.writeBoolean(payload.openScreen());
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
