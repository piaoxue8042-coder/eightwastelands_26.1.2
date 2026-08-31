package com.px8042.eightwastelands.xinfa;

import java.util.List;
import java.util.Set;

public record XinfaNode(
        String id,
        int pageIndex,
        int x,
        int y,
        boolean major,
        int wudaoSandCost,
        List<String> prerequisites,
        List<XinfaBonus> bonuses
) {

    public XinfaNode {
        prerequisites = List.copyOf(prerequisites);
        bonuses = List.copyOf(bonuses);
    }

    public boolean canActivate(Set<String> activatedNodeIds) {
        return !activatedNodeIds.contains(this.id) && activatedNodeIds.containsAll(this.prerequisites);
    }
}
