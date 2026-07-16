package com.px8042.eightwastelands.menu;

import com.px8042.eightwastelands.block.entity.AlchemyFurnaceBlockEntity;
import com.px8042.eightwastelands.block.custom.AlchemyFurnaceBlock;
import com.px8042.eightwastelands.cultivation.CultivationRealm;
import java.util.Optional;
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

public class AlchemyFurnaceMenu extends AbstractContainerMenu {

    private static final int INPUT_SLOT = AlchemyFurnaceBlockEntity.SLOT_INPUT;
    private static final int FUEL_SLOT = AlchemyFurnaceBlockEntity.SLOT_FUEL;
    private static final int OUTPUT_SLOT_START = AlchemyFurnaceBlockEntity.OUTPUT_SLOT_START;
    private static final int OUTPUT_SLOT_COUNT = AlchemyFurnaceBlockEntity.OUTPUT_SLOT_COUNT;
    private static final int OUTPUT_SLOT_END = OUTPUT_SLOT_START + OUTPUT_SLOT_COUNT;
    private static final int PLAYER_INVENTORY_START = AlchemyFurnaceBlockEntity.CONTAINER_SIZE;
    private static final int PLAYER_MAIN_INVENTORY_END = PLAYER_INVENTORY_START + 27;
    private static final int PLAYER_INVENTORY_END = PLAYER_INVENTORY_START + 36;

    private static final int INPUT_SLOT_X = 56;
    private static final int INPUT_SLOT_Y = 17;
    private static final int FUEL_SLOT_X = 56;
    private static final int FUEL_SLOT_Y = 53;
    private static final int OUTPUT_SLOT_START_X = 116;
    private static final int OUTPUT_SLOT_START_Y = 25;
    private static final int SLOT_SPACING = 18;
    private static final int PLAYER_INVENTORY_X = 8;
    private static final int PLAYER_INVENTORY_Y = 84;

    private final Container container;
    private final ContainerData data;
    private final ContainerLevelAccess access;
    private final Player player;
    private final CultivationRealm maxRealm;
    private final AlchemyFurnaceBlockEntity blockEntity;
    private final boolean clearContainerOnRemoved;

    public static AlchemyFurnaceMenu fromNetwork(
            int containerId,
            Inventory playerInventory,
            RegistryFriendlyByteBuf data
    ) {
        CultivationRealm maxRealm = CultivationRealm.byIndex(data.readInt())
                .orElse(CultivationRealm.QI_REFINING);

        return new AlchemyFurnaceMenu(
                containerId,
                playerInventory,
                ContainerLevelAccess.NULL,
                new SimpleContainer(AlchemyFurnaceBlockEntity.CONTAINER_SIZE),
                new SimpleContainerData(AlchemyFurnaceBlockEntity.DATA_COUNT),
                maxRealm,
                null,
                true
        );
    }

    public AlchemyFurnaceMenu(
            int containerId,
            Inventory playerInventory,
            ContainerLevelAccess access,
            AlchemyFurnaceBlockEntity blockEntity,
            ContainerData data,
            CultivationRealm maxRealm
    ) {
        this(
                containerId,
                playerInventory,
                access,
                blockEntity,
                data,
                maxRealm,
                blockEntity,
                false
        );
    }

    private AlchemyFurnaceMenu(
            int containerId,
            Inventory playerInventory,
            ContainerLevelAccess access,
            Container container,
            ContainerData data,
            CultivationRealm maxRealm,
            AlchemyFurnaceBlockEntity blockEntity,
            boolean clearContainerOnRemoved
    ) {
        super(ModMenuTypes.ALCHEMY_FURNACE.get(), containerId);
        checkContainerSize(container, AlchemyFurnaceBlockEntity.CONTAINER_SIZE);
        checkContainerDataCount(data, AlchemyFurnaceBlockEntity.DATA_COUNT);

        this.container = container;
        this.data = data;
        this.access = access;
        this.player = playerInventory.player;
        this.maxRealm = maxRealm;
        this.blockEntity = blockEntity;
        this.clearContainerOnRemoved = clearContainerOnRemoved;

        this.addSlot(new AlchemyInputSlot(this.container, INPUT_SLOT, INPUT_SLOT_X, INPUT_SLOT_Y));
        this.addSlot(new AlchemyFuelSlot(this.container, FUEL_SLOT, FUEL_SLOT_X, FUEL_SLOT_Y));

        for (int slot = 0; slot < OUTPUT_SLOT_COUNT; slot++) {
            int column = slot % 2;
            int row = slot / 2;
            int x = OUTPUT_SLOT_START_X + column * SLOT_SPACING;
            int y = OUTPUT_SLOT_START_Y + row * SLOT_SPACING;
            this.addSlot(new AlchemyOutputSlot(this.container, OUTPUT_SLOT_START + slot, x, y));
        }

        this.addStandardInventorySlots(playerInventory, PLAYER_INVENTORY_X, PLAYER_INVENTORY_Y);
        this.addDataSlots(this.data);
    }

