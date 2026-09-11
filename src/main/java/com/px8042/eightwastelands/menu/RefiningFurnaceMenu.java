package com.px8042.eightwastelands.menu;

import com.px8042.eightwastelands.block.custom.RefiningFurnaceBlock;
import com.px8042.eightwastelands.block.entity.RefiningFurnaceBlockEntity;
import com.px8042.eightwastelands.cultivation.CultivationRealm;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Optional;

public class RefiningFurnaceMenu extends AbstractContainerMenu {

    private static final int INPUT_SLOT = RefiningFurnaceBlockEntity.SLOT_INPUT;
    private static final int FUEL_SLOT = RefiningFurnaceBlockEntity.SLOT_FUEL;
    private static final int OUTPUT_SLOT = RefiningFurnaceBlockEntity.SLOT_OUTPUT;
    private static final int PLAYER_INVENTORY_START = RefiningFurnaceBlockEntity.CONTAINER_SIZE;
    private static final int PLAYER_MAIN_INVENTORY_END = PLAYER_INVENTORY_START + 27;
    private static final int PLAYER_INVENTORY_END = PLAYER_INVENTORY_START + 36;

    private final Container container;
    private final ContainerData data;
    private final ContainerLevelAccess access;
    private final Player player;
    private final CultivationRealm maxRealm;
    private final RefiningFurnaceBlockEntity blockEntity;
    private final boolean clearContainerOnRemoved;

    public static RefiningFurnaceMenu fromNetwork(
            int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data
    ) {
        CultivationRealm maxRealm = CultivationRealm.byIndex(data.readInt())
                .orElse(CultivationRealm.QI_REFINING);
        return new RefiningFurnaceMenu(
                containerId,
                playerInventory,
                ContainerLevelAccess.NULL,
                new SimpleContainer(RefiningFurnaceBlockEntity.CONTAINER_SIZE),
                new SimpleContainerData(RefiningFurnaceBlockEntity.DATA_COUNT),
                maxRealm,
                null,
                true
        );
    }

    public RefiningFurnaceMenu(
            int containerId,
            Inventory playerInventory,
            ContainerLevelAccess access,
            RefiningFurnaceBlockEntity blockEntity,
            ContainerData data,
            CultivationRealm maxRealm
    ) {
        this(containerId, playerInventory, access, blockEntity, data, maxRealm, blockEntity, false);
    }

    private RefiningFurnaceMenu(
            int containerId,
            Inventory playerInventory,
            ContainerLevelAccess access,
            Container container,
            ContainerData data,
            CultivationRealm maxRealm,
            RefiningFurnaceBlockEntity blockEntity,
            boolean clearContainerOnRemoved
    ) {
        super(ModMenuTypes.REFINING_FURNACE.get(), containerId);
        checkContainerSize(container, RefiningFurnaceBlockEntity.CONTAINER_SIZE);
        checkContainerDataCount(data, RefiningFurnaceBlockEntity.DATA_COUNT);
        this.container = container;
        this.data = data;
        this.access = access;
        this.player = playerInventory.player;
        this.maxRealm = maxRealm;
        this.blockEntity = blockEntity;
        this.clearContainerOnRemoved = clearContainerOnRemoved;

        addSlot(new RefiningInputSlot(container, INPUT_SLOT, 56, 17));
        addSlot(new RefiningFuelSlot(container, FUEL_SLOT, 56, 53));
        addSlot(new RefiningOutputSlot(container, OUTPUT_SLOT, 116, 35));
        addStandardInventorySlots(playerInventory, 8, 84);
        addDataSlots(data);
    }

    public float getBurnProgress() {
        int total = this.data.get(RefiningFurnaceBlockEntity.DATA_COOKING_TOTAL_TIME);
        int progress = this.data.get(RefiningFurnaceBlockEntity.DATA_COOKING_PROGRESS);
        return total != 0 && progress != 0 ? Mth.clamp((float) progress / total, 0.0F, 1.0F) : 0.0F;
    }

    public float getLitProgress() {
        int duration = this.data.get(RefiningFurnaceBlockEntity.DATA_LIT_DURATION);
        if (duration == 0) {
            duration = 200;
        }
        return Mth.clamp(
                (float) this.data.get(RefiningFurnaceBlockEntity.DATA_LIT_TIME) / duration,
                0.0F,
                1.0F
        );
    }

    public boolean isLit() {
        return this.data.get(RefiningFurnaceBlockEntity.DATA_LIT_TIME) > 0;
    }

    private boolean canPlaceInput(ItemStack stack) {
        Optional<CultivationRealm> realm = RefiningFurnaceBlockEntity.resolveRealm(stack);
        return realm.isPresent() && realm.get().getIndex() <= this.maxRealm.getIndex();
    }

    private boolean canPlaceFuel(ItemStack stack) {
        return RefiningFurnaceBlockEntity.canPlaceFuel(stack, this.player.level());
    }

    private void markLastRefiner() {
        if (this.blockEntity != null) {
            this.blockEntity.setLastRefiner(this.player);
        }
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
        if (slotIndex == OUTPUT_SLOT) {
            if (!moveItemStackTo(sourceStack, PLAYER_INVENTORY_START, PLAYER_INVENTORY_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (slotIndex == INPUT_SLOT || slotIndex == FUEL_SLOT) {
            if (!moveItemStackTo(sourceStack, PLAYER_INVENTORY_START, PLAYER_INVENTORY_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (canPlaceInput(sourceStack)) {
            if (!moveItemStackTo(sourceStack, INPUT_SLOT, INPUT_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (canPlaceFuel(sourceStack)) {
            if (!moveItemStackTo(sourceStack, FUEL_SLOT, FUEL_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (slotIndex < PLAYER_MAIN_INVENTORY_END) {
            if (!moveItemStackTo(sourceStack, PLAYER_MAIN_INVENTORY_END, PLAYER_INVENTORY_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(sourceStack, PLAYER_INVENTORY_START, PLAYER_MAIN_INVENTORY_END, false)) {
            return ItemStack.EMPTY;
        }

        if (sourceStack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (sourceStack.getCount() == copiedStack.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, sourceStack);
        return copiedStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player)
                && this.access.evaluate((level, pos) -> level.getBlockState(pos).getBlock()
                                instanceof RefiningFurnaceBlock
                                && player.distanceToSqr(
                                        pos.getX() + 0.5D,
                                        pos.getY() + 0.5D,
                                        pos.getZ() + 0.5D
                                ) <= 64.0D,
                        true
                );
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (this.clearContainerOnRemoved) {
            clearContainer(player, this.container);
        }
    }

    private class RefiningInputSlot extends Slot {
        RefiningInputSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return canPlaceInput(stack);
        }

        @Override
        public void setChanged() {
            super.setChanged();
            markLastRefiner();
        }
    }

    private class RefiningFuelSlot extends Slot {
        RefiningFuelSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return canPlaceFuel(stack);
        }

        @Override
        public int getMaxStackSize(ItemStack stack) {
            return stack.is(Items.BUCKET) ? 1 : super.getMaxStackSize(stack);
        }

        @Override
        public void setChanged() {
            super.setChanged();
            markLastRefiner();
        }
    }

    private static class RefiningOutputSlot extends Slot {
        RefiningOutputSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
