package com.px8042.eightwastelands.item.pill;

import com.px8042.eightwastelands.cultivation.CultivationRealm;

public class FoundationBuildingCultivationPillItem extends AbstractCultivationPillItem {

    public FoundationBuildingCultivationPillItem(Properties properties) {
        this(
                properties,
                PillGrade.IMMORTAL,
                PillData.cultivationQiGain(CultivationRealm.FOUNDATION_BUILDING, PillGrade.IMMORTAL)
        );
    }

    public FoundationBuildingCultivationPillItem(Properties properties, PillGrade grade, int qiGain) {
        super(properties, CultivationRealm.FOUNDATION_BUILDING, grade, qiGain);
    }
}
