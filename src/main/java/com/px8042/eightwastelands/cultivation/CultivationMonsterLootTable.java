package com.px8042.eightwastelands.cultivation;

import com.px8042.eightwastelands.item.ModItems;
import com.px8042.eightwastelands.xinfa.XinfaManager;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class CultivationMonsterLootTable {

    private static final Map<CultivationStage, List<LootEntry>> LOOT_BY_STAGE = new EnumMap<>(CultivationStage.class);

    static {
        registerRealm(
                CultivationRealm.QI_REFINING,
                loot(ModItems.QI_REFINING_MONSTER_CORE, 0.70D),
                loot(ModItems.QI_REFINING_SPIRIT_HERB, 0.30D),
                loot(ModItems.WUDAO_SAND, XinfaManager.getWudaoSandDropChance())
        );
        registerRealm(
                CultivationRealm.FOUNDATION_BUILDING,
                loot(ModItems.FOUNDATION_BUILDING_MONSTER_CORE, 0.70D),
                loot(ModItems.FOUNDATION_BUILDING_SPIRIT_HERB, 0.30D),
                loot(ModItems.WUDAO_SAND, XinfaManager.getWudaoSandDropChance())
        );
    }

    private CultivationMonsterLootTable() {
    }

    public static List<ItemStack> rollDrops(CultivationStage stage, RandomSource random) {
        List<LootEntry> entries = LOOT_BY_STAGE.getOrDefault(stage, List.of());
        if (entries.isEmpty()) {
            return List.of();
        }

        List<ItemStack> drops = new ArrayList<>();
        for (LootEntry entry : entries) {
            entry.roll(random).ifPresent(drops::add);
        }

        return drops;
    }

    private static void registerRealm(CultivationRealm realm, LootEntry... entries) {
        for (CultivationStage stage : CultivationStage.values()) {
            if (stage.isInRealm(realm)) {
                registerStage(stage, entries);
            }
        }
    }

    private static void registerStage(CultivationStage stage, LootEntry... entries) {
        LOOT_BY_STAGE.put(stage, List.of(entries));
    }

    private static LootEntry loot(Supplier<? extends Item> item, double chance) {
        return new LootEntry(item, chance);
    }

    private record LootEntry(Supplier<? extends Item> item, double chance) {
        private java.util.Optional<ItemStack> roll(RandomSource random) {
            if (random.nextDouble() >= this.chance) {
                return java.util.Optional.empty();
            }

            return java.util.Optional.of(new ItemStack(this.item.get()));
        }
    }
}
