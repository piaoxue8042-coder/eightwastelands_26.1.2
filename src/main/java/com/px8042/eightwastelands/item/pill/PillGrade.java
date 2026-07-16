package com.px8042.eightwastelands.item.pill;

import net.minecraft.network.chat.Component;

import java.util.Optional;

public enum PillGrade {
    NONE("none"),
    LOW("low"),
    MIDDLE("middle"),
    HIGH("high"),
    IMMORTAL("immortal");

    private static final PillGrade[] VALUES = values();
    private static final PillGrade[] GRADED_VALUES = {
            LOW,
            MIDDLE,
            HIGH,
            IMMORTAL
    };

    private final String serializedName;

    PillGrade(String serializedName) {
        this.serializedName = serializedName;
    }

    public String getSerializedName() {
        return this.serializedName;
    }

    public Component getDisplayName() {
        return Component.translatable("pill_grade.eightwastelands." + this.serializedName);
    }

    public boolean isGraded() {
        return this != NONE;
    }

    public static PillGrade[] gradedValues() {
        return GRADED_VALUES.clone();
    }

    public static Optional<PillGrade> bySerializedName(String name) {
        for (PillGrade grade : VALUES) {
            if (grade.serializedName.equals(name) || grade.name().equalsIgnoreCase(name)) {
                return Optional.of(grade);
            }
        }

        return Optional.empty();
    }
}
