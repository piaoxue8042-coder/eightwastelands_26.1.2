package com.px8042.eightwastelands.effect;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BleedingEffect extends MobEffect {

    public static final int MAX_LAYERS = 10;
    private static final int DAMAGE_INTERVAL_TICKS = 20;
    private static final String NEXT_DAMAGE =
            "eightwastelands_bleeding_next_damage";

    public BleedingEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity entity, int amplifier) {
        int layers = Math.min(MAX_LAYERS, amplifier + 1);
        CompoundTag data = entity.getPersistentData();
        float damage = Math.max(layers, data.getFloatOr(NEXT_DAMAGE, layers));

        if (damage > 0.0F && entity.isAlive()) {
            entity.invulnerableTime = 0;
            entity.hurtServer(serverLevel, serverLevel.damageSources().wither(), damage);
        }

        data.putFloat(NEXT_DAMAGE, damage + layers);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % DAMAGE_INTERVAL_TICKS == 0;
    }

    public static void clearData(LivingEntity entity) {
        entity.getPersistentData().remove(NEXT_DAMAGE);
    }
}
