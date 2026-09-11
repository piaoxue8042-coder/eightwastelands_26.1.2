package com.px8042.eightwastelands.item.cultivation;

import net.minecraft.network.chat.Component;

public enum CultivationGearType {
    WEAPON("weapon"),
    OFFHAND("offhand"),
    ARMOR("armor"),
    FABAO("fabao");

    private final String serializedName;

    CultivationGearType(String serializedName) {
        this.serializedName = serializedName;
    }

    public Component getDisplayName() {
        return Component.translatable("gear_type.eightwastelands." + this.serializedName);
    }
}
