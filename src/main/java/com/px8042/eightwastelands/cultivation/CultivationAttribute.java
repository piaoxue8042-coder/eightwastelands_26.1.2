package com.px8042.eightwastelands.cultivation;

import com.px8042.eightwastelands.EightWastelands;
import io.github.naimjeg.damagenexus.registry.ModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

public enum CultivationAttribute {
    MAX_HEALTH(Attributes.MAX_HEALTH, "cultivation_max_health"),
    ATTACK_DAMAGE(Attributes.ATTACK_DAMAGE, "cultivation_attack_damage"),
    ATTACK_SPEED(Attributes.ATTACK_SPEED, "cultivation_attack_speed"),
    MOVEMENT_SPEED(Attributes.MOVEMENT_SPEED, "cultivation_movement_speed"),

    CRIT_CHANCE(ModAttributes.CRIT_CHANCE, "cultivation_crit_chance"),
    CRIT_DAMAGE_ADDITIVE(ModAttributes.CRIT_DAMAGE_ADDITIVE, "cultivation_crit_damage_additive"),
    VULNERABLE_DAMAGE_ADDITIVE(ModAttributes.VULNERABLE_DAMAGE_ADDITIVE, "cultivation_vulnerable_damage_additive"),

    FIRE_DAMAGE_ADDITIVE(ModAttributes.FIRE_DAMAGE_ADDITIVE, "cultivation_fire_damage_additive"),
    COLD_DAMAGE_ADDITIVE(ModAttributes.COLD_DAMAGE_ADDITIVE, "cultivation_cold_damage_additive"),
    LIGHTNING_DAMAGE_ADDITIVE(ModAttributes.LIGHTNING_DAMAGE_ADDITIVE, "cultivation_lightning_damage_additive"),
    MAGIC_DAMAGE_ADDITIVE(ModAttributes.MAGIC_DAMAGE_ADDITIVE, "cultivation_magic_damage_additive"),
    WITHER_DAMAGE_ADDITIVE(ModAttributes.WITHER_DAMAGE_ADDITIVE, "cultivation_wither_damage_additive"),
    POISON_DAMAGE_ADDITIVE(ModAttributes.POISON_DAMAGE_ADDITIVE, "cultivation_poison_damage_additive"),
    MELEE_DAMAGE_ADDITIVE(ModAttributes.MELEE_DAMAGE_ADDITIVE, "cultivation_melee_damage_additive"),
    PROJECTILE_DAMAGE_ADDITIVE(ModAttributes.PROJECTILE_DAMAGE_ADDITIVE, "cultivation_projectile_damage_additive"),
    KINETIC_DAMAGE_ADDITIVE(ModAttributes.KINETIC_DAMAGE_ADDITIVE, "cultivation_kinetic_damage_additive"),

    DODGE_CHANCE(ModAttributes.DODGE_CHANCE, "cultivation_dodge_chance"),
    RESISTANCE_PHYSICAL(ModAttributes.RESISTANCE_PHYSICAL, "cultivation_resistance_physical"),
    RESISTANCE_FIRE(ModAttributes.RESISTANCE_FIRE, "cultivation_resistance_fire"),
    RESISTANCE_COLD(ModAttributes.RESISTANCE_COLD, "cultivation_resistance_cold"),
    RESISTANCE_LIGHTNING(ModAttributes.RESISTANCE_LIGHTNING, "cultivation_resistance_lightning"),
    RESISTANCE_MAGIC(ModAttributes.RESISTANCE_MAGIC, "cultivation_resistance_magic"),
    RESISTANCE_WITHER(ModAttributes.RESISTANCE_WITHER, "cultivation_resistance_wither"),
    RESISTANCE_POISON(ModAttributes.RESISTANCE_POISON, "cultivation_resistance_poison"),
    RESISTANCE_MELEE(ModAttributes.RESISTANCE_MELEE, "cultivation_resistance_melee"),
    RESISTANCE_PROJECTILE(ModAttributes.RESISTANCE_PROJECTILE, "cultivation_resistance_projectile"),
    RESISTANCE_KINETIC(ModAttributes.RESISTANCE_KINETIC, "cultivation_resistance_kinetic"),

    THORNS(ModAttributes.THORNS, "cultivation_thorns"),
    HEALING_RECEIVED(ModAttributes.HEALING_RECEIVED, "cultivation_healing_received");

    private final Holder<Attribute> attribute;
    private final Identifier modifierId;

    CultivationAttribute(Holder<Attribute> attribute, String modifierPath) {
        this.attribute = attribute;
        this.modifierId = Identifier.fromNamespaceAndPath(EightWastelands.MODID, modifierPath);
    }

    public Holder<Attribute> getAttribute() {
        return this.attribute;
    }

    public Identifier getModifierId() {
        return this.modifierId;
    }
}
