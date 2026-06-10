package com.px8042.eightwastelands.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ToxicErosionEffect extends MobEffect {

    public static final int MAX_LAYERS = 5;
    private static final int MAX_INTERVAL_TICKS = 40;
    private static final int MIN_INTERVAL_TICKS = 10;

    public ToxicErosionEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity entity, int amplifier) {
        int layers = Math.min(MAX_LAYERS, amplifier + 1);
        float damage = layers;

        if (damage > 0.0F && entity.isAlive()) {
            entity.invulnerableTime = 0;
            entity.hurtServer(serverLevel, serverLevel.damageSources().magic(), damage);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int layers = Math.min(MAX_LAYERS, amplifier + 1);
        int interval = Math.max(MIN_INTERVAL_TICKS, MAX_INTERVAL_TICKS - (layers - 1) * 8);
        return duration % interval == 0;
    }
}
