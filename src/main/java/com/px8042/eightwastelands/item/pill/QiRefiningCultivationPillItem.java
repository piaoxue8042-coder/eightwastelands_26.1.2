package com.px8042.eightwastelands.item.pill;

import com.px8042.eightwastelands.cultivation.CultivationRealm;

public class QiRefiningCultivationPillItem extends AbstractCultivationPillItem {

    private static final int LEGACY_QI_GAIN = 50;

    public QiRefiningCultivationPillItem(Properties properties) {
        this(properties, PillGrade.IMMORTAL, LEGACY_QI_GAIN);
    }

    public QiRefiningCultivationPillItem(Properties properties, PillGrade grade, int qiGain) {
        super(properties, CultivationRealm.QI_REFINING, grade, qiGain);
    }
}