    private void markLastAlchemist() {
        if (this.blockEntity != null) {
            this.blockEntity.setLastAlchemist(this.player);
        }
    }

    private boolean canPlaceInput(ItemStack stack) {
        Optional<CultivationRealm> realm = AlchemyFurnaceBlockEntity.resolveRealm(stack);

        return realm.isPresent() && realm.get().getIndex() <= this.maxRealm.getIndex();
    }

    private boolean canPlaceFuel(ItemStack stack) {
        return AlchemyFurnaceBlockEntity.canPlaceFuel(stack, this.player.level());
    }

    public float getBurnProgress() {
        int cookingTotalTime = this.data.get(AlchemyFurnaceBlockEntity.DATA_COOKING_TOTAL_TIME);
        int cookingProgress = this.data.get(AlchemyFurnaceBlockEntity.DATA_COOKING_PROGRESS);

        return cookingTotalTime != 0 && cookingProgress != 0
                ? Mth.clamp((float) cookingProgress / cookingTotalTime, 0.0F, 1.0F)
                : 0.0F;
    }

    public float getLitProgress() {
        int litDuration = this.data.get(AlchemyFurnaceBlockEntity.DATA_LIT_DURATION);
        if (litDuration == 0) {
            litDuration = 200;
        }

        return Mth.clamp((float) this.data.get(AlchemyFurnaceBlockEntity.DATA_LIT_TIME) / litDuration, 0.0F, 1.0F);
    }

    public boolean isLit() {
        return this.data.get(AlchemyFurnaceBlockEntity.DATA_LIT_TIME) > 0;
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

        if (slotIndex >= OUTPUT_SLOT_START && slotIndex < OUTPUT_SLOT_END) {
            if (!this.moveItemStackTo(sourceStack, PLAYER_INVENTORY_START, PLAYER_INVENTORY_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (slotIndex == INPUT_SLOT || slotIndex == FUEL_SLOT) {
            if (!this.moveItemStackTo(sourceStack, PLAYER_INVENTORY_START, PLAYER_INVENTORY_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (canPlaceInput(sourceStack)) {
            if (!this.moveItemStackTo(sourceStack, INPUT_SLOT, INPUT_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (canPlaceFuel(sourceStack)) {
            if (!this.moveItemStackTo(sourceStack, FUEL_SLOT, FUEL_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (slotIndex >= PLAYER_INVENTORY_START && slotIndex < PLAYER_MAIN_INVENTORY_END) {
            if (!this.moveItemStackTo(sourceStack, PLAYER_MAIN_INVENTORY_END, PLAYER_INVENTORY_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (slotIndex >= PLAYER_MAIN_INVENTORY_END
                && slotIndex < PLAYER_INVENTORY_END
                && !this.moveItemStackTo(sourceStack, PLAYER_INVENTORY_START, PLAYER_MAIN_INVENTORY_END, false)) {
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
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.container || slot.index < OUTPUT_SLOT_START || super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player)
                && this.access.evaluate((level, pos) -> level.getBlockState(pos).getBlock()
                                instanceof AlchemyFurnaceBlock
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
            this.clearContainer(player, this.container);
        }
    }

    private class AlchemyInputSlot extends Slot {

        AlchemyInputSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return canPlaceInput(stack);
        }

        @Override
        public void setChanged() {
            super.setChanged();
            markLastAlchemist();
        }
    }

    private class AlchemyFuelSlot extends Slot {

        AlchemyFuelSlot(Container container, int slot, int x, int y) {
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
            markLastAlchemist();
        }
    }

    private static class AlchemyOutputSlot extends Slot {

        AlchemyOutputSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
