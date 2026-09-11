package com.px8042.eightwastelands.item.cultivation;

import java.util.ArrayList;
import java.util.List;

/**
 * Central tables for fixed and random gear affixes.
 */
public final class GearDefinitions {

    private static final List<GearAffix> COMMON_AFFIXES = List.of(
            GearAffix.VITALITY,
            GearAffix.SPIRIT_RESERVOIR,
            GearAffix.SPIRIT_FLOW,
            GearAffix.BLOOD_FLOW,
            GearAffix.STRENGTH,
            GearAffix.HASTE,
            GearAffix.SWIFTNESS,
            GearAffix.INSIGHT,
            GearAffix.DEADLY_EDGE,
            GearAffix.MARTIAL_POWER,
            GearAffix.RANGED_POWER,
            GearAffix.FLAME_POWER,
            GearAffix.FROST_POWER,
            GearAffix.THUNDER_POWER,
            GearAffix.ARCANE_POWER,
            GearAffix.WITHER_POWER,
            GearAffix.VENOM_POWER,
            GearAffix.KINETIC_POWER,
            GearAffix.PLATING,
            GearAffix.TOUGHNESS,
            GearAffix.PHYSICAL_WARD,
            GearAffix.MELEE_WARD,
            GearAffix.PROJECTILE_WARD,
            GearAffix.FLAME_WARD,
            GearAffix.FROST_WARD,
            GearAffix.THUNDER_WARD,
            GearAffix.ARCANE_WARD,
            GearAffix.WITHER_WARD,
            GearAffix.VENOM_WARD,
            GearAffix.KINETIC_WARD
    );

    private GearDefinitions() {
    }

    public static List<GearAffix> getFixedAffixes(CultivationGearSlot slot) {
        return switch (slot) {
            case WEAPON -> List.of(GearAffix.WEAPON_EDGE);
            case OFFHAND -> List.of(GearAffix.OFFHAND_RESERVOIR, GearAffix.OFFHAND_FLOW);
            case HELMET -> List.of(GearAffix.HELMET_INSIGHT);
            case CHEST_ARMOR -> List.of(GearAffix.ARMOR_PLATING, GearAffix.ARMOR_GUARD);
            case CHEST_ROBE -> List.of(GearAffix.ROBE_RESERVOIR, GearAffix.ROBE_WARD);
            case LEGGINGS -> List.of(GearAffix.LEGGING_VITALITY, GearAffix.LEGGING_PLATING);
            case BOOTS -> List.of(GearAffix.BOOT_STEP);
            case FABAO -> List.of(GearAffix.FABAO_EMPOWERMENT);
        };
    }

    public static List<GearAffix> getRandomAffixes(CultivationGearSlot slot) {
        List<GearAffix> affixes = new ArrayList<>(COMMON_AFFIXES);
        affixes.addAll(switch (slot) {
            case WEAPON -> List.of(
                    GearAffix.WEAPON_STRENGTH,
                    GearAffix.WEAPON_MARTIAL_POWER,
                    GearAffix.WEAPON_CRIT_DAMAGE,
                    GearAffix.WEAPON_KINETIC_POWER
            );
            case OFFHAND -> List.of(
                    GearAffix.OFFHAND_SPIRIT,
                    GearAffix.OFFHAND_SPIRIT_FLOW,
                    GearAffix.OFFHAND_BLOOD_FLOW,
                    GearAffix.OFFHAND_MAGIC_WARD,
                    GearAffix.OFFHAND_FLAME_WARD,
                    GearAffix.OFFHAND_FROST_WARD
            );
            case HELMET -> List.of(
                    GearAffix.HELMET_STRENGTH,
                    GearAffix.HELMET_CRIT,
                    GearAffix.HELMET_CRIT_DAMAGE,
                    GearAffix.HELMET_MAGIC
            );
            case CHEST_ARMOR -> List.of(
                    GearAffix.ARMOR_VITALITY,
                    GearAffix.ARMOR_TOUGHNESS,
                    GearAffix.ARMOR_PHYSICAL_WARD,
                    GearAffix.ARMOR_MELEE_WARD
            );
            case CHEST_ROBE -> List.of(
                    GearAffix.ROBE_VITALITY,
                    GearAffix.ROBE_SPIRIT,
                    GearAffix.ROBE_MAGIC_WARD,
                    GearAffix.ROBE_ELEMENTAL_WARD,
                    GearAffix.ROBE_FROST_WARD,
                    GearAffix.ROBE_THUNDER_WARD,
                    GearAffix.ROBE_MAGIC_POWER
            );
            case LEGGINGS -> List.of(
                    GearAffix.LEGGING_VIGOR,
                    GearAffix.LEGGING_TOUGHNESS,
                    GearAffix.LEGGING_PHYSICAL_WARD,
                    GearAffix.LEGGING_VENOM_WARD
            );
            case BOOTS -> List.of(
                    GearAffix.BOOT_HASTE,
                    GearAffix.BOOT_SWIFTNESS,
                    GearAffix.BOOT_SPIRIT_FLOW,
                    GearAffix.BOOT_BLOOD_FLOW,
                    GearAffix.BOOT_KINETIC_WARD
            );
            case FABAO -> List.of(
                    GearAffix.FABAO_SPIRIT,
                    GearAffix.FABAO_FLOW,
                    GearAffix.FABAO_MAGIC_POWER,
                    GearAffix.FABAO_MARTIAL_POWER,
                    GearAffix.FABAO_CRIT
            );
        });
        return affixes;
    }
}
