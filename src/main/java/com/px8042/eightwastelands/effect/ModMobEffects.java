package com.px8042.eightwastelands.effect;

import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMobEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, EightWastelands.MODID);

    public static final DeferredHolder<MobEffect, MobEffect> HEAVENLY_TRIBULATION =
            MOB_EFFECTS.register("heavenly_tribulation",
                    () -> new ModMobEffect(MobEffectCategory.HARMFUL, 0x33CCFF));

    public static final DeferredHolder<MobEffect, MobEffect> EARTH_DISASTER =
            MOB_EFFECTS.register("earth_disaster",
                    () -> new EarthDisasterEffect(MobEffectCategory.HARMFUL, 0x8B7355));

    public static final DeferredHolder<MobEffect, MobEffect> KARMA =
            MOB_EFFECTS.register("karma",
                    () -> new ModMobEffect(MobEffectCategory.HARMFUL, 0x8A2BE2));

    public static final DeferredHolder<MobEffect, MobEffect> HEART_DEMON =
            MOB_EFFECTS.register("heart_demon",
                    () -> new ModMobEffect(MobEffectCategory.HARMFUL, 0xC71585));

    public static final DeferredHolder<MobEffect, MobEffect> SPIRIT_EXHAUSTION =
            MOB_EFFECTS.register("spirit_exhaustion",
                    () -> new ModMobEffect(MobEffectCategory.HARMFUL, 0x3F7F5F));

    public static final DeferredHolder<MobEffect, MobEffect> WITHERED_BLOOD =
            MOB_EFFECTS.register("withered_blood",
                    () -> new ModMobEffect(MobEffectCategory.HARMFUL, 0xAA0000));

    public static final DeferredHolder<MobEffect, MobEffect> LUCK_DEPRIVATION =
            MOB_EFFECTS.register("luck_deprivation",
                    () -> new ModMobEffect(MobEffectCategory.HARMFUL, 0xB8860B));

    public static final DeferredHolder<MobEffect, MobEffect> WIND_EVIL =
            MOB_EFFECTS.register("wind_evil",
                    () -> new ModMobEffect(MobEffectCategory.HARMFUL, 0xB0C4DE));

    public static final DeferredHolder<MobEffect, MobEffect> LIFE_EXHAUSTION =
            MOB_EFFECTS.register("life_exhaustion",
                    () -> new ModMobEffect(MobEffectCategory.HARMFUL, 0xC0C0C0));

    public static final DeferredHolder<MobEffect, MobEffect> FLAME_BAN =
            MOB_EFFECTS.register("flame_ban",
                    () -> new ModMobEffect(MobEffectCategory.HARMFUL, 0xFF5A1F));

    public static final DeferredHolder<MobEffect, MobEffect> COLD_ACCUMULATION =
            MOB_EFFECTS.register("cold_accumulation",
                    () -> new ColdAccumulationEffect(MobEffectCategory.HARMFUL, 0x66D9FF));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
    public static final DeferredHolder<MobEffect, MobEffect> SPRING_SHEARS_FATE_CUT =
            MOB_EFFECTS.register("spring_shears_fate_cut",
                    () -> new SpringShearsFateCutEffect(
                            MobEffectCategory.HARMFUL,
                            0x99FF99
                    ));
}
