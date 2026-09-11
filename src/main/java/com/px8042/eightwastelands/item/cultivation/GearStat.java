package com.px8042.eightwastelands.item.cultivation;

import io.github.naimjeg.damagenexus.registry.ModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * A stat that can be granted by a generated gear affix.
 */
public enum GearStat {
    MAX_HEALTH("max_health", Attributes.MAX_HEALTH),
    ATTACK_DAMAGE("attack_damage", Attributes.ATTACK_DAMAGE),
    ATTACK_SPEED("attack_speed", Attributes.ATTACK_SPEED),
    MOVEMENT_SPEED("movement_speed", Attributes.MOVEMENT_SPEED),
    ARMOR("armor", Attributes.ARMOR),
    ARMOR_TOUGHNESS("armor_toughness", Attributes.ARMOR_TOUGHNESS),

    CRIT_CHANCE("crit_chance", ModAttributes.CRIT_CHANCE),
    CRIT_DAMAGE("crit_damage", ModAttributes.CRIT_DAMAGE_ADDITIVE),
    MELEE_DAMAGE("melee_damage", ModAttributes.MELEE_DAMAGE_ADDITIVE),
    PROJECTILE_DAMAGE("projectile_damage", ModAttributes.PROJECTILE_DAMAGE_ADDITIVE),
    FIRE_DAMAGE("fire_damage", ModAttributes.FIRE_DAMAGE_ADDITIVE),
    COLD_DAMAGE("cold_damage", ModAttributes.COLD_DAMAGE_ADDITIVE),
    LIGHTNING_DAMAGE("lightning_damage", ModAttributes.LIGHTNING_DAMAGE_ADDITIVE),
    MAGIC_DAMAGE("magic_damage", ModAttributes.MAGIC_DAMAGE_ADDITIVE),
    WITHER_DAMAGE("wither_damage", ModAttributes.WITHER_DAMAGE_ADDITIVE),
    POISON_DAMAGE("poison_damage", ModAttributes.POISON_DAMAGE_ADDITIVE),
    KINETIC_DAMAGE("kinetic_damage", ModAttributes.KINETIC_DAMAGE_ADDITIVE),

    PHYSICAL_RESISTANCE("physical_resistance", ModAttributes.RESISTANCE_PHYSICAL),
    MELEE_RESISTANCE("melee_resistance", ModAttributes.RESISTANCE_MELEE),
    PROJECTILE_RESISTANCE("projectile_resistance", ModAttributes.RESISTANCE_PROJECTILE),
    FIRE_RESISTANCE("fire_resistance", ModAttributes.RESISTANCE_FIRE),
    COLD_RESISTANCE("cold_resistance", ModAttributes.RESISTANCE_COLD),
    LIGHTNING_RESISTANCE("lightning_resistance", ModAttributes.RESISTANCE_LIGHTNING),
    MAGIC_RESISTANCE("magic_resistance", ModAttributes.RESISTANCE_MAGIC),
    WITHER_RESISTANCE("wither_resistance", ModAttributes.RESISTANCE_WITHER),
    POISON_RESISTANCE("poison_resistance", ModAttributes.RESISTANCE_POISON),
    KINETIC_RESISTANCE("kinetic_resistance", ModAttributes.RESISTANCE_KINETIC),

    MAX_ZHENYUAN("max_zhenyuan", null),
    ZHENYUAN_REGEN("zhenyuan_regen", null),
    HEALTH_REGEN("health_regen", null);

    private final String serializedName;
    private final Holder<Attribute> attribute;

    GearStat(String serializedName, Holder<Attribute> attribute) {
        this.serializedName = serializedName;
        this.attribute = attribute;
    }

    public String getSerializedName() {
        return this.serializedName;
    }

    public Holder<Attribute> getAttribute() {
        return this.attribute;
    }

    public boolean isAttribute() {
        return this.attribute != null;
    }

    public boolean isPercent() {
        return switch (this) {
            case CRIT_CHANCE,
                    CRIT_DAMAGE,
                    MELEE_DAMAGE,
                    PROJECTILE_DAMAGE,
                    FIRE_DAMAGE,
                    COLD_DAMAGE,
                    LIGHTNING_DAMAGE,
                    MAGIC_DAMAGE,
                    WITHER_DAMAGE,
                    POISON_DAMAGE,
                    KINETIC_DAMAGE -> true;
            default -> false;
        };
    }
}
