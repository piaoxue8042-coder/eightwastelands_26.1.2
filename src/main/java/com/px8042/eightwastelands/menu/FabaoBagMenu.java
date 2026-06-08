package com.px8042.eightwastelands.menu;

import com.px8042.eightwastelands.item.fabao.FabaoBagItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FabaoBagMenu extends AbstractContainerMenu {

    private static final int BAG_SLOT_COUNT = FabaoBagItem.SLOT_COUNT;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = 36;
    private static final int PLAYER_INVENTORY_START = BAG_SLOT_COUNT;
    private static final int PLAYER_INVENTORY_END = PLAYER_INVENTORY_START + PLAYER_INVENTORY_SLOT_COUNT;

    private final Container bagContainer;
    private final ItemStack bagStack;

    public static FabaoBagMenu fromNetwork(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        return new FabaoBagMenu(containerId, playerInventory, new SimpleContainer(BAG_SLOT_COUNT), ItemStack.EMPTY);
    }

    public FabaoBagMenu(int containerId, Inventory playerInventory, ItemStack bagStack) {
        this(containerId, playerInventory, new FabaoBagInventory(bagStack), bagStack);
    }

    private FabaoBagMenu(int containerId, Inventory playerInventory, Container bagContainer, ItemStack bagStack) {
        super(ModMenuTypes.FABAO_BAG.get(), containerId);
        this.bagContainer = bagContainer;
        this.bagStack = bagStack;

        checkContainerSize(bagContainer, BAG_SLOT_COUNT);
        bagContainer.startOpen(playerInventory.player);

        for (int slot = 0; slot < BAG_SLOT_COUNT; slot++) {
            this.addSlot(new Slot(bagContainer, slot, 35 + slot * 18, 18) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return FabaoBagItem.isValidFabaoStack(stack);
                }

                @Override
                public int getMaxStackSize() {
                    return 1;
                }
            });
        }

        this.addStandardInventorySlots(playerInventory, 8, 50);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        if (slotIndex < 0 || slotIndex >= this.slots.size()) {
            return ItemStack.EMPTY;
        }

        Slot slot = this.slots.get(slotIndex);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack = slot.getItem();
        ItemStack copiedStack = sourceStack.copy();

        if (slotIndex < BAG_SLOT_COUNT) {
            if (!this.moveItemStackTo(sourceStack, PLAYER_INVENTORY_START, PLAYER_INVENTORY_END, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!FabaoBagItem.isValidFabaoStack(sourceStack)) {
                return ItemStack.EMPTY;
            }

            if (!this.moveItemStackTo(sourceStack, 0, BAG_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (sourceStack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return copiedStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.bagContainer.stopOpen(player);

        if (this.bagContainer instanceof FabaoBagInventory fabaoBagInventory) {
            fabaoBagInventory.saveToStack();
        }
    }

    public ItemStack getBagStack() {
        return this.bagStack;
    }
}
