package com.px8042.eightwastelands.cultivation;

import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;

public enum CultivationStage {
    MORTAL(0, "mortal", null, 0, attributes(), 0.0D, 0.0D, 0.0D, 0L),
    QI_REFINING_EARLY(
            1,
            "qi_refining_early",
            CultivationRealm.QI_REFINING,
            2000,
            attributes(
                    bonus(CultivationAttribute.MAX_HEALTH, 12.0D),
                    bonus(CultivationAttribute.ATTACK_DAMAGE, 1.0D),
                    bonus(CultivationAttribute.MOVEMENT_SPEED, 0.01D)
            ),
            100.0D,
            1.0D,
            0.05D,
            minutes(10)
    ),
    QI_REFINING_MIDDLE(
            2,
            "qi_refining_middle",
            CultivationRealm.QI_REFINING,
            5000,
            attributes(
                    bonus(CultivationAttribute.MAX_HEALTH, 36.0D),
                    bonus(CultivationAttribute.ATTACK_DAMAGE, 3.0D),
                    bonus(CultivationAttribute.MOVEMENT_SPEED, 0.01D)
            ),
            250.0D,
            1.5D,
            0.05D,
            minutes(10)
    ),
    QI_REFINING_LATE(
            3,
            "qi_refining_late",
            CultivationRealm.QI_REFINING,
            10000,
            attributes(
                    bonus(CultivationAttribute.MAX_HEALTH, 60.0D),
                    bonus(CultivationAttribute.ATTACK_DAMAGE, 5.0D),
                    bonus(CultivationAttribute.MOVEMENT_SPEED, 0.01D)
            ),
            500.0D,
            2.0D,
            0.05D,
            minutes(10)
    ),
    FOUNDATION_BUILDING_EARLY(
            4,
            "foundation_building_early",
            CultivationRealm.FOUNDATION_BUILDING,
            7000,
            attributes(
                    bonus(CultivationAttribute.MAX_HEALTH, 90.0D),
                    bonus(CultivationAttribute.ATTACK_DAMAGE, 8.0D),
                    bonus(CultivationAttribute.MOVEMENT_SPEED, 0.015D),
                    bonus(CultivationAttribute.CRIT_CHANCE, 0.05D),
                    bonus(CultivationAttribute.CRIT_DAMAGE_ADDITIVE, 0.05D),
                    bonus(CultivationAttribute.VULNERABLE_DAMAGE_ADDITIVE, 0.05D),
                    bonus(CultivationAttribute.MELEE_DAMAGE_ADDITIVE, 0.05D),
                    bonus(CultivationAttribute.PROJECTILE_DAMAGE_ADDITIVE, 0.05D),
                    bonus(CultivationAttribute.RESISTANCE_PHYSICAL, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_MELEE, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_PROJECTILE, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_FIRE, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_COLD, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_LIGHTNING, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_MAGIC, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_POISON, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_WITHER, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_KINETIC, 2.0D),
                    bonus(CultivationAttribute.HEALING_RECEIVED, 0.05D)
            ),
            600.0D,
            3.0D,
            0.05D,
            minutes(20)
    ),
    FOUNDATION_BUILDING_MIDDLE(
            5,
            "foundation_building_middle",
            CultivationRealm.FOUNDATION_BUILDING,
            12000,
            attributes(
                    bonus(CultivationAttribute.MAX_HEALTH, 120.0D),
                    bonus(CultivationAttribute.ATTACK_DAMAGE, 10.0D),
                    bonus(CultivationAttribute.MOVEMENT_SPEED, 0.015D),
                    bonus(CultivationAttribute.CRIT_CHANCE, 0.05D),
                    bonus(CultivationAttribute.CRIT_DAMAGE_ADDITIVE, 0.05D),
                    bonus(CultivationAttribute.VULNERABLE_DAMAGE_ADDITIVE, 0.05D),
                    bonus(CultivationAttribute.MELEE_DAMAGE_ADDITIVE, 0.05D),
                    bonus(CultivationAttribute.PROJECTILE_DAMAGE_ADDITIVE, 0.05D),
                    bonus(CultivationAttribute.RESISTANCE_PHYSICAL, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_MELEE, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_PROJECTILE, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_FIRE, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_COLD, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_LIGHTNING, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_MAGIC, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_POISON, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_WITHER, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_KINETIC, 2.0D),
                    bonus(CultivationAttribute.HEALING_RECEIVED, 0.05D)
            ),
            800.0D,
            4.0D,
            0.05D,
            minutes(20)
    ),
    FOUNDATION_BUILDING_LATE(
            6,
            "foundation_building_late",
            CultivationRealm.FOUNDATION_BUILDING,
            25000,
            attributes(
                    bonus(CultivationAttribute.MAX_HEALTH, 200.0D),
                    bonus(CultivationAttribute.ATTACK_DAMAGE, 15.0D),
                    bonus(CultivationAttribute.MOVEMENT_SPEED, 0.015D),
                    bonus(CultivationAttribute.CRIT_CHANCE, 0.05D),
                    bonus(CultivationAttribute.CRIT_DAMAGE_ADDITIVE, 0.05D),
                    bonus(CultivationAttribute.VULNERABLE_DAMAGE_ADDITIVE, 0.05D),
                    bonus(CultivationAttribute.MELEE_DAMAGE_ADDITIVE, 0.05D),
                    bonus(CultivationAttribute.PROJECTILE_DAMAGE_ADDITIVE, 0.05D),
                    bonus(CultivationAttribute.RESISTANCE_PHYSICAL, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_MELEE, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_PROJECTILE, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_FIRE, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_COLD, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_LIGHTNING, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_MAGIC, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_POISON, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_WITHER, 2.0D),
                    bonus(CultivationAttribute.RESISTANCE_KINETIC, 2.0D),
                    bonus(CultivationAttribute.HEALING_RECEIVED, 0.10D)
            ),
            1000.0D,
            5.0D,
            0.05D,
            minutes(20)
    );

