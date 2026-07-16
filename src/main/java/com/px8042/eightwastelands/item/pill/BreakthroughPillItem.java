package com.px8042.eightwastelands.item.pill;

import com.px8042.eightwastelands.cultivation.CultivationRealm;

public class BreakthroughPillItem extends AbstractBreakthroughPillItem {

    public BreakthroughPillItem(Properties properties) {
        super(properties);
    }

    public BreakthroughPillItem(
            Properties properties,
            CultivationRealm realm,
            PillGrade grade,
            double chanceBonus,
            long cooldownReductionTicks
    ) {
        super(properties, realm, grade, chanceBonus, cooldownReductionTicks);
    }

    public BreakthroughPillItem(
            Properties properties,
            PillGrade grade,
            double chanceBonus,
            long cooldownReductionTicks
    ) {
        super(properties, grade, chanceBonus, cooldownReductionTicks);
    }
}
