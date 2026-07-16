package com.px8042.eightwastelands.item.pill;

import com.px8042.eightwastelands.cultivation.CultivationRealm;

public class QiRefiningCultivationPillItem extends AbstractCultivationPillItem {

    public QiRefiningCultivationPillItem(Properties properties) {
        this(
                properties,
                PillGrade.IMMORTAL,
                PillData.cultivationQiGain(CultivationRealm.QI_REFINING, PillGrade.IMMORTAL)
        );
    }

    public QiRefiningCultivationPillItem(Properties properties, PillGrade grade, int qiGain) {
        super(properties, CultivationRealm.QI_REFINING, grade, qiGain);
    }
}
