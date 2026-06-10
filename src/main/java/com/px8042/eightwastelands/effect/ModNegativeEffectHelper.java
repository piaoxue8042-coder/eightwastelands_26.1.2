package com.px8042.eightwastelands.effect;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public final class ModNegativeEffectHelper {

    public static final int DEFAULT_MAX_LAYERS = 5;
    public static final int BLEEDING_MAX_LAYERS = 10;
    public static final int ROOTED_MAX_LAYERS = 1;
    public static final int BASE_DURATION_TICKS = 10 * 20;
    public static final int DURATION_TICKS_PER_EXTRA_LAYER = 5 * 20;

    private ModNegativeEffectHelper() {
    }

    public static boolean addStackingEffect(LivingEntity target, Holder<MobEffect> effect) {
        return addStackingEffect(target, effect, 1, DEFAULT_MAX_LAYERS);
    }

    public static boolean addBleeding(LivingEntity target) {
        return addStackingEffect(target, ModMobEffects.BLEEDING, 1, BLEEDING_MAX_LAYERS);
    }

    public static boolean addToxicErosion(LivingEntity target) {
        return addStackingEffect(target, ModMobEffects.TOXIC_EROSION);
    }

    public static boolean addArmorBreak(LivingEntity target) {
        return addStackingEffect(target, ModMobEffects.ARMOR_BREAK);
    }

    public static boolean addWeaknessMark(LivingEntity target) {
        return addStackingEffect(target, ModMobEffects.WEAKNESS_MARK);
    }

    public static boolean addSlowdown(LivingEntity target) {
        return addStackingEffect(target, ModMobEffects.SLOWDOWN);
    }

    public static boolean addRooted(LivingEntity target) {
        return addStackingEffect(target, ModMobEffects.ROOTED, 1, ROOTED_MAX_LAYERS);
    }

    public static boolean addStackingEffect(LivingEntity target, Holder<MobEffect> effect, int addedLayers, int maxLayers) {
        if (target.level().isClientSide()) {
            return false;
        }

        int oldLayers = getEffectLayers(target.getEffect(effect), maxLayers);
        int newLayers = Math.min(Math.max(1, maxLayers), oldLayers + Math.max(1, addedLayers));
        return target.addEffect(new MobEffectInstance(
                effect,
                getDurationTicksForLayers(newLayers),
                newLayers - 1
        ));
    }

    public static int getEffectLayers(MobEffectInstance effectInstance, int maxLayers) {
        if (effectInstance == null) {
            return 0;
        }

        return Math.min(Math.max(1, maxLayers), effectInstance.getAmplifier() + 1);
    }

    public static int getDurationTicksForLayers(int layers) {
        int safeLayers = Math.max(1, layers);
        return BASE_DURATION_TICKS + (safeLayers - 1) * DURATION_TICKS_PER_EXTRA_LAYER;
    }
}
