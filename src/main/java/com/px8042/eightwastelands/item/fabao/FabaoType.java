package com.px8042.eightwastelands.item.fabao;

public enum FabaoType {
    SWORD("sword", "fabao/type/sword"),
    BLADE("blade", "fabao/type/blade"),
    AXE("axe", "fabao/type/axe"),
    SPEAR("spear", "fabao/type/spear"),
    NEEDLE("needle", "fabao/type/needle"),
    SEAL("seal", "fabao/type/seal"),
    BELL("bell", "fabao/type/bell"),
    MIRROR("mirror", "fabao/type/mirror"),
    PEARL("pearl", "fabao/type/pearl"),
    BANNER("banner", "fabao/type/banner"),
    GOURD("gourd", "fabao/type/gourd"),
    FAN("fan", "fabao/type/fan"),
    TALISMAN("talisman", "fabao/type/talisman"),
    CHAIN("chain", "fabao/type/chain"),
    OTHER("other", "fabao/type/other");

    private final String id;
    private final String tagPath;

    FabaoType(String id, String tagPath) {
        this.id = id;
        this.tagPath = tagPath;
    }

    public String getId() {
        return this.id;
    }

    public String getTagPath() {
        return this.tagPath;
    }
}
