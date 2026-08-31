package com.px8042.eightwastelands.network;

import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record XinfaOpenPayload(String xinfaId) implements CustomPacketPayload {

    public static final Type<XinfaOpenPayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(EightWastelands.MODID, "xinfa_open")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, XinfaOpenPayload> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public XinfaOpenPayload decode(RegistryFriendlyByteBuf buffer) {
                    return new XinfaOpenPayload(buffer.readUtf());
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buffer, XinfaOpenPayload payload) {
                    buffer.writeUtf(payload.xinfaId());
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
