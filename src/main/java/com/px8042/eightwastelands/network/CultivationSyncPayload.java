package com.px8042.eightwastelands.network;

import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record CultivationSyncPayload(
        int stageIndex,
        int qi,
        int tunaiUsedToday,
        int tunaiDailyUses,
        long breakthroughCooldownTicks,
        int breakthroughFailureCount,
        double zhenyuan,
        double maxZhenyuan,
        double zhenyuanRegenPerSecond,
        double health,
        double maxHealth,
        double attackDamage,
        double attackSpeed,
        double movementSpeed,
        int alchemyRealmIndex,
        int alchemyMasteryIndex,
        int alchemyExperience,
        int alchemyRequiredExperience
) implements CustomPacketPayload {

    public static final Type<CultivationSyncPayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(EightWastelands.MODID, "cultivation_sync")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, CultivationSyncPayload> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public CultivationSyncPayload decode(RegistryFriendlyByteBuf buffer) {
                    return new CultivationSyncPayload(
                            buffer.readInt(),
                            buffer.readInt(),
                            buffer.readInt(),
                            buffer.readInt(),
                            buffer.readLong(),
                            buffer.readInt(),
                            buffer.readDouble(),
                            buffer.readDouble(),
                            buffer.readDouble(),
                            buffer.readDouble(),
                            buffer.readDouble(),
                            buffer.readDouble(),
                            buffer.readDouble(),
                            buffer.readDouble(),
                            buffer.readInt(),
                            buffer.readInt(),
                            buffer.readInt(),
                            buffer.readInt()
                    );
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buffer, CultivationSyncPayload payload) {
                    buffer.writeInt(payload.stageIndex());
                    buffer.writeInt(payload.qi());
                    buffer.writeInt(payload.tunaiUsedToday());
                    buffer.writeInt(payload.tunaiDailyUses());
                    buffer.writeLong(payload.breakthroughCooldownTicks());
                    buffer.writeInt(payload.breakthroughFailureCount());
                    buffer.writeDouble(payload.zhenyuan());
                    buffer.writeDouble(payload.maxZhenyuan());
                    buffer.writeDouble(payload.zhenyuanRegenPerSecond());
                    buffer.writeDouble(payload.health());
                    buffer.writeDouble(payload.maxHealth());
                    buffer.writeDouble(payload.attackDamage());
                    buffer.writeDouble(payload.attackSpeed());
                    buffer.writeDouble(payload.movementSpeed());
                    buffer.writeInt(payload.alchemyRealmIndex());
                    buffer.writeInt(payload.alchemyMasteryIndex());
                    buffer.writeInt(payload.alchemyExperience());
                    buffer.writeInt(payload.alchemyRequiredExperience());
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
