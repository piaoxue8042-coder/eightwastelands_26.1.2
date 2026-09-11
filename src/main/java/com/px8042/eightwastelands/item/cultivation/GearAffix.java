package com.px8042.eightwastelands.item.cultivation;

import net.minecraft.network.chat.Component;

import java.util.Locale;

/**
 * One named affix and its unscaled base value.
 */
public enum GearAffix {
    VITALITY("vitality", GearStat.MAX_HEALTH, 4.0D),
    SPIRIT_RESERVOIR("spirit_reservoir", GearStat.MAX_ZHENYUAN, 25.0D),
    SPIRIT_FLOW("spirit_flow", GearStat.ZHENYUAN_REGEN, 0.25D),
    BLOOD_FLOW("blood_flow", GearStat.HEALTH_REGEN, 0.15D),
    STRENGTH("strength", GearStat.ATTACK_DAMAGE, 1.0D),
    HASTE("haste", GearStat.ATTACK_SPEED, 0.05D),
    SWIFTNESS("swiftness", GearStat.MOVEMENT_SPEED, 0.003D),
    INSIGHT("insight", GearStat.CRIT_CHANCE, 0.02D),
    DEADLY_EDGE("deadly_edge", GearStat.CRIT_DAMAGE, 0.05D),
    MARTIAL_POWER("martial_power", GearStat.MELEE_DAMAGE, 0.03D),
    RANGED_POWER("ranged_power", GearStat.PROJECTILE_DAMAGE, 0.03D),
    FLAME_POWER("flame_power", GearStat.FIRE_DAMAGE, 0.03D),
    FROST_POWER("frost_power", GearStat.COLD_DAMAGE, 0.03D),
    THUNDER_POWER("thunder_power", GearStat.LIGHTNING_DAMAGE, 0.03D),
    ARCANE_POWER("arcane_power", GearStat.MAGIC_DAMAGE, 0.03D),
    WITHER_POWER("wither_power", GearStat.WITHER_DAMAGE, 0.03D),
    VENOM_POWER("venom_power", GearStat.POISON_DAMAGE, 0.03D),
    KINETIC_POWER("kinetic_power", GearStat.KINETIC_DAMAGE, 0.03D),
    PLATING("plating", GearStat.ARMOR, 1.0D),
    TOUGHNESS("toughness", GearStat.ARMOR_TOUGHNESS, 0.5D),
    PHYSICAL_WARD("physical_ward", GearStat.PHYSICAL_RESISTANCE, 1.0D),
    MELEE_WARD("melee_ward", GearStat.MELEE_RESISTANCE, 1.0D),
    PROJECTILE_WARD("projectile_ward", GearStat.PROJECTILE_RESISTANCE, 1.0D),
    FLAME_WARD("flame_ward", GearStat.FIRE_RESISTANCE, 1.0D),
    FROST_WARD("frost_ward", GearStat.COLD_RESISTANCE, 1.0D),
    THUNDER_WARD("thunder_ward", GearStat.LIGHTNING_RESISTANCE, 1.0D),
    ARCANE_WARD("arcane_ward", GearStat.MAGIC_RESISTANCE, 1.0D),
    WITHER_WARD("wither_ward", GearStat.WITHER_RESISTANCE, 1.0D),
    VENOM_WARD("venom_ward", GearStat.POISON_RESISTANCE, 1.0D),
    KINETIC_WARD("kinetic_ward", GearStat.KINETIC_RESISTANCE, 1.0D),

    WEAPON_EDGE("weapon_edge", GearStat.ATTACK_DAMAGE, 1.0D),
    HELMET_INSIGHT("helmet_insight", GearStat.CRIT_CHANCE, 0.01D),
    ARMOR_PLATING("armor_plating", GearStat.ARMOR, 2.0D),
    ARMOR_GUARD("armor_guard", GearStat.PHYSICAL_RESISTANCE, 1.0D),
    ROBE_RESERVOIR("robe_reservoir", GearStat.MAX_ZHENYUAN, 25.0D),
    ROBE_WARD("robe_ward", GearStat.MAGIC_RESISTANCE, 1.0D),
    LEGGING_VITALITY("legging_vitality", GearStat.MAX_HEALTH, 4.0D),
    LEGGING_PLATING("legging_plating", GearStat.ARMOR, 1.0D),
    BOOT_STEP("boot_step", GearStat.MOVEMENT_SPEED, 0.003D),
    OFFHAND_RESERVOIR("offhand_reservoir", GearStat.MAX_ZHENYUAN, 25.0D),
    OFFHAND_FLOW("offhand_flow", GearStat.ZHENYUAN_REGEN, 0.25D),
    FABAO_EMPOWERMENT("fabao_empowerment", GearStat.MAGIC_DAMAGE, 0.03D),

