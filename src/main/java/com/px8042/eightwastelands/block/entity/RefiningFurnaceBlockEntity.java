package com.px8042.eightwastelands.block.entity;

import com.px8042.eightwastelands.block.custom.RefiningFurnaceBlock;
import com.px8042.eightwastelands.cultivation.CultivationManager;
import com.px8042.eightwastelands.cultivation.CultivationRealm;
import com.px8042.eightwastelands.item.cultivation.CultivationGearGrade;
import com.px8042.eightwastelands.item.storage.StorageBagContents;
import com.px8042.eightwastelands.menu.RefiningFurnaceMenu;
import com.px8042.eightwastelands.refining.RefiningFormula;
import com.px8042.eightwastelands.refining.RefiningFormulas;
import com.px8042.eightwastelands.refining.RefiningManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Optional;
import java.util.UUID;

public class RefiningFurnaceBlockEntity extends BaseContainerBlockEntity {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_FUEL = 1;
    public static final int SLOT_OUTPUT = 2;
    public static final int CONTAINER_SIZE = 3;

    public static final int DATA_LIT_TIME = 0;
    public static final int DATA_LIT_DURATION = 1;
    public static final int DATA_COOKING_PROGRESS = 2;
    public static final int DATA_COOKING_TOTAL_TIME = 3;
    public static final int DATA_COUNT = 4;

    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    private int litTime;
    private int litDuration;
    private int cookingProgress;
    private int cookingTotalTime;
    private ItemStack lastInputKey = ItemStack.EMPTY;
    private UUID lastRefinerUuid;
    private CultivationRealm pendingRealm;
    private CultivationGearGrade pendingGrade;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int dataId) {
            return switch (dataId) {
                case DATA_LIT_TIME -> RefiningFurnaceBlockEntity.this.litTime;
                case DATA_LIT_DURATION -> RefiningFurnaceBlockEntity.this.litDuration;
                case DATA_COOKING_PROGRESS -> RefiningFurnaceBlockEntity.this.cookingProgress;
                case DATA_COOKING_TOTAL_TIME -> RefiningFurnaceBlockEntity.this.cookingTotalTime;
                default -> 0;
            };
        }

        @Override
        public void set(int dataId, int value) {
            switch (dataId) {
                case DATA_LIT_TIME -> RefiningFurnaceBlockEntity.this.litTime = value;
                case DATA_LIT_DURATION -> RefiningFurnaceBlockEntity.this.litDuration = value;
                case DATA_COOKING_PROGRESS -> RefiningFurnaceBlockEntity.this.cookingProgress = value;
                case DATA_COOKING_TOTAL_TIME -> RefiningFurnaceBlockEntity.this.cookingTotalTime = value;
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return DATA_COUNT;
        }
    };

    public RefiningFurnaceBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.REFINING_FURNACE.get(), worldPosition, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RefiningFurnaceBlockEntity entity) {
        if (level instanceof ServerLevel serverLevel && entity.tickServer(serverLevel)) {
            setChanged(level, pos, state);
        }
    }

    public void setLastRefiner(Player player) {
        this.lastRefinerUuid = player.getUUID();
        setChanged();
    }

    public ContainerData getDataAccess() {
        return this.dataAccess;
    }

    public CultivationRealm getMaxRealm() {
        if (getBlockState().getBlock() instanceof RefiningFurnaceBlock refiningFurnaceBlock) {
            return refiningFurnaceBlock.getMaxRealm();
        }
        return CultivationRealm.QI_REFINING;
    }

    public boolean canFurnaceRefine(CultivationRealm realm) {
        return realm.getIndex() <= getMaxRealm().getIndex();
    }

    private boolean tickServer(ServerLevel level) {
        boolean changed = false;
        ItemStack input = this.items.get(SLOT_INPUT);
        if (updateInputKey(input)) {
            changed = true;
        }

        if (this.litTime > 0) {
            this.litTime--;
            changed = true;
        }

        Optional<RefiningFormula> formula = RefiningFormulas.find(input);
        if (formula.isEmpty() || !canFurnaceRefine(formula.get().realm())) {
            return resetWorkingState() || changed;
        }

        int totalTime = formula.get().cookingTime();
        if (this.cookingTotalTime != totalTime) {
            this.cookingTotalTime = totalTime;
            changed = true;
        }

        ServerPlayer refiner = getLastRefiner(level);
        if (refiner == null || !RefiningManager.canRefine(refiner, formula.get().realm())) {
            return coolCookingProgress() || changed;
        }

        if (!ensurePendingGrade(refiner, formula.get().realm())) {
            return coolCookingProgress() || changed;
        }

        if (!canAcceptResult() && !StorageBagContents.hasEmptySlotInPlayerBags(refiner.getInventory())) {
            return coolCookingProgress() || changed;
        }

        if (this.litTime <= 0 && !consumeFuel(level)) {
            return coolCookingProgress() || changed;
        }

        this.cookingProgress++;
        changed = true;
        if (this.cookingProgress >= this.cookingTotalTime) {
            completePendingRecipe(refiner, formula.get());
        }
        return changed;
    }

    private ServerPlayer getLastRefiner(ServerLevel level) {
        return this.lastRefinerUuid == null ? null : level.getServer().getPlayerList().getPlayer(this.lastRefinerUuid);
    }

    private boolean ensurePendingGrade(ServerPlayer player, CultivationRealm realm) {
        if (this.pendingRealm == realm && this.pendingGrade != null) {
            return true;
        }
        Optional<CultivationGearGrade> grade = RefiningManager.rollGearGrade(player, realm);
        if (grade.isEmpty()) {
            return false;
        }
        this.pendingRealm = realm;
        this.pendingGrade = grade.get();
        return true;
    }

    private void completePendingRecipe(ServerPlayer player, RefiningFormula formula) {
        if (this.pendingGrade == null || this.pendingRealm == null) {
            return;
        }

        ItemStack result = formula.createResult(this.pendingGrade, player.getRandom());
        if (result.isEmpty()) {
            return;
        }

        ItemStack simulatedRemaining = StorageBagContents.simulateInsertIntoPlayerBags(
                player.getInventory(),
                result
        );
        if (!simulatedRemaining.isEmpty() && !canAcceptResult()) {
            return;
        }

        ItemStack remaining = StorageBagContents.insertIntoPlayerBags(player.getInventory(), result);
        if (!remaining.isEmpty() && !canAcceptResult()) {
            return;
        }

        if (!remaining.isEmpty()) {
            this.items.set(SLOT_OUTPUT, remaining);
        }
        this.items.get(SLOT_INPUT).shrink(1);
        RefiningManager.RefiningProgressResult progress = RefiningManager.recordGearRefined(
                player,
                formula.realm(),
                this.pendingGrade
        );
        CultivationManager.syncTo(player);
        player.sendSystemMessage(Component.translatable(
                "message.eightwastelands.refining.refined",
                result.getHoverName(),
                this.pendingGrade.getDisplayName(),
                progress.gainedExperience(),
                progress.newExperience(),
                progress.requiredExperience()
        ));
        if (progress.advanced()) {
            player.sendSystemMessage(Component.translatable(
                    "message.eightwastelands.refining.advanced",
                    progress.newRealm().getDisplayName(),
                    progress.newMastery().getDisplayName()
            ));
        }

        resetPendingRecipe();
        this.cookingProgress = 0;
        this.cookingTotalTime = getCookingTime(this.items.get(SLOT_INPUT));
    }

    private boolean consumeFuel(ServerLevel level) {
        ItemStack fuel = this.items.get(SLOT_FUEL);
        int burnDuration = getFuelBurnDuration(fuel, level);
        if (burnDuration <= 0) {
            return false;
        }

        this.litTime = burnDuration;
        this.litDuration = burnDuration;
        ItemStackTemplate remainder = fuel.getCraftingRemainder();
        fuel.shrink(1);
        if (fuel.isEmpty()) {
            this.items.set(SLOT_FUEL, remainder == null ? ItemStack.EMPTY : remainder.create());
        }
        return true;
    }

    private boolean canAcceptResult() {
        return this.items.get(SLOT_OUTPUT).isEmpty();
    }

    private boolean updateInputKey(ItemStack input) {
        ItemStack currentKey = input.isEmpty() ? ItemStack.EMPTY : input.copyWithCount(1);
        if (sameItemKey(this.lastInputKey, currentKey)) {
            return false;
        }
        this.lastInputKey = currentKey.isEmpty() ? ItemStack.EMPTY : currentKey.copy();
        resetPendingRecipe();
        this.cookingProgress = 0;
        this.cookingTotalTime = getCookingTime(input);
        return true;
    }

    private boolean coolCookingProgress() {
        if (this.cookingProgress <= 0) {
            return false;
        }
        this.cookingProgress = Mth.clamp(this.cookingProgress - 2, 0, this.cookingTotalTime);
        return true;
    }

    private boolean resetWorkingState() {
        boolean changed = this.pendingRealm != null || this.pendingGrade != null
                || this.cookingProgress != 0 || this.cookingTotalTime != 0;
        resetPendingRecipe();
        this.cookingProgress = 0;
        this.cookingTotalTime = 0;
        return changed;
    }

    private void resetPendingRecipe() {
        this.pendingRealm = null;
        this.pendingGrade = null;
    }

    public static Optional<CultivationRealm> resolveRealm(ItemStack stack) {
        return RefiningFormulas.find(stack).map(RefiningFormula::realm);
    }

    public static int getCookingTime(ItemStack stack) {
        return RefiningFormulas.find(stack).map(RefiningFormula::cookingTime).orElse(0);
    }

    public static int getFuelBurnDuration(ItemStack stack, Level level) {
        return stack.isEmpty() ? 0 : stack.getBurnTime(RecipeType.SMELTING, level.fuelValues());
    }

    public static boolean canPlaceFuel(ItemStack stack, Level level) {
        return getFuelBurnDuration(stack, level) > 0 || stack.is(Items.BUCKET);
    }

    private static boolean sameItemKey(ItemStack first, ItemStack second) {
        if (first.isEmpty() && second.isEmpty()) {
            return true;
        }
        return !first.isEmpty() && !second.isEmpty() && ItemStack.isSameItemSameComponents(first, second);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == SLOT_INPUT) {
            return resolveRealm(stack).filter(this::canFurnaceRefine).isPresent();
        }
        if (slot == SLOT_FUEL && this.level != null) {
            return canPlaceFuel(stack, this.level);
        }
        return false;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.items);
        this.litTime = input.getIntOr("lit_time_remaining", 0);
        this.litDuration = input.getIntOr("lit_total_time", 0);
        this.cookingProgress = input.getIntOr("cooking_time_spent", 0);
        this.cookingTotalTime = input.getIntOr("cooking_total_time", 0);
        String uuidText = input.getStringOr("last_refiner_uuid", "");
        try {
            this.lastRefinerUuid = uuidText.isBlank() ? null : UUID.fromString(uuidText);
        } catch (IllegalArgumentException ignored) {
            this.lastRefinerUuid = null;
        }
        this.pendingRealm = CultivationRealm.byIndex(input.getIntOr("pending_realm", -1)).orElse(null);
        this.pendingGrade = CultivationGearGrade.bySerializedName(input.getStringOr("pending_grade", "")).orElse(null);
        this.lastInputKey = ItemStack.EMPTY;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);
        output.putInt("lit_time_remaining", this.litTime);
        output.putInt("lit_total_time", this.litDuration);
        output.putInt("cooking_time_spent", this.cookingProgress);
        output.putInt("cooking_total_time", this.cookingTotalTime);
        output.putString("last_refiner_uuid", this.lastRefinerUuid == null ? "" : this.lastRefinerUuid.toString());
        output.putInt("pending_realm", this.pendingRealm == null ? -1 : this.pendingRealm.getIndex());
        output.putString("pending_grade", this.pendingGrade == null ? "" : this.pendingGrade.getSerializedName());
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        if (inventory.player != null) {
            setLastRefiner(inventory.player);
        }
        return new RefiningFurnaceMenu(
                containerId,
                inventory,
                ContainerLevelAccess.create(this.level, this.worldPosition),
                this,
                this.dataAccess,
                getMaxRealm()
        );
    }
}
