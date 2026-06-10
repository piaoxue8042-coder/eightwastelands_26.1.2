package com.px8042.eightwastelands.effect;

import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class RootedEffect extends MobEffect {

    public RootedEffect(MobEffectCategory category, int color) {
        super(category, color);

        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                Identifier.fromNamespaceAndPath(EightWastelands.MODID, "rooted_movement_speed"),
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                amplifier -> -1.0D
        );
    }
}