    private static final CultivationStage[] BY_INDEX = values();

    private final int index;
    private final String translationName;
    private final CultivationRealm realm;
    private final int requiredQi;
    private final List<CultivationAttributeBonus> attributeBonuses;
    private final double maxZhenyuan;
    private final double zhenyuanRegenPerSecond;
    private final double baseBreakthroughChance;
    private final long breakthroughFailureCooldownTicks;

    CultivationStage(
            int index,
            String translationName,
            CultivationRealm realm,
            int requiredQi,
            List<CultivationAttributeBonus> attributeBonuses,
            double maxZhenyuan,
            double zhenyuanRegenPerSecond,
            double baseBreakthroughChance,
            long breakthroughFailureCooldownTicks
    ) {
        this.index = index;
        this.translationName = translationName;
        this.realm = realm;
        this.requiredQi = requiredQi;
        this.attributeBonuses = attributeBonuses;
        this.maxZhenyuan = maxZhenyuan;
        this.zhenyuanRegenPerSecond = zhenyuanRegenPerSecond;
        this.baseBreakthroughChance = baseBreakthroughChance;
        this.breakthroughFailureCooldownTicks = breakthroughFailureCooldownTicks;
    }

    public int getIndex() {
        return this.index;
    }

    public int getRequiredQi() {
        return this.requiredQi;
    }

    public String getSerializedName() {
        return this.translationName;
    }

    public Optional<CultivationRealm> getRealm() {
        return Optional.ofNullable(this.realm);
    }

    public boolean isInRealm(CultivationRealm realm) {
        return this.realm == realm;
    }

    public List<CultivationAttributeBonus> getAttributeBonuses() {
        return this.attributeBonuses;
    }

    public double getMaxZhenyuan() {
        return this.maxZhenyuan;
    }

    public double getZhenyuanRegenPerSecond() {
        return this.zhenyuanRegenPerSecond;
    }

    public double getBaseBreakthroughChance() {
        return this.baseBreakthroughChance;
    }

    public long getBreakthroughFailureCooldownTicks() {
        return this.breakthroughFailureCooldownTicks;
    }

    public boolean isMortal() {
        return this == MORTAL;
    }

    public boolean isMaxStage() {
        return this.index >= BY_INDEX.length - 1;
    }

    public CultivationStage next() {
        if (isMaxStage()) {
            return this;
        }

        return BY_INDEX[this.index + 1];
    }

    public Component getDisplayName() {
        return Component.translatable("cultivation.eightwastelands.stage." + this.translationName);
    }

    public static CultivationStage byIndex(int index) {
        if (index < 0 || index >= BY_INDEX.length) {
            return MORTAL;
        }

        return BY_INDEX[index];
    }

    public static Optional<CultivationStage> bySerializedName(String name) {
        for (CultivationStage stage : BY_INDEX) {
            if (stage.translationName.equals(name) || stage.name().equalsIgnoreCase(name)) {
                return Optional.of(stage);
            }
        }

        return Optional.empty();
    }

    private static long minutes(int minutes) {
        return minutes * 60L * 20L;
    }

    private static List<CultivationAttributeBonus> attributes(CultivationAttributeBonus... bonuses) {
        return List.of(bonuses);
    }

    private static CultivationAttributeBonus bonus(CultivationAttribute attribute, double amount) {
        return new CultivationAttributeBonus(attribute, amount);
    }
}
