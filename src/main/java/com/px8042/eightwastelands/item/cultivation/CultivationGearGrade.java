package com.px8042.eightwastelands.item.cultivation;

import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Optional;

public enum CultivationGearGrade {
    LOW("low", 0.80D, 2),
    MIDDLE("middle", 1.00D, 3),
    HIGH("high", 1.30D, 4),
    IMMORTAL("immortal", 1.70D, 5);

    private final String serializedName;
    private final double attributeMultiplier;
    private final int maxRandomAffixes;

    CultivationGearGrade(String serializedName, double attributeMultiplier, int maxRandomAffixes) {
        this.serializedName = serializedName;
        this.attributeMultiplier = attributeMultiplier;
        this.maxRandomAffixes = maxRandomAffixes;
    }

    public String getSerializedName() {
        return this.serializedName;
    }

    public Component getDisplayName() {
        return Component.translatable("gear_grade.eightwastelands." + this.serializedName);
    }

    public double getAttributeMultiplier() {
        return this.attributeMultiplier;
    }

    public int getMaxRandomAffixes() {
        return this.maxRandomAffixes;
    }

    public static Optional<CultivationGearGrade> bySerializedName(String serializedName) {
        return Arrays.stream(values())
                .filter(grade -> grade.serializedName.equals(serializedName))
                .findFirst();
    }
}
