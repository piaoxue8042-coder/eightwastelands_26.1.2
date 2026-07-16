package com.px8042.eightwastelands.alchemy;

import net.minecraft.network.chat.Component;

import java.util.Optional;

public enum AlchemyMastery {
    BEGINNER(0, "beginner"),
    SKILLED(1, "skilled"),
    PROFICIENT(2, "proficient"),
    MASTER(3, "master");

    private static final AlchemyMastery[] VALUES = values();

    private final int index;
    private final String serializedName;

    AlchemyMastery(int index, String serializedName) {
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
        return Component.translatable("alchemy.eightwastelands.mastery." + this.serializedName);
    }

    public Optional<AlchemyMastery> next() {
        return byIndex(this.index + 1);
    }

    public static Optional<AlchemyMastery> byIndex(int index) {
        for (AlchemyMastery mastery : VALUES) {
            if (mastery.index == index) {
                return Optional.of(mastery);
            }
        }

        return Optional.empty();
    }

    public static Optional<AlchemyMastery> bySerializedName(String name) {
        for (AlchemyMastery mastery : VALUES) {
            if (mastery.serializedName.equals(name) || mastery.name().equalsIgnoreCase(name)) {
                return Optional.of(mastery);
            }
        }

        return Optional.empty();
    }
}
