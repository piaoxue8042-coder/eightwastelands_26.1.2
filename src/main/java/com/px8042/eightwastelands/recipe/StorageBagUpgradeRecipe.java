package com.px8042.eightwastelands.recipe;

import com.mojang.serialization.MapCodec;
import com.px8042.eightwastelands.item.ModItems;
import com.px8042.eightwastelands.item.storage.StorageBagContents;
import com.px8042.eightwastelands.item.storage.StorageBagTier;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class StorageBagUpgradeRecipe extends CustomRecipe {

    public static final StorageBagUpgradeRecipe INSTANCE = new StorageBagUpgradeRecipe();
    public static final MapCodec<StorageBagUpgradeRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, StorageBagUpgradeRecipe> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return findQiRefiningBag(input) != null && hasOnlyUpgradeIngredients(input);
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack qiRefiningBag = findQiRefiningBag(input);
        if (qiRefiningBag == null || !hasOnlyUpgradeIngredients(input)) {
            return ItemStack.EMPTY;
        }

        ItemStack upgradedBag = new ItemStack(ModItems.FOUNDATION_BUILDING_STORAGE_BAG.get());
        StorageBagContents.copyContents(qiRefiningBag, upgradedBag, StorageBagTier.FOUNDATION_BUILDING);
        return upgradedBag;
    }

    @Override
    public RecipeSerializer<StorageBagUpgradeRecipe> getSerializer() {
        return ModRecipeSerializers.STORAGE_BAG_UPGRADE.get();
    }

    private static ItemStack findQiRefiningBag(CraftingInput input) {
        ItemStack bag = null;
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (!stack.is(ModItems.QI_REFINING_STORAGE_BAG.get())) {
                continue;
            }
            if (bag != null) {
                return null;
            }
            bag = stack;
        }
        return bag;
    }

    private static boolean hasOnlyUpgradeIngredients(CraftingInput input) {
        int bags = 0;
        int monsterCores = 0;
        int spiritHerbs = 0;

        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(ModItems.QI_REFINING_STORAGE_BAG.get())) {
                bags++;
            } else if (stack.is(ModItems.FOUNDATION_BUILDING_MONSTER_CORE.get())) {
                monsterCores++;
            } else if (stack.is(ModItems.FOUNDATION_BUILDING_SPIRIT_HERB.get())) {
                spiritHerbs++;
            } else {
                return false;
            }
        }
        return bags == 1 && monsterCores == 1 && spiritHerbs == 1;
    }
}
