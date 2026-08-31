package com.px8042.eightwastelands.xinfa;

import com.px8042.eightwastelands.cultivation.CultivationRealm;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class XinfaCostTable {

    private static final Map<CultivationRealm, List<PageCostRule>> COSTS = new EnumMap<>(CultivationRealm.class);

    static {
        COSTS.put(CultivationRealm.QI_REFINING, List.of(rule(1, 18, 1, 3)));
        COSTS.put(CultivationRealm.FOUNDATION_BUILDING, List.of(rule(1, 18, 3, 5)));
        COSTS.put(CultivationRealm.GOLDEN_CORE, List.of(rule(1, 18, 5, 8)));
        COSTS.put(CultivationRealm.NASCENT_SOUL, List.of(rule(1, 18, 8, 12)));
        COSTS.put(CultivationRealm.SPIRIT_TRANSFORMATION, List.of(rule(1, 18, 12, 18)));
    }

    private XinfaCostTable() {
    }

    public static int cost(CultivationRealm realm, int pageIndex, boolean major) {
        int pageNumber = pageIndex + 1;
        for (PageCostRule rule : COSTS.getOrDefault(realm, List.of(rule(1, 18, 16, 24)))) {
            if (rule.matches(pageNumber)) {
                return major ? rule.majorCost() : rule.minorCost();
            }
        }

        return major ? 24 : 16;
    }

    private static PageCostRule rule(int firstPage, int lastPage, int minorCost, int majorCost) {
        return new PageCostRule(firstPage, lastPage, minorCost, majorCost);
    }

    private record PageCostRule(int firstPage, int lastPage, int minorCost, int majorCost) {

        private boolean matches(int pageNumber) {
            return pageNumber >= this.firstPage && pageNumber <= this.lastPage;
        }
    }
}
