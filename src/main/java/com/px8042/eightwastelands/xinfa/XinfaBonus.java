package com.px8042.eightwastelands.xinfa;

import com.px8042.eightwastelands.cultivation.CultivationAttribute;

public record XinfaBonus(
        CultivationAttribute attribute,
        XinfaSpecialAttribute specialAttribute,
        double amount
) {

    public static XinfaBonus attribute(CultivationAttribute attribute, double amount) {
        return new XinfaBonus(attribute, null, amount);
    }

    public static XinfaBonus special(XinfaSpecialAttribute attribute, double amount) {
        return new XinfaBonus(null, attribute, amount);
    }

    public boolean isAttributeBonus() {
        return this.attribute != null;
    }

    public boolean isSpecialBonus(XinfaSpecialAttribute attribute) {
        return this.specialAttribute == attribute;
    }
}
