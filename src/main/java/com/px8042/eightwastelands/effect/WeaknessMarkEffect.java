package com.px8042.eightwastelands.effect;

import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class WeaknessMarkEffect extends MobEffect {

    private static final int MAX_LAYERS = 5;
    private static final double ATTACK_DAMAGE_PENALTY_PER_LAYER = -0.10D;

    public WeaknessMarkEffect(MobEffectCategory category, int color) {
        super(category, color);

        this.addAttributeModifier(
                Attributes.ATTACK_DAMAGE,
                Identifier.fromNamespaceAndPath(EightWastelands.MODID, "weakness_mark_attack_damage"),
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                amplifier -> ATTACK_DAMAGE_PENALTY_PER_LAYER * Math.min(MAX_LAYERS, amplifier + 1)
        );
    }
}
