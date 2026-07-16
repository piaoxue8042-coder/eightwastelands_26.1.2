package com.px8042.eightwastelands.cultivation;

import net.minecraft.network.chat.Component;

import java.util.Optional;

public enum CultivationRealm {
    QI_REFINING(1, "qi_refining"),
    FOUNDATION_BUILDING(2, "foundation_building"),
    GOLDEN_CORE(3, "golden_core"),
    NASCENT_SOUL(4, "nascent_soul"),
    SPIRIT_TRANSFORMATION(5, "spirit_transformation"),
    VOID_REFINING(6, "void_refining"),
    BODY_INTEGRATION(7, "body_integration"),
    MAHAYANA(8, "mahayana"),
    TRIBULATION(9, "tribulation");

    private static final CultivationRealm[] VALUES = values();

    private final int index;
    private final String serializedName;

    CultivationRealm(int index, String serializedName) {
        this.index = index;
        this.serializedName = serializedName;
    }

    public int getIndex() {
        return this.index;
    }

    public String getSerializedName() {
        return this.serializedName;
    }

    public boolean isAtLeast(CultivationRealm requiredRealm) {
        return this.index >= requiredRealm.index;
    }

    public Component getDisplayName() {
        return Component.translatable("cultivation.eightwastelands.realm." + this.serializedName);
    }

    public static Optional<CultivationRealm> byIndex(int index) {
        for (CultivationRealm realm : VALUES) {
            if (realm.index == index) {
                return Optional.of(realm);
            }
        }

        return Optional.empty();
    }

    public static Optional<CultivationRealm> bySerializedName(String name) {
        for (CultivationRealm realm : VALUES) {
            if (realm.serializedName.equals(name) || realm.name().equalsIgnoreCase(name)) {
                return Optional.of(realm);
            }
        }

        return Optional.empty();
    }
}
