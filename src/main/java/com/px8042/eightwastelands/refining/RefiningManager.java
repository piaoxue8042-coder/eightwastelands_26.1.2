package com.px8042.eightwastelands.refining;

import com.px8042.eightwastelands.cultivation.CultivationRealm;
import com.px8042.eightwastelands.item.cultivation.CultivationGearGrade;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;

import java.util.Optional;

/**
 * Tracks each player's independent refining realm, mastery, and experience.
 */
public final class RefiningManager {

    public static final String REFINING_REALM = "eightwastelands_refining_realm";
    public static final String REFINING_MASTERY = "eightwastelands_refining_mastery";
    public static final String REFINING_EXPERIENCE = "eightwastelands_refining_experience";

    private static final CultivationRealm DEFAULT_REALM = CultivationRealm.QI_REFINING;
    private static final RefiningMastery DEFAULT_MASTERY = RefiningMastery.BEGINNER;
    private static final CultivationGearGrade[] GRADE_ORDER = {
            CultivationGearGrade.LOW,
            CultivationGearGrade.MIDDLE,
            CultivationGearGrade.HIGH,
            CultivationGearGrade.IMMORTAL
    };
    private static final int[][] SAME_REALM_WEIGHTS = {
            {80, 18, 2, 0},
            {55, 35, 9, 1},
            {30, 45, 22, 3},
            {10, 40, 40, 10}
    };
    private static final int[] MASTERY_REQUIRED_EXPERIENCE = {100, 200, 400, 800};

    private RefiningManager() {
    }

    public static CultivationRealm getRealm(ServerPlayer player) {
        return getRealm(player.getPersistentData());
    }

    public static RefiningMastery getMastery(ServerPlayer player) {
        return getMastery(player.getPersistentData());
    }

    public static int getExperience(ServerPlayer player) {
        return Math.max(0, player.getPersistentData().getIntOr(REFINING_EXPERIENCE, 0));
    }

    public static int getRequiredExperience(ServerPlayer player) {
        return MASTERY_REQUIRED_EXPERIENCE[getMastery(player).getIndex()];
    }

    public static boolean canRefine(ServerPlayer player, CultivationRealm gearRealm) {
        return getRealm(player).getIndex() + 1 >= gearRealm.getIndex();
    }

    public static Optional<CultivationGearGrade> rollGearGrade(ServerPlayer player, CultivationRealm gearRealm) {
        int realmDifference = getRealm(player).getIndex() - gearRealm.getIndex();
        if (realmDifference < -1) {
            return Optional.empty();
        }
        if (realmDifference == -1) {
            return Optional.of(CultivationGearGrade.LOW);
        }
        return Optional.of(rollWeightedGrade(getMastery(player), player.getRandom()));
    }

    public static RefiningProgressResult recordGearRefined(
            ServerPlayer player,
            CultivationRealm gearRealm,
            CultivationGearGrade grade
    ) {
        CompoundTag data = player.getPersistentData();
        CultivationRealm oldRealm = getRealm(data);
        RefiningMastery oldMastery = getMastery(data);
        int oldExperience = getExperience(player);
        int gainedExperience = calculateExperienceGain(oldRealm, gearRealm, grade);

        CultivationRealm newRealm = oldRealm;
        RefiningMastery newMastery = oldMastery;
        int newExperience = oldExperience + gainedExperience;
        boolean advanced = false;

        while (newExperience >= MASTERY_REQUIRED_EXPERIENCE[newMastery.getIndex()]
                && !isMaxLevel(newRealm, newMastery)) {
            newExperience -= MASTERY_REQUIRED_EXPERIENCE[newMastery.getIndex()];
            RefiningLevel nextLevel = advance(newRealm, newMastery);
            newRealm = nextLevel.realm();
            newMastery = nextLevel.mastery();
            advanced = true;
        }

        if (isMaxLevel(newRealm, newMastery)) {
            newExperience = Math.min(newExperience, MASTERY_REQUIRED_EXPERIENCE[newMastery.getIndex()]);
        }

        data.putInt(REFINING_REALM, newRealm.getIndex());
        data.putInt(REFINING_MASTERY, newMastery.getIndex());
        data.putInt(REFINING_EXPERIENCE, Math.max(0, newExperience));

        return new RefiningProgressResult(
                newRealm,
                newMastery,
                Math.max(0, newExperience),
                MASTERY_REQUIRED_EXPERIENCE[newMastery.getIndex()],
                gainedExperience,
                advanced
        );
    }