    HELMET_STRENGTH("helmet_strength", GearStat.ATTACK_DAMAGE, 2.0D),
    HELMET_CRIT("helmet_crit", GearStat.CRIT_CHANCE, 0.03D),
    HELMET_CRIT_DAMAGE("helmet_crit_damage", GearStat.CRIT_DAMAGE, 0.10D),
    HELMET_MAGIC("helmet_magic", GearStat.MAGIC_DAMAGE, 0.08D),
    ARMOR_VITALITY("armor_vitality", GearStat.MAX_HEALTH, 8.0D),
    ARMOR_TOUGHNESS("armor_toughness", GearStat.ARMOR_TOUGHNESS, 1.0D),
    ARMOR_PHYSICAL_WARD("armor_physical_ward", GearStat.PHYSICAL_RESISTANCE, 2.0D),
    ARMOR_MELEE_WARD("armor_melee_ward", GearStat.MELEE_RESISTANCE, 2.0D),
    ROBE_VITALITY("robe_vitality", GearStat.MAX_HEALTH, 4.0D),
    ROBE_SPIRIT("robe_spirit", GearStat.MAX_ZHENYUAN, 50.0D),
    ROBE_MAGIC_WARD("robe_magic_ward", GearStat.MAGIC_RESISTANCE, 3.0D),
    ROBE_ELEMENTAL_WARD("robe_elemental_ward", GearStat.FIRE_RESISTANCE, 2.0D),
    ROBE_FROST_WARD("robe_frost_ward", GearStat.COLD_RESISTANCE, 2.0D),
    ROBE_THUNDER_WARD("robe_thunder_ward", GearStat.LIGHTNING_RESISTANCE, 2.0D),
    ROBE_MAGIC_POWER("robe_magic_power", GearStat.MAGIC_DAMAGE, 0.08D),
    LEGGING_VIGOR("legging_vigor", GearStat.MAX_HEALTH, 8.0D),
    LEGGING_TOUGHNESS("legging_toughness", GearStat.ARMOR_TOUGHNESS, 1.0D),
    LEGGING_PHYSICAL_WARD("legging_physical_ward", GearStat.PHYSICAL_RESISTANCE, 2.0D),
    LEGGING_VENOM_WARD("legging_venom_ward", GearStat.POISON_RESISTANCE, 2.0D),
    BOOT_HASTE("boot_haste", GearStat.ATTACK_SPEED, 0.10D),
    BOOT_SWIFTNESS("boot_swiftness", GearStat.MOVEMENT_SPEED, 0.01D),
    BOOT_SPIRIT_FLOW("boot_spirit_flow", GearStat.ZHENYUAN_REGEN, 0.50D),
    BOOT_BLOOD_FLOW("boot_blood_flow", GearStat.HEALTH_REGEN, 0.35D),
    BOOT_KINETIC_WARD("boot_kinetic_ward", GearStat.KINETIC_RESISTANCE, 2.0D),
    OFFHAND_SPIRIT("offhand_spirit", GearStat.MAX_ZHENYUAN, 50.0D),
    OFFHAND_SPIRIT_FLOW("offhand_spirit_flow", GearStat.ZHENYUAN_REGEN, 0.50D),
    OFFHAND_BLOOD_FLOW("offhand_blood_flow", GearStat.HEALTH_REGEN, 0.30D),
    OFFHAND_MAGIC_WARD("offhand_magic_ward", GearStat.MAGIC_RESISTANCE, 2.0D),
    OFFHAND_FLAME_WARD("offhand_flame_ward", GearStat.FIRE_RESISTANCE, 2.0D),
    OFFHAND_FROST_WARD("offhand_frost_ward", GearStat.COLD_RESISTANCE, 2.0D),
    WEAPON_STRENGTH("weapon_strength", GearStat.ATTACK_DAMAGE, 2.0D),
    WEAPON_MARTIAL_POWER("weapon_martial_power", GearStat.MELEE_DAMAGE, 0.10D),
    WEAPON_CRIT_DAMAGE("weapon_crit_damage", GearStat.CRIT_DAMAGE, 0.12D),
    WEAPON_KINETIC_POWER("weapon_kinetic_power", GearStat.KINETIC_DAMAGE, 0.08D),
    FABAO_SPIRIT("fabao_spirit", GearStat.MAX_ZHENYUAN, 40.0D),
    FABAO_FLOW("fabao_flow", GearStat.ZHENYUAN_REGEN, 0.25D),
    FABAO_MAGIC_POWER("fabao_magic_power", GearStat.MAGIC_DAMAGE, 0.10D),
    FABAO_MARTIAL_POWER("fabao_martial_power", GearStat.MELEE_DAMAGE, 0.05D),
    FABAO_CRIT("fabao_crit", GearStat.CRIT_CHANCE, 0.02D);

    private final String serializedName;
    private final GearStat stat;
    private final double baseAmount;

    GearAffix(String serializedName, GearStat stat, double baseAmount) {
        this.serializedName = serializedName;
        this.stat = stat;
        this.baseAmount = baseAmount;
    }

    public String getSerializedName() {
        return this.serializedName;
    }

    public GearStat getStat() {
        return this.stat;
    }

    public double getBaseAmount() {
        return this.baseAmount;
    }

    public Component getDisplayComponent(double amount) {
        return Component.translatable(
                "gear_stat.eightwastelands." + this.stat.getSerializedName(),
                formatAmount(amount)
        );
    }

    private String formatAmount(double amount) {
        String sign = amount >= 0.0D ? "+" : "";
        if (this.stat.isPercent()) {
            return sign + String.format(Locale.ROOT, "%.1f%%", amount * 100.0D);
        }
        if (this.stat == GearStat.ZHENYUAN_REGEN || this.stat == GearStat.HEALTH_REGEN) {
            return sign + String.format(Locale.ROOT, "%.2f/s", amount);
        }
        if (Math.abs(amount - Math.rint(amount)) < 0.001D) {
            return sign + String.format(Locale.ROOT, "%.0f", amount);
        }
        return sign + String.format(Locale.ROOT, "%.3f", amount);
    }

    public static GearAffix bySerializedName(String serializedName) {
        for (GearAffix affix : values()) {
            if (affix.serializedName.equals(serializedName)) {
                return affix;
            }
        }
        return null;
    }
}
