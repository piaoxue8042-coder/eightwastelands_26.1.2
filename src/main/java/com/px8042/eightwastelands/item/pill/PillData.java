package com.px8042.eightwastelands.item.pill;

import com.px8042.eightwastelands.cultivation.CultivationRealm;

public final class PillData {

    private PillData() {
    }

    public static int cultivationQiGain(CultivationRealm realm, PillGrade grade) {
        return switch (realm) {
            case QI_REFINING -> switch (grade) {
                case LOW -> 5;
                case MIDDLE -> 10;
                case HIGH -> 20;
                case IMMORTAL -> 50;
                case NONE -> 0;
            };
            case FOUNDATION_BUILDING -> switch (grade) {
                case LOW -> 15;
                case MIDDLE -> 30;
                case HIGH -> 60;
                case IMMORTAL -> 150;
                case NONE -> 0;
            };
            default -> 0;
        };
    }

    public static double breakthroughChanceBonus(PillGrade grade) {
        return switch (grade) {
            case LOW -> 0.10D;
            case MIDDLE -> 0.20D;
            case HIGH -> 0.35D;
            case IMMORTAL -> 0.60D;
            case NONE -> 0.0D;
        };
    }

    public static long breakthroughCooldownReductionTicks(PillGrade grade) {
        return switch (grade) {
            case LOW -> minutes(2);
            case MIDDLE -> minutes(4);
            case HIGH -> minutes(8);
            case IMMORTAL -> minutes(15);
            case NONE -> 0L;
        };
    }

    public static int cookingTime(CultivationRealm realm) {
        return switch (realm) {
            case QI_REFINING -> seconds(10);
            case FOUNDATION_BUILDING -> seconds(15);
            default -> seconds(20);
        };
    }

    private static int seconds(int seconds) {
        return seconds * 20;
    }

    private static long minutes(int minutes) {
        return minutes * 60L * 20L;
    }
}
