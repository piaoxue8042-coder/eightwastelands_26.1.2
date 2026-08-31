package com.px8042.eightwastelands.network;

import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record CultivationActionPayload(int action) implements CustomPacketPayload {

    public static final int ACTION_MAIN_BUTTON = 0;
    public static final int ACTION_REQUEST_SYNC = 1;
    public static final int ACTION_BREAKTHROUGH = 2;
    public static final int ACTION_OPEN_XINFA_LIST = 3;

    public static final Type<CultivationActionPayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(EightWastelands.MODID, "cultivation_action")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, CultivationActionPayload> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public CultivationActionPayload decode(RegistryFriendlyByteBuf buffer) {
                    return new CultivationActionPayload(buffer.readInt());
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buffer, CultivationActionPayload payload) {
                    buffer.writeInt(payload.action());
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
