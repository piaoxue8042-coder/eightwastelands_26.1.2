package com.px8042.eightwastelands.alchemy;

import net.minecraft.network.chat.Component;

public enum AlchemyFormulaType {
    CULTIVATION("cultivation"),
    BREAKTHROUGH("breakthrough");

    private final String serializedName;

    AlchemyFormulaType(String serializedName) {
        this.serializedName = serializedName;
    }

    public String getSerializedName() {
        return this.serializedName;
    }

    public Component getDisplayName() {
        return Component.translatable("alchemy.eightwastelands.formula_type." + this.serializedName);
    }
}
