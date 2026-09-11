package com.px8042.eightwastelands.item.storage;

import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class StorageBagContainer extends SimpleContainer {

    private final ItemStack storageBag;
    private final StorageBagTier tier;
    private final Inventory ownerInventory;
    private boolean loading;

    public StorageBagContainer(ItemStack storageBag, StorageBagTier tier, Inventory ownerInventory) {
        super(tier.getMenuSlotCount());
        this.storageBag = storageBag;
        this.tier = tier;
        this.ownerInventory = ownerInventory;
        this.loading = true;

        NonNullList<ItemStack> contents = StorageBagContents.load(storageBag, tier);
        for (int slot = 0; slot < contents.size(); slot++) {
            super.setItem(slot, contents.get(slot));
        }
        this.loading = false;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot >= 0 && slot < this.tier.getCapacity() && !StorageBagContents.isStorageBag(stack);
    }

    @Override
    public boolean stillValid(Player player) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            if (player.getInventory().getItem(slot) == this.storageBag) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.loading) {
            return;
        }

        StorageBagContents.save(this.storageBag, this.tier, this.getItems());
        this.ownerInventory.setChanged();
    }
}
