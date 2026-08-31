package com.px8042.eightwastelands.alchemy;

import com.px8042.eightwastelands.cultivation.CultivationRealm;
import com.px8042.eightwastelands.item.pill.PillGrade;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;

import java.util.Optional;

public class AlchemyManager {

    public static final String ALCHEMY_REALM = "eightwastelands_alchemy_realm";
    public static final String ALCHEMY_MASTERY = "eightwastelands_alchemy_mastery";
    public static final String ALCHEMY_EXPERIENCE = "eightwastelands_alchemy_experience";

    private static final CultivationRealm DEFAULT_REALM = CultivationRealm.QI_REFINING;
    private static final AlchemyMastery DEFAULT_MASTERY = AlchemyMastery.BEGINNER;

    private static final PillGrade[] GRADE_ORDER = {
            PillGrade.LOW,
            PillGrade.MIDDLE,
            PillGrade.HIGH,
            PillGrade.IMMORTAL
    };

    private static final int[][] SAME_REALM_WEIGHTS = {
            {80, 18, 2, 0},
            {55, 35, 9, 1},
            {30, 45, 22, 3},
            {10, 40, 40, 10}
    };

    private static final int[] MASTERY_REQUIRED_EXPERIENCE = {
            100,
            200,
            400,
            800
    };

    private AlchemyManager() {
    }

    public static CultivationRealm getRealm(ServerPlayer player) {
        return getRealm(player.getPersistentData());
    }

    public static AlchemyMastery getMastery(ServerPlayer player) {
        return getMastery(player.getPersistentData());
    }

    public static int getExperience(ServerPlayer player) {
        return getExperience(player.getPersistentData());
    }

    public static int getRequiredExperience(ServerPlayer player) {
        return getRequiredExperience(getMastery(player));
    }

    public static void setRealmForCommand(ServerPlayer player, CultivationRealm realm) {
        player.getPersistentData().putInt(ALCHEMY_REALM, realm.getIndex());
        player.getPersistentData().remove(ALCHEMY_EXPERIENCE);
    }

    public static void setMasteryForCommand(ServerPlayer player, AlchemyMastery mastery) {
        player.getPersistentData().putInt(ALCHEMY_MASTERY, mastery.getIndex());
        player.getPersistentData().remove(ALCHEMY_EXPERIENCE);
    }

    public static void clearForCommand(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        data.remove(ALCHEMY_REALM);
        data.remove(ALCHEMY_MASTERY);
        data.remove(ALCHEMY_EXPERIENCE);
    }

    public static boolean canRefine(ServerPlayer player, CultivationRealm pillRealm) {
        return canRefine(getRealm(player), pillRealm);
    }

    public static boolean canRefine(CultivationRealm alchemyRealm, CultivationRealm pillRealm) {
        return alchemyRealm.getIndex() + 1 >= pillRealm.getIndex();
    }

    public static Optional<PillGrade> rollPillGrade(ServerPlayer player, CultivationRealm pillRealm) {
        return rollPillGrade(getRealm(player), getMastery(player), pillRealm, player.getRandom());
    }

    public static Optional<PillGrade> rollPillGrade(
            CultivationRealm alchemyRealm,
            AlchemyMastery mastery,
            CultivationRealm pillRealm,
            RandomSource random
    ) {
        int realmDifference = alchemyRealm.getIndex() - pillRealm.getIndex();
        if (realmDifference < -1) {
            return Optional.empty();
        }

        if (realmDifference == -1) {
            return Optional.of(PillGrade.LOW);
        }

        return Optional.of(rollWeightedGrade(mastery, random));
    }

    public static Optional<AlchemyGradeChances> getGradeChances(
            CultivationRealm alchemyRealm,
            AlchemyMastery mastery,
            CultivationRealm pillRealm
    ) {
        int realmDifference = alchemyRealm.getIndex() - pillRealm.getIndex();
        if (realmDifference < -1) {
            return Optional.empty();
        }

        if (realmDifference == -1) {
            return Optional.of(new AlchemyGradeChances(100, 0, 0, 0, true));
        }

        int[] weights = SAME_REALM_WEIGHTS[mastery.getIndex()];
        int totalWeight = 0;
        for (int weight : weights) {
            totalWeight += weight;
        }

        if (totalWeight <= 0) {
            return Optional.of(new AlchemyGradeChances(100, 0, 0, 0, false));
        }

        return Optional.of(new AlchemyGradeChances(
                toPercent(weights[0], totalWeight),
                toPercent(weights[1], totalWeight),
                toPercent(weights[2], totalWeight),
                toPercent(weights[3], totalWeight),
                false
        ));
    }

    public static AlchemyProgressResult recordPillRefined(
            ServerPlayer player,
            CultivationRealm pillRealm,
            PillGrade grade
    ) {
        return recordPillRefined(player, pillRealm, grade, 1.0D);
    }

