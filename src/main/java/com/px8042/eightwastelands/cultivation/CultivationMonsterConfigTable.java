package com.px8042.eightwastelands.cultivation;

import net.minecraft.util.RandomSource;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class CultivationMonsterConfigTable {

    private static final MonsterAttributes DEFAULT_ATTRIBUTES = new MonsterAttributes(1.0D, 1.0D, 0.0D);
    private static final Map<CultivationStage, List<SpawnEntry>> SPAWN_BY_PLAYER_STAGE =
            new EnumMap<>(CultivationStage.class);
    private static final Map<CultivationStage, MonsterAttributes> ATTRIBUTES_BY_MONSTER_STAGE =
            new EnumMap<>(CultivationStage.class);

    static {
        registerSpawn(CultivationStage.MORTAL,
                spawn(CultivationStage.MORTAL, 1.00D));

        registerSpawn(CultivationStage.QI_REFINING_EARLY,
                spawn(CultivationStage.QI_REFINING_EARLY, 0.80D),
                spawn(CultivationStage.MORTAL, 0.20D));
        registerSpawn(CultivationStage.QI_REFINING_MIDDLE,
                spawn(CultivationStage.QI_REFINING_EARLY, 0.80D),
                spawn(CultivationStage.MORTAL, 0.20D));
        registerSpawn(CultivationStage.QI_REFINING_LATE,
                spawn(CultivationStage.FOUNDATION_BUILDING_EARLY, 0.05D),
                spawn(CultivationStage.QI_REFINING_EARLY, 0.80D),
                spawn(CultivationStage.MORTAL, 0.15D));

        registerSpawn(CultivationStage.FOUNDATION_BUILDING_EARLY,
                spawn(CultivationStage.FOUNDATION_BUILDING_EARLY, 0.75D),
                spawn(CultivationStage.QI_REFINING_EARLY, 0.20D),
                spawn(CultivationStage.MORTAL, 0.05D));
        registerSpawn(CultivationStage.FOUNDATION_BUILDING_MIDDLE,
                spawn(CultivationStage.FOUNDATION_BUILDING_EARLY, 0.75D),
                spawn(CultivationStage.QI_REFINING_EARLY, 0.20D),
                spawn(CultivationStage.MORTAL, 0.05D));
        registerSpawn(CultivationStage.FOUNDATION_BUILDING_LATE,
                spawn(CultivationStage.FOUNDATION_BUILDING_EARLY, 0.75D),
                spawn(CultivationStage.QI_REFINING_EARLY, 0.20D),
                spawn(CultivationStage.MORTAL, 0.05D));

        registerRealmAttributes(CultivationRealm.QI_REFINING, attributes(1.50D, 1.25D, 2.0D));
        registerRealmAttributes(CultivationRealm.FOUNDATION_BUILDING, attributes(2.50D, 1.75D, 6.0D));
    }

    private CultivationMonsterConfigTable() {
    }

    public static CultivationStage rollMonsterStage(CultivationStage playerStage, RandomSource random) {
        List<SpawnEntry> entries = SPAWN_BY_PLAYER_STAGE.getOrDefault(playerStage, List.of());
        if (entries.isEmpty()) {
            return CultivationStage.MORTAL;
        }

        double roll = random.nextDouble();
        double accumulatedChance = 0.0D;
        for (SpawnEntry entry : entries) {
            accumulatedChance += entry.chance;
            if (roll < accumulatedChance) {
                return entry.stage;
            }
        }

        return entries.getLast().stage;
    }

    public static MonsterAttributes getAttributes(CultivationStage stage) {
        return ATTRIBUTES_BY_MONSTER_STAGE.getOrDefault(stage, DEFAULT_ATTRIBUTES);
    }

    private static void registerSpawn(CultivationStage playerStage, SpawnEntry... entries) {
        SPAWN_BY_PLAYER_STAGE.put(playerStage, List.of(entries));
    }

    private static SpawnEntry spawn(CultivationStage stage, double chance) {
        return new SpawnEntry(stage, chance);
    }

    private static void registerRealmAttributes(CultivationRealm realm, MonsterAttributes attributes) {
        for (CultivationStage stage : CultivationStage.values()) {
            if (stage.isInRealm(realm)) {
                ATTRIBUTES_BY_MONSTER_STAGE.put(stage, attributes);
            }
        }
    }

    private static MonsterAttributes attributes(
            double maxHealthMultiplier,
            double attackDamageMultiplier,
            double armorBonus
    ) {
        return new MonsterAttributes(maxHealthMultiplier, attackDamageMultiplier, armorBonus);
    }

    private record SpawnEntry(CultivationStage stage, double chance) {
    }

    public record MonsterAttributes(
            double maxHealthMultiplier,
            double attackDamageMultiplier,
            double armorBonus
    ) {
        public boolean isDefault() {
            return this.maxHealthMultiplier == 1.0D
                    && this.attackDamageMultiplier == 1.0D
                    && this.armorBonus == 0.0D;
        }
    }
}
