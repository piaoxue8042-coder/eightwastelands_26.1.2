package com.px8042.eightwastelands.item.storage;

public enum StorageBagTier {
    QI_REFINING("qi_refining", 30, 4),
    FOUNDATION_BUILDING("foundation_building", 50, 6);

    private final String serializedName;
    private final int capacity;
    private final int menuRows;

    StorageBagTier(String serializedName, int capacity, int menuRows) {
        this.serializedName = serializedName;
        this.capacity = capacity;
        this.menuRows = menuRows;
    }

    public String getSerializedName() {
        return this.serializedName;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getMenuRows() {
        return this.menuRows;
    }

    public int getMenuSlotCount() {
        return this.menuRows * 9;
    }
}
