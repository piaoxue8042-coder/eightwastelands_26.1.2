package com.px8042.eightwastelands.effect;

import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ColdAccumulationEffect extends MobEffect {

    private static final double MOVEMENT_SPEED_PENALTY_PER_LAYER = -0.10D;
    private static final int MAX_LAYERS = 5;

    public ColdAccumulationEffect(MobEffectCategory category, int color) {
        super(category, color);

        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                Identifier.fromNamespaceAndPath(
                        EightWastelands.MODID,
                        "cold_accumulation_movement_speed"
                ),
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                amplifier -> MOVEMENT_SPEED_PENALTY_PER_LAYER * Math.min(MAX_LAYERS, amplifier + 1)
        );
    }
}
