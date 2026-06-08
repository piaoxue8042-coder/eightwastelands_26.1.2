package com.px8042.eightwastelands.menu;

import com.px8042.eightwastelands.item.fabao.FabaoBagItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.ArrayList;
import java.util.List;

public class FabaoBagInventory extends SimpleContainer {

    private final ItemStack bagStack;
    private boolean loading = true;

    public FabaoBagInventory(ItemStack bagStack) {
        super(FabaoBagItem.SLOT_COUNT);
        this.bagStack = bagStack;

        NonNullList<ItemStack> contents = FabaoBagItem.getContents(bagStack);
        for (int slot = 0; slot < contents.size(); slot++) {
            super.setItem(slot, contents.get(slot));
        }

        this.loading = false;
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (!this.loading) {
            this.saveToStack();
        }
    }

    public void saveToStack() {
        if (this.bagStack.isEmpty()) {
            return;
        }

        List<ItemStack> stacks = new ArrayList<>(FabaoBagItem.SLOT_COUNT);
        for (int slot = 0; slot < FabaoBagItem.SLOT_COUNT; slot++) {
            stacks.add(this.getItem(slot).copy());
        }

        ItemContainerContents contents = ItemContainerContents.fromItems(stacks);
        if (contents.equals(ItemContainerContents.EMPTY)) {
            this.bagStack.remove(DataComponents.CONTAINER);
        } else {
            this.bagStack.set(DataComponents.CONTAINER, contents);
        }
    }
}