    public static AlchemyProgressResult recordPillRefined(
            ServerPlayer player,
            CultivationRealm pillRealm,
            PillGrade grade,
            double experienceMultiplier
    ) {
        CompoundTag data = player.getPersistentData();
        CultivationRealm oldRealm = getRealm(data);
        AlchemyMastery oldMastery = getMastery(data);
        int oldExperience = getExperience(data);
        int gainedExperience = calculateExperienceGain(oldRealm, pillRealm, grade, experienceMultiplier);

        CultivationRealm newRealm = oldRealm;
        AlchemyMastery newMastery = oldMastery;
        int newExperience = oldExperience + gainedExperience;
        boolean advanced = false;

        while (newExperience >= getRequiredExperience(newMastery) && !isMaxLevel(newRealm, newMastery)) {
            newExperience -= getRequiredExperience(newMastery);
            AlchemyLevel nextLevel = advance(newRealm, newMastery);
            newRealm = nextLevel.realm();
            newMastery = nextLevel.mastery();
            advanced = true;
        }

        if (isMaxLevel(newRealm, newMastery)) {
            newExperience = Math.min(newExperience, getRequiredExperience(newMastery));
        }

        data.putInt(ALCHEMY_REALM, newRealm.getIndex());
        data.putInt(ALCHEMY_MASTERY, newMastery.getIndex());
        data.putInt(ALCHEMY_EXPERIENCE, Math.max(0, newExperience));

        return new AlchemyProgressResult(
                oldRealm,
                oldMastery,
                oldExperience,
                newRealm,
                newMastery,
                Math.max(0, newExperience),
                getRequiredExperience(newMastery),
                gainedExperience,
                advanced
        );
    }

    public static void copyData(CompoundTag oldData, CompoundTag newData) {
        copyInt(oldData, newData, ALCHEMY_REALM);
        copyInt(oldData, newData, ALCHEMY_MASTERY);
        copyInt(oldData, newData, ALCHEMY_EXPERIENCE);
    }

    private static CultivationRealm getRealm(CompoundTag data) {
        return CultivationRealm.byIndex(data.getIntOr(ALCHEMY_REALM, DEFAULT_REALM.getIndex()))
                .orElse(DEFAULT_REALM);
    }

    private static AlchemyMastery getMastery(CompoundTag data) {
        return AlchemyMastery.byIndex(data.getIntOr(ALCHEMY_MASTERY, DEFAULT_MASTERY.getIndex()))
                .orElse(DEFAULT_MASTERY);
    }

    private static int getExperience(CompoundTag data) {
        return Math.max(0, data.getIntOr(ALCHEMY_EXPERIENCE, 0));
    }

    private static int getRequiredExperience(AlchemyMastery mastery) {
        return MASTERY_REQUIRED_EXPERIENCE[mastery.getIndex()];
    }

    private static int calculateExperienceGain(
            CultivationRealm alchemyRealm,
            CultivationRealm pillRealm,
            PillGrade grade,
            double experienceMultiplier
    ) {
        int baseExperience = switch (grade) {
            case NONE -> 0;
            case LOW -> 5;
            case MIDDLE -> 12;
            case HIGH -> 30;
            case IMMORTAL -> 80;
        };

        int realmDifference = pillRealm.getIndex() - alchemyRealm.getIndex();
        int experience;
        if (realmDifference > 0) {
            experience = baseExperience * 2;
        } else if (realmDifference == 0) {
            experience = baseExperience;
        } else if (realmDifference == -1) {
            experience = Math.max(1, baseExperience / 3);
        } else {
            experience = 0;
        }

        return applyExperienceMultiplier(experience, experienceMultiplier);
    }

    private static int applyExperienceMultiplier(int experience, double multiplier) {
        if (experience <= 0 || multiplier <= 0.0D) {
            return 0;
        }

        return Math.max(1, (int) Math.round(experience * multiplier));
    }

    private static boolean isMaxLevel(CultivationRealm realm, AlchemyMastery mastery) {
        return realm == CultivationRealm.TRIBULATION && mastery == AlchemyMastery.MASTER;
    }

    private static AlchemyLevel advance(CultivationRealm realm, AlchemyMastery mastery) {
        Optional<AlchemyMastery> nextMastery = mastery.next();
        if (nextMastery.isPresent()) {
            return new AlchemyLevel(realm, nextMastery.get());
        }

        return CultivationRealm.byIndex(realm.getIndex() + 1)
                .map(nextRealm -> new AlchemyLevel(nextRealm, AlchemyMastery.BEGINNER))
                .orElseGet(() -> new AlchemyLevel(realm, mastery));
    }

    private static PillGrade rollWeightedGrade(AlchemyMastery mastery, RandomSource random) {
        int[] weights = SAME_REALM_WEIGHTS[mastery.getIndex()];
        int totalWeight = 0;
        for (int weight : weights) {
            totalWeight += weight;
        }

        int roll = random.nextInt(totalWeight);
        for (int i = 0; i < weights.length; i++) {
            roll -= weights[i];
            if (roll < 0) {
                return GRADE_ORDER[i];
            }
        }

        return PillGrade.LOW;
    }

    private static int toPercent(int weight, int totalWeight) {
        return (int) Math.round(weight * 100.0D / totalWeight);
    }

    private static void copyInt(CompoundTag oldData, CompoundTag newData, String key) {
        if (oldData.contains(key)) {
            newData.putInt(key, oldData.getIntOr(key, 0));
        }
    }

    private record AlchemyLevel(CultivationRealm realm, AlchemyMastery mastery) {
    }

    public record AlchemyProgressResult(
            CultivationRealm oldRealm,
            AlchemyMastery oldMastery,
            int oldExperience,
            CultivationRealm newRealm,
            AlchemyMastery newMastery,
            int newExperience,
            int requiredExperience,
            int gainedExperience,
            boolean advanced
    ) {
    }

    public record AlchemyGradeChances(
            int low,
            int middle,
            int high,
            int immortal,
            boolean forcedLowGrade
    ) {
        public int get(PillGrade grade) {
            return switch (grade) {
                case LOW -> this.low;
                case MIDDLE -> this.middle;
                case HIGH -> this.high;
                case IMMORTAL -> this.immortal;
                case NONE -> 0;
            };
        }
    }
}
