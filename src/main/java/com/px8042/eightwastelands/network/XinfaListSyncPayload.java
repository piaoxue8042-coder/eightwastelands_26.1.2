package com.px8042.eightwastelands.network;

import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public record XinfaListSyncPayload(List<Entry> entries) implements CustomPacketPayload {

    public XinfaListSyncPayload {
        entries = List.copyOf(entries);
    }

    public record Entry(String xinfaId, int activatedNodes, int totalNodes) {
    }

    public static final Type<XinfaListSyncPayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(EightWastelands.MODID, "xinfa_list_sync")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, XinfaListSyncPayload> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public XinfaListSyncPayload decode(RegistryFriendlyByteBuf buffer) {
                    int count = buffer.readInt();
                    List<Entry> entries = new ArrayList<>(count);
                    for (int index = 0; index < count; index++) {
                        entries.add(new Entry(buffer.readUtf(), buffer.readInt(), buffer.readInt()));
                    }

                    return new XinfaListSyncPayload(entries);
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buffer, XinfaListSyncPayload payload) {
                    buffer.writeInt(payload.entries().size());
                    for (Entry entry : payload.entries()) {
                        buffer.writeUtf(entry.xinfaId());
                        buffer.writeInt(entry.activatedNodes());
                        buffer.writeInt(entry.totalNodes());
                    }
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
