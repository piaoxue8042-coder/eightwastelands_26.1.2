package com.px8042.eightwastelands.xinfa;

public enum XinfaSpecialAttribute {
    MAX_ZHENYUAN("max_zhenyuan"),
    ZHENYUAN_REGEN("zhenyuan_regen"),
    TUNAI_QI_GAIN("tunai_qi_gain"),
    TUNAI_DAILY_USES("tunai_daily_uses");

    private final String serializedName;

    XinfaSpecialAttribute(String serializedName) {
        this.serializedName = serializedName;
    }

    public String getSerializedName() {
        return this.serializedName;
    }
}
