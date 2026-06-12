package com.px8042.eightwastelands.menu;

import com.px8042.eightwastelands.block.ModBlocks;
import com.px8042.eightwastelands.recipe.CraftingVariantResolver;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WealthTableMenu extends AbstractContainerMenu {

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT_START = 1;
    private static final int OUTPUT_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_START = OUTPUT_SLOT_START + OUTPUT_SLOT_COUNT;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = 36;
    private static final int PLAYER_INVENTORY_END = PLAYER_INVENTORY_START + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int MAX_DECOMPOSITION_VARIANTS = 64;

    public static final int BUTTON_PREVIOUS_VARIANT = 0;
    public static final int BUTTON_NEXT_VARIANT = 1;

    private static final int INPUT_SLOT_X = 30;
    private static final int INPUT_SLOT_Y = 35;
    private static final int OUTPUT_SLOT_START_X = 98;
    private static final int OUTPUT_SLOT_START_Y = 17;
    private static final int SLOT_SPACING = 18;
    private static final int PLAYER_INVENTORY_X = 8;
    private static final int PLAYER_INVENTORY_Y = 84;

    private final SimpleContainer inputContainer = new SimpleContainer(1);
    private final SimpleContainer outputContainer = new SimpleContainer(OUTPUT_SLOT_COUNT);
    private final ContainerLevelAccess access;
    private final Player player;

    private List<CraftingVariantResolver.DecompositionVariant> decompositionVariants = List.of();
    private CraftingVariantResolver.DecompositionVariant currentDecomposition;
    private int selectedVariantIndex;
    private int variantCount;
    private boolean outputsPaid;
    private boolean updatingInput;

    public static WealthTableMenu fromNetwork(
            int containerId,
            Inventory playerInventory,
            RegistryFriendlyByteBuf data
    ) {
        return new WealthTableMenu(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public WealthTableMenu(
            int containerId,
            Inventory playerInventory,
            ContainerLevelAccess access
    ) {
        super(ModMenuTypes.WEALTH_TABLE.get(), containerId);
        this.access = access;
        this.player = playerInventory.player;

        this.addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return WealthTableMenu.this.selectedVariantIndex;
            }

            @Override
            public void set(int value) {
                WealthTableMenu.this.selectedVariantIndex = value;
            }
        });

        this.addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return WealthTableMenu.this.variantCount;
            }

            @Override
            public void set(int value) {
                WealthTableMenu.this.variantCount = value;
            }
        });

        this.addSlot(new Slot(this.inputContainer, INPUT_SLOT, INPUT_SLOT_X, INPUT_SLOT_Y) {
            @Override
            public void setChanged() {
                super.setChanged();
                WealthTableMenu.this.onInputChanged();
            }
        });

        for (int slot = 0; slot < OUTPUT_SLOT_COUNT; slot++) {
            int column = slot % 3;
            int row = slot / 3;
            int x = OUTPUT_SLOT_START_X + column * SLOT_SPACING;
            int y = OUTPUT_SLOT_START_Y + row * SLOT_SPACING;

            this.addSlot(new WealthTableOutputSlot(this.outputContainer, slot, x, y));
        }

        this.addStandardInventorySlots(playerInventory, PLAYER_INVENTORY_X, PLAYER_INVENTORY_Y);
        updateOutputs();
    }

    private void onInputChanged() {
        if (this.updatingInput || this.outputsPaid) {
            return;
        }

        this.selectedVariantIndex = 0;
        updateOutputs();
    }

    private void updateOutputs() {
        if (this.outputsPaid) {
            return;
        }

        ItemStack input = this.inputContainer.getItem(INPUT_SLOT);
        this.decompositionVariants = findDecompositions(input);
        this.variantCount = this.decompositionVariants.size();

        if (this.selectedVariantIndex >= this.variantCount) {
            this.selectedVariantIndex = 0;
        }

        this.currentDecomposition = this.variantCount == 0
                ? null
                : this.decompositionVariants.get(this.selectedVariantIndex);
        clearOutputs();

        if (this.currentDecomposition == null) {
            broadcastChanges();
            return;
        }

        for (int slot = 0; slot < OUTPUT_SLOT_COUNT; slot++) {
            this.outputContainer.setItem(slot, this.currentDecomposition.outputs().get(slot).copy());
        }

        broadcastChanges();
    }

    private List<CraftingVariantResolver.DecompositionVariant> findDecompositions(ItemStack input) {
        Level level = this.player.level();

        return CraftingVariantResolver.findDecompositionVariants(level, input, MAX_DECOMPOSITION_VARIANTS);
    }

    public int getSelectedVariantIndex() {
        return this.selectedVariantIndex;
    }

    public int getVariantCount() {
        return this.variantCount;
    }

    public boolean hasMultipleVariants() {
        return this.variantCount > 1;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == BUTTON_PREVIOUS_VARIANT) {
            return cycleVariant(-1);
        }

        if (id == BUTTON_NEXT_VARIANT) {
            return cycleVariant(1);
        }

        return super.clickMenuButton(player, id);
    }

    private boolean cycleVariant(int direction) {
        if (this.outputsPaid) {
            return false;
        }

        updateOutputs();

        if (this.variantCount <= 1) {
            return false;
        }

        this.selectedVariantIndex = Math.floorMod(this.selectedVariantIndex + direction, this.variantCount);
        updateOutputs();

        return true;
    }

    private boolean canPayForOutputs() {
        if (this.currentDecomposition == null) {
            return false;
        }

        ItemStack input = this.inputContainer.getItem(INPUT_SLOT);

        return CraftingVariantResolver.isValidInputForCurrentVariant(input, this.currentDecomposition)
                && input.getCount() >= this.currentDecomposition.inputCost();
    }

    private boolean payForOutputs() {
        if (this.outputsPaid) {
            return true;
        }

        if (!canPayForOutputs()) {
            clearOutputs();
            this.currentDecomposition = null;
            broadcastChanges();
            return false;
        }

        this.updatingInput = true;
        this.inputContainer.removeItem(INPUT_SLOT, this.currentDecomposition.inputCost());
        this.updatingInput = false;

        giveExtraOutputsToPlayer(this.currentDecomposition.extraOutputs());
        this.outputsPaid = true;
        broadcastChanges();

        return true;
    }

    private void giveExtraOutputsToPlayer(List<ItemStack> extraOutputs) {
        for (ItemStack extraOutput : extraOutputs) {
            if (extraOutput.isEmpty()) {
                continue;
            }

            giveItemToPlayer(extraOutput);
        }
    }

    private boolean canTakeOutput() {
        return this.outputsPaid || canPayForOutputs();
    }

    private void outputTaken() {
        if (!payForOutputs()) {
            return;
        }

        giveVisibleOutputsToPlayer();
        this.outputsPaid = false;
        this.currentDecomposition = null;
        updateOutputs();
    }

    private void giveVisibleOutputsToPlayer() {
        for (int slot = 0; slot < OUTPUT_SLOT_COUNT; slot++) {
            ItemStack output = this.outputContainer.removeItemNoUpdate(slot);

            if (!output.isEmpty()) {
                giveItemToPlayer(output);
            }
        }

        this.outputContainer.setChanged();
    }

    private void giveItemToPlayer(ItemStack stack) {
        ItemStack remaining = stack.copy();

        if (!this.player.getInventory().add(remaining)) {
            this.player.drop(remaining, false);
        }
    }

    private void clearOutputs() {
        for (int slot = 0; slot < OUTPUT_SLOT_COUNT; slot++) {
            this.outputContainer.setItem(slot, ItemStack.EMPTY);
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

        if (slotIndex == INPUT_SLOT) {
            if (!this.moveItemStackTo(sourceStack, PLAYER_INVENTORY_START, PLAYER_INVENTORY_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (slotIndex >= OUTPUT_SLOT_START && slotIndex < PLAYER_INVENTORY_START) {
            if (!slot.mayPickup(player)) {
                return ItemStack.EMPTY;
            }

            if (!this.moveItemStackTo(sourceStack, PLAYER_INVENTORY_START, PLAYER_INVENTORY_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(sourceStack, INPUT_SLOT, INPUT_SLOT + 1, false)) {
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
        return slot.container != this.outputContainer && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.WEALTH_TABLE.get());
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.clearContainer(player, this.inputContainer);

        if (this.outputsPaid) {
            this.clearContainer(player, this.outputContainer);
        } else {
            this.outputContainer.clearContent();
        }
    }

    private class WealthTableOutputSlot extends Slot {

        WealthTableOutputSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player player) {
            return this.hasItem() && canTakeOutput();
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            super.onTake(player, stack);
            outputTaken();
        }
    }
}
