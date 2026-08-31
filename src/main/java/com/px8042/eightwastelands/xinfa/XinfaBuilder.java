package com.px8042.eightwastelands.xinfa;

import com.px8042.eightwastelands.cultivation.CultivationRealm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class XinfaBuilder {

    private static final int[] LINEAR_NODE_X = {28, 106, 184};
    private static final int LINEAR_NODE_Y = 78;
    private static final int MAX_NODE_X = 212;
    private static final int MAX_NODE_Y = 176;

    private final String id;
    private final List<XinfaPage> pages = new ArrayList<>();
    private final List<NodeSpec> nodes = new ArrayList<>();
    private CultivationRealm requiredRealm;
    private double completionMultiplier = 1.0D;
    private int currentPageIndex = -1;
    private int currentPageLinearNodeCount;
    private String previousNodeId;

    private XinfaBuilder(String id) {
        this.id = requireText(id, "xinfa id");
    }

    public static XinfaBuilder create(String id) {
        return new XinfaBuilder(id);
    }

    public XinfaBuilder requiredRealm(CultivationRealm requiredRealm) {
        if (requiredRealm == null) {
            throw new IllegalArgumentException("Xinfa " + this.id + " requires a realm.");
        }

        this.requiredRealm = requiredRealm;
        return this;
    }

    public XinfaBuilder completionMultiplier(double completionMultiplier) {
        if (completionMultiplier <= 0.0D) {
            throw new IllegalArgumentException("Xinfa " + this.id + " completion multiplier must be positive.");
        }

        this.completionMultiplier = completionMultiplier;
        return this;
    }

    public XinfaBuilder linearPage(String pageId) {
        this.pages.add(new XinfaPage(requireText(pageId, "page id")));
        this.currentPageIndex = this.pages.size() - 1;
        this.currentPageLinearNodeCount = 0;
        return this;
    }

    public XinfaBuilder node(String nodeId, XinfaBonus... bonuses) {
        return linearNode(nodeId, false, null, bonuses);
    }

    public XinfaBuilder majorNode(String nodeId, XinfaBonus... bonuses) {
        return linearNode(nodeId, true, null, bonuses);
    }

    public XinfaBuilder nodeWithCost(String nodeId, int wudaoSandCost, XinfaBonus... bonuses) {
        return linearNode(nodeId, false, wudaoSandCost, bonuses);
    }

    public XinfaBuilder majorNodeWithCost(String nodeId, int wudaoSandCost, XinfaBonus... bonuses) {
        return linearNode(nodeId, true, wudaoSandCost, bonuses);
    }

    public XinfaBuilder positionedNode(
            String nodeId,
            int x,
            int y,
            List<String> prerequisites,
            XinfaBonus... bonuses
    ) {
        return positionedNode(nodeId, x, y, false, null, prerequisites, bonuses);
    }

    public XinfaBuilder positionedMajorNode(
            String nodeId,
            int x,
            int y,
            List<String> prerequisites,
            XinfaBonus... bonuses
    ) {
        return positionedNode(nodeId, x, y, true, null, prerequisites, bonuses);
    }

    public XinfaDefinition build() {
        validateDefinition();
        List<XinfaNode> builtNodes = new ArrayList<>();
        for (NodeSpec node : this.nodes) {
            builtNodes.add(new XinfaNode(
                    node.id(),
                    node.pageIndex(),
                    node.x(),
                    node.y(),
                    node.major(),
                    node.costOverride() == null
                            ? XinfaCostTable.cost(this.requiredRealm, node.pageIndex(), node.major())
                            : node.costOverride(),
                    node.prerequisites(),
                    node.bonuses()
            ));
        }

        return new XinfaDefinition(
                this.id,
                this.requiredRealm,
                this.completionMultiplier,
                this.pages,
                builtNodes
        );
    }

    private XinfaBuilder linearNode(String nodeId, boolean major, Integer costOverride, XinfaBonus... bonuses) {
        requireCurrentPage();
        if (this.currentPageLinearNodeCount >= LINEAR_NODE_X.length) {
            throw new IllegalStateException(
                    "Xinfa " + this.id + " page " + this.currentPageIndex + " has too many linear nodes."
            );
        }

        int x = LINEAR_NODE_X[this.currentPageLinearNodeCount];
        this.currentPageLinearNodeCount++;
        return addNode(
                nodeId,
                x,
                LINEAR_NODE_Y,
                major,
                costOverride,
                this.previousNodeId == null ? List.of() : List.of(this.previousNodeId),
                bonuses
        );
    }

    private XinfaBuilder positionedNode(
            String nodeId,
            int x,
            int y,
            boolean major,
            Integer costOverride,
            List<String> prerequisites,
            XinfaBonus... bonuses
    ) {
        requireCurrentPage();
        return addNode(nodeId, x, y, major, costOverride, prerequisites, bonuses);
    }

    private XinfaBuilder addNode(
            String nodeId,
            int x,
            int y,
            boolean major,
            Integer costOverride,
            List<String> prerequisites,
            XinfaBonus... bonuses
    ) {
        String id = requireText(nodeId, "node id");
        validatePosition(id, x, y);
        if (costOverride != null && costOverride < 0) {
            throw new IllegalArgumentException("Xinfa " + this.id + " node " + id + " has negative cost.");
        }

        List<String> copiedPrerequisites = prerequisites == null ? List.of() : List.copyOf(prerequisites);
        List<XinfaBonus> copiedBonuses = bonuses == null ? List.of() : List.of(bonuses);
        this.nodes.add(new NodeSpec(
                id,
                this.currentPageIndex,
                x,
                y,
                major,
                costOverride,
                copiedPrerequisites,
                copiedBonuses
        ));
        this.previousNodeId = id;
        return this;
    }

    private void validateDefinition() {
        if (this.requiredRealm == null) {
            throw new IllegalStateException("Xinfa " + this.id + " requires a realm.");
        }

        if (this.pages.isEmpty()) {
            throw new IllegalStateException("Xinfa " + this.id + " must have at least one page.");
        }

        if (this.nodes.isEmpty()) {
            throw new IllegalStateException("Xinfa " + this.id + " must have at least one node.");
        }

        Set<String> pageIds = new HashSet<>();
        for (XinfaPage page : this.pages) {
            if (!pageIds.add(page.id())) {
                throw new IllegalStateException("Xinfa " + this.id + " has duplicate page id " + page.id() + ".");
            }
        }

        Set<String> nodeIds = new HashSet<>();
        for (NodeSpec node : this.nodes) {
            if (!nodeIds.add(node.id())) {
                throw new IllegalStateException("Xinfa " + this.id + " has duplicate node id " + node.id() + ".");
            }
        }

        for (NodeSpec node : this.nodes) {
            if (node.pageIndex() < 0 || node.pageIndex() >= this.pages.size()) {
                throw new IllegalStateException("Xinfa " + this.id + " node " + node.id() + " has invalid page.");
            }

            Set<String> prerequisites = new HashSet<>();
            for (String prerequisite : node.prerequisites()) {
                String prerequisiteId = requireText(prerequisite, "prerequisite id");
                if (node.id().equals(prerequisiteId)) {
                    throw new IllegalStateException("Xinfa " + this.id + " node " + node.id() + " requires itself.");
                }

                if (!nodeIds.contains(prerequisiteId)) {
                    throw new IllegalStateException(
                            "Xinfa " + this.id + " node " + node.id() + " requires missing node " + prerequisiteId + "."
                    );
                }

                if (!prerequisites.add(prerequisiteId)) {
                    throw new IllegalStateException(
                            "Xinfa " + this.id + " node " + node.id()
                                    + " repeats prerequisite " + prerequisiteId + "."
                    );
                }
            }
        }
    }

    private void requireCurrentPage() {
        if (this.currentPageIndex < 0) {
            throw new IllegalStateException("Xinfa " + this.id + " must define a page before nodes.");
        }
    }

    private void validatePosition(String nodeId, int x, int y) {
        if (x < 0 || x > MAX_NODE_X || y < 0 || y > MAX_NODE_Y) {
            throw new IllegalArgumentException(
                    "Xinfa " + this.id + " node " + nodeId + " position is outside the current screen."
            );
        }
    }

    private static String requireText(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing " + name + ".");
        }

        return value;
    }

    private record NodeSpec(
            String id,
            int pageIndex,
            int x,
            int y,
            boolean major,
            Integer costOverride,
            List<String> prerequisites,
            List<XinfaBonus> bonuses
    ) {
    }
}
