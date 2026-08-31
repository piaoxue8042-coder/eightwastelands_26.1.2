package com.px8042.eightwastelands.xinfa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.px8042.eightwastelands.network.XinfaListSyncPayload;

public final class XinfaClientCache {

    private static String xinfaId = XinfaDefinitions.QI_REFINING_MANUAL_ID;
    private static Set<String> activatedNodeIds = Set.of();
    private static List<XinfaListSyncPayload.Entry> learnedXinfaEntries = List.of();
    private static int wudaoSandCount;

    private XinfaClientCache() {
    }

    public static void set(String id, List<String> newActivatedNodeIds, int newWudaoSandCount) {
        xinfaId = id;
        activatedNodeIds = Set.copyOf(newActivatedNodeIds);
        wudaoSandCount = Math.max(0, newWudaoSandCount);
    }

    public static String getXinfaId() {
        return xinfaId;
    }

    public static Set<String> getActivatedNodeIds() {
        return new HashSet<>(activatedNodeIds);
    }

    public static int getWudaoSandCount() {
        return wudaoSandCount;
    }

    public static void setLearnedXinfaEntries(List<XinfaListSyncPayload.Entry> entries) {
        learnedXinfaEntries = List.copyOf(entries);
    }

    public static List<XinfaListSyncPayload.Entry> getLearnedXinfaEntries() {
        return List.copyOf(learnedXinfaEntries);
    }
}
