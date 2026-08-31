package com.px8042.eightwastelands.xinfa;

import com.px8042.eightwastelands.EightWastelands;
import com.px8042.eightwastelands.cultivation.CultivationAttribute;
import com.px8042.eightwastelands.cultivation.CultivationManager;
import com.px8042.eightwastelands.cultivation.CultivationStage;
import com.px8042.eightwastelands.item.ModItems;
import com.px8042.eightwastelands.network.XinfaListSyncPayload;
import com.px8042.eightwastelands.network.XinfaSyncPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class XinfaManager {

    private static final String LEARNED_XINFA = "eightwastelands_learned_xinfa";
    private static final double WUDAO_SAND_DROP_CHANCE = 0.25D;

    private XinfaManager() {
    }

    public static double getWudaoSandDropChance() {
        return WUDAO_SAND_DROP_CHANCE;
    }

    public static void useXinfaItem(ServerPlayer player, XinfaDefinition definition, ItemStack stack) {
        if (!hasRequiredRealm(player, definition)) {
            player.sendOverlayMessage(Component.translatable(
                    "message.eightwastelands.xinfa.required_realm",
                    definition.requiredRealm().getDisplayName()
            ));
            sendSync(player, definition, false);
            return;
        }

        if (!isLearned(player, definition)) {
            learnXinfa(player, definition);
            if (!player.isCreative()) {
                stack.shrink(1);
                player.getInventory().setChanged();
            }

            applyAttributes(player);
            CultivationManager.syncTo(player);
            player.sendOverlayMessage(Component.translatable(
                    "message.eightwastelands.xinfa.learned",
                    definition.getDisplayName()
            ));
        }

        sendSync(player, definition, true);
    }

    public static void openXinfa(ServerPlayer player, String xinfaId) {
        XinfaDefinitions.byId(xinfaId).ifPresent(definition -> {
            if (!isLearned(player, definition)) {
                player.sendOverlayMessage(Component.translatable("message.eightwastelands.xinfa.not_learned"));
                sendSync(player, definition, false);
                return;
            }

            sendSync(player, definition, true);
        });
    }

    public static void openXinfaList(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new XinfaListSyncPayload(buildLearnedListEntries(player)));
    }

    public static void handleActivateNode(ServerPlayer player, String xinfaId, String nodeId) {
        XinfaDefinitions.byId(xinfaId).ifPresent(definition ->
                definition.nodeById(nodeId).ifPresent(node -> activateNode(player, definition, node))
        );
    }

    public static boolean isLearned(Player player, XinfaDefinition definition) {
        return getLearnedXinfaIds(player).contains(definition.id()) || hasStoredProgress(player, definition);
    }

    public static Set<String> getActivatedNodeIds(Player player, XinfaDefinition definition) {
        CompoundTag data = player.getPersistentData();
        String key = activatedKey(definition);
        Set<String> activatedNodeIds = new HashSet<>();

        if (data.contains(key)) {
            ListTag list = data.getListOrEmpty(key);
            for (int index = 0; index < list.size(); index++) {
                String nodeId = list.getStringOr(index, "");
                if (definition.nodeById(nodeId).isPresent()) {
                    activatedNodeIds.add(nodeId);
                }
            }

            return activatedNodeIds;
        }

        int legacyProgress = Math.max(0, Math.min(
                definition.totalNodes(),
                data.getIntOr(legacyProgressKey(definition), 0)
        ));
        for (int index = 0; index < legacyProgress; index++) {
            activatedNodeIds.add(definition.nodes().get(index).id());
        }

        if (legacyProgress > 0) {
            saveActivatedNodeIds(data, definition, activatedNodeIds);
        }

        return activatedNodeIds;
    }

    public static int getProgress(Player player, XinfaDefinition definition) {
        return definition.countActivatedNodes(getActivatedNodeIds(player, definition));
    }

    public static int getWudaoSandCount(Player player) {
        int count = 0;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.is(ModItems.WUDAO_SAND.get())) {
                count += stack.getCount();
            }
        }

        return count;
    }

    public static void applyAttributes(ServerPlayer player) {
        clearXinfaAttributeModifiers(player);

        Map<CultivationAttribute, Double> bonuses = collectLearnedAttributeBonuses(player);
        for (Map.Entry<CultivationAttribute, Double> entry : bonuses.entrySet()) {
            applyAttributeBonus(player, entry.getKey(), entry.getValue());
        }

        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    public static double getMaxZhenyuanBonus(ServerPlayer player) {
        return collectLearnedSpecialBonus(player, XinfaSpecialAttribute.MAX_ZHENYUAN);
    }

    public static double getZhenyuanRegenBonus(ServerPlayer player) {
        return collectLearnedSpecialBonus(player, XinfaSpecialAttribute.ZHENYUAN_REGEN);
    }

    public static int getTunaiQiGainBonus(ServerPlayer player) {
        return Math.max(0, (int) Math.round(collectLearnedSpecialBonus(player, XinfaSpecialAttribute.TUNAI_QI_GAIN)));
    }

    public static int getTunaiDailyUsesBonus(ServerPlayer player) {
        return Math.max(0, (int) Math.floor(collectLearnedSpecialBonus(player, XinfaSpecialAttribute.TUNAI_DAILY_USES)));
    }

    public static void copyData(CompoundTag oldData, CompoundTag newData) {
        if (oldData.contains(LEARNED_XINFA)) {
            newData.put(LEARNED_XINFA, oldData.getListOrEmpty(LEARNED_XINFA).copy());
        }

        for (XinfaDefinition definition : XinfaDefinitions.all()) {
            String activatedKey = activatedKey(definition);
            if (oldData.contains(activatedKey)) {
                newData.put(activatedKey, oldData.getListOrEmpty(activatedKey).copy());
            }

            String legacyKey = legacyProgressKey(definition);
            if (oldData.contains(legacyKey)) {
                newData.putInt(legacyKey, oldData.getIntOr(legacyKey, 0));
            }
        }
    }

    private static void learnXinfa(ServerPlayer player, XinfaDefinition definition) {
        Set<String> learnedXinfaIds = getLearnedXinfaIds(player);
        if (learnedXinfaIds.add(definition.id())) {
            saveLearnedXinfaIds(player.getPersistentData(), learnedXinfaIds);
        }
    }

    private static void activateNode(ServerPlayer player, XinfaDefinition definition, XinfaNode node) {
        if (!isLearned(player, definition)) {
            player.sendOverlayMessage(Component.translatable("message.eightwastelands.xinfa.not_learned"));
            sendSync(player, definition, false);
            return;
        }

        if (!hasRequiredRealm(player, definition)) {
            player.sendOverlayMessage(Component.translatable(
                    "message.eightwastelands.xinfa.required_realm",
                    definition.requiredRealm().getDisplayName()
            ));
            sendSync(player, definition, false);
            return;
        }

        Set<String> activatedNodeIds = getActivatedNodeIds(player, definition);
        if (activatedNodeIds.contains(node.id())) {
            sendSync(player, definition, false);
            return;
        }

        if (definition.isComplete(activatedNodeIds)) {
            player.sendOverlayMessage(Component.translatable("message.eightwastelands.xinfa.complete"));
            sendSync(player, definition, false);
            return;
        }

        if (!node.canActivate(activatedNodeIds)) {
            player.sendOverlayMessage(Component.translatable("message.eightwastelands.xinfa.node_locked"));
            sendSync(player, definition, false);
            return;
        }

        if (!consumeWudaoSand(player, node.wudaoSandCost())) {
            player.sendOverlayMessage(Component.translatable(
                    "message.eightwastelands.xinfa.not_enough_wudao_sand",
                    node.wudaoSandCost()
            ));
            sendSync(player, definition, false);
            return;
        }

        activatedNodeIds.add(node.id());
        saveActivatedNodeIds(player.getPersistentData(), definition, activatedNodeIds);
        applyAttributes(player);
        CultivationManager.syncTo(player);
        player.sendOverlayMessage(Component.translatable(
                "message.eightwastelands.xinfa.node_activated",
                definition.getNodeName(node)
        ));
        sendSync(player, definition, false);
    }

    private static boolean consumeWudaoSand(ServerPlayer player, int amount) {
        if (amount <= 0 || player.isCreative()) {
            return true;
        }

        if (getWudaoSandCount(player) < amount) {
            return false;
        }

        int remaining = amount;
        for (int slot = 0; slot < player.getInventory().getContainerSize() && remaining > 0; slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (!stack.is(ModItems.WUDAO_SAND.get())) {
                continue;
            }

            int consumed = Math.min(remaining, stack.getCount());
            stack.shrink(consumed);
            remaining -= consumed;
        }

        player.getInventory().setChanged();
        return true;
    }

    private static Map<CultivationAttribute, Double> collectLearnedAttributeBonuses(ServerPlayer player) {
        Map<CultivationAttribute, Double> totals = new EnumMap<>(CultivationAttribute.class);
        for (XinfaDefinition definition : getLearnedDefinitions(player)) {
            Set<String> activatedNodeIds = getActivatedNodeIds(player, definition);
            double multiplier = definition.isComplete(activatedNodeIds)
                    ? definition.completionMultiplier()
                    : 1.0D;
            for (XinfaNode node : getActiveNodes(definition, activatedNodeIds)) {
                for (XinfaBonus bonus : node.bonuses()) {
                    if (bonus.isAttributeBonus()) {
                        totals.merge(bonus.attribute(), bonus.amount() * multiplier, Double::sum);
                    }
                }
            }
        }

        return totals;
    }

    private static double collectLearnedSpecialBonus(ServerPlayer player, XinfaSpecialAttribute attribute) {
        double total = 0.0D;
        for (XinfaDefinition definition : getLearnedDefinitions(player)) {
            Set<String> activatedNodeIds = getActivatedNodeIds(player, definition);
            double multiplier = definition.isComplete(activatedNodeIds)
                    ? definition.completionMultiplier()
                    : 1.0D;
            for (XinfaNode node : getActiveNodes(definition, activatedNodeIds)) {
                for (XinfaBonus bonus : node.bonuses()) {
                    if (bonus.isSpecialBonus(attribute)) {
                        total += bonus.amount() * multiplier;
                    }
                }
            }
        }

        return total;
    }

    private static List<XinfaNode> getActiveNodes(XinfaDefinition definition, Set<String> activatedNodeIds) {
        return definition.nodes().stream()
                .filter(node -> activatedNodeIds.contains(node.id()))
                .toList();
    }

    private static Set<XinfaDefinition> getLearnedDefinitions(Player player) {
        Set<XinfaDefinition> definitions = new HashSet<>();
        Set<String> learnedXinfaIds = getLearnedXinfaIds(player);
        boolean changed = false;

        for (XinfaDefinition definition : XinfaDefinitions.all()) {
            boolean learnedOrHasProgress = learnedXinfaIds.contains(definition.id()) || hasStoredProgress(player, definition);
            if (learnedOrHasProgress) {
                changed |= learnedXinfaIds.add(definition.id());
                if (hasRequiredRealm(player, definition)) {
                    definitions.add(definition);
                }
            }
        }

        if (changed) {
            saveLearnedXinfaIds(player.getPersistentData(), learnedXinfaIds);
        }

        return definitions;
    }

    private static boolean hasRequiredRealm(Player player, XinfaDefinition definition) {
        return CultivationStage.byIndex(player.getPersistentData().getIntOr(CultivationManager.STAGE, 0)).getRealm()
                .map(realm -> realm.isAtLeast(definition.requiredRealm()))
                .orElse(false);
    }

    private static boolean hasStoredProgress(Player player, XinfaDefinition definition) {
        CompoundTag data = player.getPersistentData();
        if (data.contains(activatedKey(definition))) {
            ListTag list = data.getListOrEmpty(activatedKey(definition));
            for (int index = 0; index < list.size(); index++) {
                if (definition.nodeById(list.getStringOr(index, "")).isPresent()) {
                    return true;
                }
            }
        }

        return data.getIntOr(legacyProgressKey(definition), 0) > 0;
    }

    private static Set<String> getLearnedXinfaIds(Player player) {
        Set<String> learnedXinfaIds = new HashSet<>();
        ListTag list = player.getPersistentData().getListOrEmpty(LEARNED_XINFA);
        for (int index = 0; index < list.size(); index++) {
            String xinfaId = list.getStringOr(index, "");
            if (XinfaDefinitions.byId(xinfaId).isPresent()) {
                learnedXinfaIds.add(xinfaId);
            }
        }

        return learnedXinfaIds;
    }

    private static void saveLearnedXinfaIds(CompoundTag data, Set<String> learnedXinfaIds) {
        ListTag list = new ListTag();
        for (XinfaDefinition definition : XinfaDefinitions.all()) {
            if (learnedXinfaIds.contains(definition.id())) {
                list.add(StringTag.valueOf(definition.id()));
            }
        }

        data.put(LEARNED_XINFA, list);
    }

    private static void clearXinfaAttributeModifiers(ServerPlayer player) {
        for (CultivationAttribute attribute : CultivationAttribute.values()) {
            AttributeInstance instance = player.getAttribute(attribute.getAttribute());
            if (instance != null) {
                instance.removeModifier(modifierId(attribute));
            }
        }
    }

    private static void applyAttributeBonus(ServerPlayer player, CultivationAttribute attribute, double amount) {
        if (amount == 0.0D) {
            return;
        }

        AttributeInstance instance = player.getAttribute(attribute.getAttribute());
        if (instance == null) {
            return;
        }

        instance.addOrUpdateTransientModifier(new AttributeModifier(
                modifierId(attribute),
                amount,
                AttributeModifier.Operation.ADD_VALUE
        ));
    }

    private static Identifier modifierId(CultivationAttribute attribute) {
        return Identifier.fromNamespaceAndPath(
                EightWastelands.MODID,
                "xinfa_" + attribute.name().toLowerCase(java.util.Locale.ROOT)
        );
    }

    private static void sendSync(ServerPlayer player, XinfaDefinition definition, boolean openScreen) {
        PacketDistributor.sendToPlayer(player, new XinfaSyncPayload(
                definition.id(),
                orderedActivatedNodeIds(player, definition),
                getWudaoSandCount(player),
                openScreen
        ));
    }

    private static List<XinfaListSyncPayload.Entry> buildLearnedListEntries(Player player) {
        Set<String> learnedXinfaIds = getLearnedXinfaIds(player);
        List<XinfaListSyncPayload.Entry> entries = new ArrayList<>();
        boolean changed = false;

        for (XinfaDefinition definition : XinfaDefinitions.all()) {
            boolean learnedOrHasProgress = learnedXinfaIds.contains(definition.id()) || hasStoredProgress(player, definition);
            if (learnedOrHasProgress) {
                changed |= learnedXinfaIds.add(definition.id());
                Set<String> activatedNodeIds = getActivatedNodeIds(player, definition);
                entries.add(new XinfaListSyncPayload.Entry(
                        definition.id(),
                        definition.countActivatedNodes(activatedNodeIds),
                        definition.totalNodes()
                ));
            }
        }

        if (changed) {
            saveLearnedXinfaIds(player.getPersistentData(), learnedXinfaIds);
        }

        return entries;
    }

    private static List<String> orderedActivatedNodeIds(Player player, XinfaDefinition definition) {
        Set<String> activatedNodeIds = getActivatedNodeIds(player, definition);
        List<String> ordered = new ArrayList<>();
        for (XinfaNode node : definition.nodes()) {
            if (activatedNodeIds.contains(node.id())) {
                ordered.add(node.id());
            }
        }

        return ordered;
    }

    private static void saveActivatedNodeIds(CompoundTag data, XinfaDefinition definition, Set<String> activatedNodeIds) {
        ListTag list = new ListTag();
        for (XinfaNode node : definition.nodes()) {
            if (activatedNodeIds.contains(node.id())) {
                list.add(StringTag.valueOf(node.id()));
            }
        }

        data.put(activatedKey(definition), list);
    }

    private static String activatedKey(XinfaDefinition definition) {
        return "eightwastelands_xinfa_" + definition.id() + "_activated";
    }

    private static String legacyProgressKey(XinfaDefinition definition) {
        return "eightwastelands_xinfa_" + definition.id() + "_progress";
    }
}
