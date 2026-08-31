package com.px8042.eightwastelands.xinfa;

import com.px8042.eightwastelands.cultivation.CultivationRealm;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public record XinfaDefinition(
        String id,
        CultivationRealm requiredRealm,
        double completionMultiplier,
        List<XinfaPage> pages,
        List<XinfaNode> nodes
) {

    public XinfaDefinition {
        pages = List.copyOf(pages);
        nodes = List.copyOf(nodes);
    }

    public int pageCount() {
        return this.pages.size();
    }

    public int totalNodes() {
        return this.nodes.size();
    }

    public boolean isComplete(Collection<String> activatedNodeIds) {
        return countActivatedNodes(activatedNodeIds) >= totalNodes();
    }

    public int countActivatedNodes(Collection<String> activatedNodeIds) {
        int count = 0;
        for (XinfaNode node : this.nodes) {
            if (activatedNodeIds.contains(node.id())) {
                count++;
            }
        }

        return count;
    }

    public List<XinfaNode> nodesOnPage(int pageIndex) {
        return this.nodes.stream()
                .filter(node -> node.pageIndex() == pageIndex)
                .toList();
    }

    public Optional<XinfaNode> nodeById(String nodeId) {
        for (XinfaNode node : this.nodes) {
            if (node.id().equals(nodeId)) {
                return Optional.of(node);
            }
        }

        return Optional.empty();
    }

    public int recommendedPage(Collection<String> activatedNodeIds) {
        for (int index = 0; index < this.pages.size(); index++) {
            for (XinfaNode node : nodesOnPage(index)) {
                if (!activatedNodeIds.contains(node.id())) {
                    return index;
                }
            }
        }

        return Math.max(0, this.pages.size() - 1);
    }

    public Component getDisplayName() {
        return Component.translatable("xinfa.eightwastelands." + this.id + ".name");
    }

    public Component getNodeName(XinfaNode node) {
        return Component.translatable("xinfa.eightwastelands." + this.id + ".node." + node.id());
    }

    public Component getNodeDescription(XinfaNode node) {
        return Component.translatable("xinfa.eightwastelands." + this.id + ".node." + node.id() + ".description");
    }
}
