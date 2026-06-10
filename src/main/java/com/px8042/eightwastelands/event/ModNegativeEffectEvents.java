package com.px8042.eightwastelands.event;

import com.px8042.eightwastelands.effect.ModMobEffects;
import com.px8042.eightwastelands.effect.ModNegativeEffectHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

public class ModNegativeEffectEvents {

    private static final String COLD_ACCUMULATION_DURATION_TICKS =
            "eightwastelands_cold_accumulation_duration_ticks";
    private static final float FLAME_BAN_FIRE_DAMAGE_BONUS_PER_LAYER = 0.10F;

    @SubscribeEvent
    public void onLivingHeal(LivingHealEvent event) {
        if (event.getEntity().hasEffect(ModMobEffects.FLAME_BAN)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent.Pre event) {
        applyFlameBanFireDamageBonus(event);
    }

    @SubscribeEvent
    public void onMobEffectAdded(MobEffectEvent.Added event) {
        MobEffectInstance effectInstance = event.getEffectInstance();
        if (effectInstance.getEffect().value() != ModMobEffects.COLD_ACCUMULATION.get()) {
            return;
        }

        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) {
            return;
        }

        int layers = ModNegativeEffectHelper.getEffectLayers(
                effectInstance,
                ModNegativeEffectHelper.DEFAULT_MAX_LAYERS
        );
        int durationTicks = effectInstance.isInfiniteDuration()
                ? ModNegativeEffectHelper.getDurationTicksForLayers(layers)
                : effectInstance.getDuration();

        entity.getPersistentData().putInt(
                COLD_ACCUMULATION_DURATION_TICKS,
                Math.max(1, durationTicks)
        );
    }

    @SubscribeEvent
    public void onMobEffectExpired(MobEffectEvent.Expired event) {
        MobEffectInstance effectInstance = event.getEffectInstance();
        if (effectInstance == null || effectInstance.getEffect().value() != ModMobEffects.COLD_ACCUMULATION.get()) {
            return;
        }

        LivingEntity entity = event.getEntity();
        if (!(entity.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        applyColdAccumulationBurst(serverLevel, entity, effectInstance);
        clearColdAccumulationData(entity);
    }

    @SubscribeEvent
    public void onMobEffectRemoved(MobEffectEvent.Remove event) {
        if (event.getEffect().value() == ModMobEffects.COLD_ACCUMULATION.get()) {
            clearColdAccumulationData(event.getEntity());
        }
    }

    private void applyFlameBanFireDamageBonus(LivingDamageEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance effectInstance = entity.getEffect(ModMobEffects.FLAME_BAN);
        if (effectInstance == null) {
            return;
        }

        if (!event.getSource().is(DamageTypeTags.IS_FIRE)) {
            return;
        }

        int layers = ModNegativeEffectHelper.getEffectLayers(
                effectInstance,
                ModNegativeEffectHelper.DEFAULT_MAX_LAYERS
        );
        event.setNewDamage(event.getNewDamage() * (1.0F + layers * FLAME_BAN_FIRE_DAMAGE_BONUS_PER_LAYER));
    }

    private void applyColdAccumulationBurst(
            ServerLevel serverLevel,
            LivingEntity entity,
            MobEffectInstance effectInstance
    ) {
        if (!entity.isAlive()) {
            return;
        }

        int layers = ModNegativeEffectHelper.getEffectLayers(
                effectInstance,
                ModNegativeEffectHelper.DEFAULT_MAX_LAYERS
        );
        int durationTicks = getStoredColdAccumulationDurationTicks(entity, layers);
        int durationSeconds = Math.max(1, durationTicks / 20);
        float damage = durationSeconds * layers;

        if (damage <= 0.0F) {
            return;
        }

        entity.invulnerableTime = 0;
        entity.hurtServer(serverLevel, serverLevel.damageSources().freeze(), damage);
    }

    private int getStoredColdAccumulationDurationTicks(LivingEntity entity, int layers) {
        CompoundTag data = entity.getPersistentData();
        return data.getIntOr(
                COLD_ACCUMULATION_DURATION_TICKS,
                ModNegativeEffectHelper.getDurationTicksForLayers(layers)
        );
    }

    private void clearColdAccumulationData(LivingEntity entity) {
        entity.getPersistentData().remove(COLD_ACCUMULATION_DURATION_TICKS);
    }
}