    public static void setRealmForCommand(ServerPlayer player, CultivationRealm realm) {
        player.getPersistentData().putInt(REFINING_REALM, realm.getIndex());
        player.getPersistentData().remove(REFINING_EXPERIENCE);
    }

    public static void setMasteryForCommand(ServerPlayer player, RefiningMastery mastery) {
        player.getPersistentData().putInt(REFINING_MASTERY, mastery.getIndex());
        player.getPersistentData().remove(REFINING_EXPERIENCE);
    }

    public static void clearForCommand(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        data.remove(REFINING_REALM);
        data.remove(REFINING_MASTERY);
        data.remove(REFINING_EXPERIENCE);
    }

    public static void copyData(CompoundTag oldData, CompoundTag newData) {
        copyInt(oldData, newData, REFINING_REALM);
        copyInt(oldData, newData, REFINING_MASTERY);
        copyInt(oldData, newData, REFINING_EXPERIENCE);
    }

    private static CultivationRealm getRealm(CompoundTag data) {
        return CultivationRealm.byIndex(data.getIntOr(REFINING_REALM, DEFAULT_REALM.getIndex()))
                .orElse(DEFAULT_REALM);
    }

    private static RefiningMastery getMastery(CompoundTag data) {
        return RefiningMastery.byIndex(data.getIntOr(REFINING_MASTERY, DEFAULT_MASTERY.getIndex()))
                .orElse(DEFAULT_MASTERY);
    }

    private static int calculateExperienceGain(
            CultivationRealm refiningRealm,
            CultivationRealm gearRealm,
            CultivationGearGrade grade
    ) {
        int baseExperience = switch (grade) {
            case LOW -> 5;
            case MIDDLE -> 12;
            case HIGH -> 30;
            case IMMORTAL -> 80;
        };
        int realmDifference = gearRealm.getIndex() - refiningRealm.getIndex();
        if (realmDifference > 0) {
            return baseExperience * 2;
        }
        if (realmDifference == 0) {
            return baseExperience;
        }
        if (realmDifference == -1) {
            return Math.max(1, baseExperience / 3);
        }
        return 0;
    }

    private static boolean isMaxLevel(CultivationRealm realm, RefiningMastery mastery) {
        return realm == CultivationRealm.TRIBULATION && mastery == RefiningMastery.MASTER;
    }

    private static RefiningLevel advance(CultivationRealm realm, RefiningMastery mastery) {
        Optional<RefiningMastery> nextMastery = mastery.next();
        if (nextMastery.isPresent()) {
            return new RefiningLevel(realm, nextMastery.get());
        }
        return CultivationRealm.byIndex(realm.getIndex() + 1)
                .map(nextRealm -> new RefiningLevel(nextRealm, RefiningMastery.BEGINNER))
                .orElseGet(() -> new RefiningLevel(realm, mastery));
    }

    private static CultivationGearGrade rollWeightedGrade(RefiningMastery mastery, RandomSource random) {
        int[] weights = SAME_REALM_WEIGHTS[mastery.getIndex()];
        int totalWeight = 0;
        for (int weight : weights) {
            totalWeight += weight;
        }

        int roll = random.nextInt(totalWeight);
        for (int index = 0; index < weights.length; index++) {
            roll -= weights[index];
            if (roll < 0) {
                return GRADE_ORDER[index];
            }
        }
        return CultivationGearGrade.LOW;
    }

    private static void copyInt(CompoundTag oldData, CompoundTag newData, String key) {
        if (oldData.contains(key)) {
            newData.putInt(key, oldData.getIntOr(key, 0));
        }
    }

    private record RefiningLevel(CultivationRealm realm, RefiningMastery mastery) {
    }

    public record RefiningProgressResult(
            CultivationRealm newRealm,
            RefiningMastery newMastery,
            int newExperience,
            int requiredExperience,
            int gainedExperience,
            boolean advanced
    ) {
    }
}
