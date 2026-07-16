package com.px8042.eightwastelands.item.pill;

import com.px8042.eightwastelands.cultivation.CultivationRealm;

public class FoundationBuildingCultivationPillItem extends AbstractCultivationPillItem {

    private static final int LEGACY_QI_GAIN = 150;

    public FoundationBuildingCultivationPillItem(Properties properties) {
        this(properties, PillGrade.IMMORTAL, LEGACY_QI_GAIN);
    }

    public FoundationBuildingCultivationPillItem(Properties properties, PillGrade grade, int qiGain) {
        super(properties, CultivationRealm.FOUNDATION_BUILDING, grade, qiGain);
    }
}
