package com.px8042.eightwastelands.effect;

import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ArmorBreakEffect extends MobEffect {

    private static final int MAX_LAYERS = 5;
    private static final double ARMOR_PENALTY_PER_LAYER = -0.10D;

    public ArmorBreakEffect(MobEffectCategory category, int color) {
        super(category, color);

        this.addAttributeModifier(
                Attributes.ARMOR,
                Identifier.fromNamespaceAndPath(EightWastelands.MODID, "armor_break_armor"),
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                amplifier -> ARMOR_PENALTY_PER_LAYER * Math.min(MAX_LAYERS, amplifier + 1)
        );
        this.addAttributeModifier(
                Attributes.ARMOR_TOUGHNESS,
                Identifier.fromNamespaceAndPath(EightWastelands.MODID, "armor_break_armor_toughness"),
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                amplifier -> ARMOR_PENALTY_PER_LAYER * Math.min(MAX_LAYERS, amplifier + 1)
        );
    }
}
