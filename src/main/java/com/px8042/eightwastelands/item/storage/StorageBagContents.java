package com.px8042.eightwastelands.item.storage;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public final class StorageBagContents {

    private StorageBagContents() {
    }

    public static boolean isStorageBag(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof StorageBagItem;
    }

    public static NonNullList<ItemStack> load(ItemStack bag, StorageBagTier tier) {
        NonNullList<ItemStack> contents = NonNullList.withSize(tier.getMenuSlotCount(), ItemStack.EMPTY);
        bag.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(contents);

        for (int slot = tier.getCapacity(); slot < contents.size(); slot++) {
            contents.set(slot, ItemStack.EMPTY);
        }
        return contents;
    }

    public static void save(ItemStack bag, StorageBagTier tier, List<ItemStack> contents) {
        List<ItemStack> savedContents = new ArrayList<>(tier.getCapacity());
        for (int slot = 0; slot < tier.getCapacity(); slot++) {
            ItemStack stack = slot < contents.size() ? contents.get(slot) : ItemStack.EMPTY;
            savedContents.add(stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
        }
        bag.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(savedContents));
    }

    public static void copyContents(ItemStack source, ItemStack target, StorageBagTier targetTier) {
        NonNullList<ItemStack> contents = load(source, getTier(source));
        save(target, targetTier, contents);
    }

    public static ItemStack insertIntoPlayerBags(Inventory inventory, ItemStack offered) {
        ItemStack remaining = offered.copy();
        if (remaining.isEmpty() || isStorageBag(remaining)) {
            return remaining;
        }

        boolean changed = false;
        for (int slot = 0; slot < inventory.getContainerSize() && !remaining.isEmpty(); slot++) {
            ItemStack bag = inventory.getItem(slot);
            if (!(bag.getItem() instanceof StorageBagItem storageBagItem)) {
                continue;
            }

            int previousCount = remaining.getCount();
            remaining = insertIntoBag(bag, storageBagItem.getTier(), remaining);
            changed |= previousCount != remaining.getCount();
        }
        if (changed) {
            inventory.setChanged();
        }
        return remaining;
    }

    public static boolean hasEmptySlotInPlayerBags(Inventory inventory) {
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack bag = inventory.getItem(slot);
            if (!(bag.getItem() instanceof StorageBagItem storageBagItem)) {
                continue;
            }

            NonNullList<ItemStack> contents = load(bag, storageBagItem.getTier());
            for (int contentsSlot = 0; contentsSlot < storageBagItem.getTier().getCapacity(); contentsSlot++) {
                if (contents.get(contentsSlot).isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ItemStack simulateInsertIntoPlayerBags(Inventory inventory, ItemStack offered) {
        ItemStack remaining = offered.copy();
        if (remaining.isEmpty() || isStorageBag(remaining)) {
            return remaining;
        }

        for (int slot = 0; slot < inventory.getContainerSize() && !remaining.isEmpty(); slot++) {
            ItemStack bag = inventory.getItem(slot);
            if (!(bag.getItem() instanceof StorageBagItem storageBagItem)) {
                continue;
            }

            remaining = insertIntoBag(bag.copy(), storageBagItem.getTier(), remaining);
        }
        return remaining;
    }

    private static ItemStack insertIntoBag(ItemStack bag, StorageBagTier tier, ItemStack offered) {
        ItemStack remaining = offered.copy();
        NonNullList<ItemStack> contents = load(bag, tier);
        boolean changed = false;

        for (int slot = 0; slot < tier.getCapacity() && !remaining.isEmpty(); slot++) {
            ItemStack stored = contents.get(slot);
            if (stored.isEmpty() || !ItemStack.isSameItemSameComponents(stored, remaining)) {
                continue;
            }

            int moveAmount = Math.min(
                    remaining.getCount(),
                    Math.min(stored.getMaxStackSize(), remaining.getMaxStackSize()) - stored.getCount()
            );
            if (moveAmount <= 0) {
                continue;
            }

            stored.grow(moveAmount);
            remaining.shrink(moveAmount);
            changed = true;
        }

        for (int slot = 0; slot < tier.getCapacity() && !remaining.isEmpty(); slot++) {
            if (!contents.get(slot).isEmpty()) {
                continue;
            }

            int moveAmount = Math.min(remaining.getCount(), remaining.getMaxStackSize());
            contents.set(slot, remaining.copyWithCount(moveAmount));
            remaining.shrink(moveAmount);
            changed = true;
        }

        if (changed) {
            save(bag, tier, contents);
        }
        return remaining;
    }

    private static StorageBagTier getTier(ItemStack stack) {
        return stack.getItem() instanceof StorageBagItem storageBagItem
                ? storageBagItem.getTier()
                : StorageBagTier.QI_REFINING;
    }
}
