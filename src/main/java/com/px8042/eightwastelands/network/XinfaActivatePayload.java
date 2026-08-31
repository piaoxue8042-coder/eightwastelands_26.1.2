package com.px8042.eightwastelands.network;

import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record XinfaActivatePayload(String xinfaId, String nodeId) implements CustomPacketPayload {

    public static final Type<XinfaActivatePayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(EightWastelands.MODID, "xinfa_activate")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, XinfaActivatePayload> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public XinfaActivatePayload decode(RegistryFriendlyByteBuf buffer) {
                    return new XinfaActivatePayload(buffer.readUtf(), buffer.readUtf());
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buffer, XinfaActivatePayload payload) {
                    buffer.writeUtf(payload.xinfaId());
                    buffer.writeUtf(payload.nodeId());
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
