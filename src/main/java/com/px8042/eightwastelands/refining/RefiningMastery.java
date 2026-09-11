package com.px8042.eightwastelands.refining;

import net.minecraft.network.chat.Component;

import java.util.Optional;

public enum RefiningMastery {
    BEGINNER(0, "beginner"),
    SKILLED(1, "skilled"),
    PROFICIENT(2, "proficient"),
    MASTER(3, "master");

    private static final RefiningMastery[] VALUES = values();

    private final int index;
    private final String serializedName;

    RefiningMastery(int index, String serializedName) {
        this.index = index;
        this.serializedName = serializedName;
    }

    public int getIndex() {
        return this.index;
    }

    public String getSerializedName() {
        return this.serializedName;
    }

    public Component getDisplayName() {
        return Component.translatable("refining.eightwastelands.mastery." + this.serializedName);
    }

    public Optional<RefiningMastery> next() {
        return byIndex(this.index + 1);
    }

    public static Optional<RefiningMastery> byIndex(int index) {
        for (RefiningMastery mastery : VALUES) {
            if (mastery.index == index) {
                return Optional.of(mastery);
            }
        }
        return Optional.empty();
    }

    public static Optional<RefiningMastery> bySerializedName(String name) {
        for (RefiningMastery mastery : VALUES) {
            if (mastery.serializedName.equals(name) || mastery.name().equalsIgnoreCase(name)) {
                return Optional.of(mastery);
            }
        }
        return Optional.empty();
    }
}
