package com.px8042.eightwastelands.block.entity;

import com.px8042.eightwastelands.alchemy.AlchemyFormula;
import com.px8042.eightwastelands.alchemy.AlchemyFormulas;
import com.px8042.eightwastelands.alchemy.AlchemyManager;
import com.px8042.eightwastelands.block.custom.AlchemyFurnaceBlock;
import com.px8042.eightwastelands.cultivation.CultivationManager;
import com.px8042.eightwastelands.cultivation.CultivationRealm;
import com.px8042.eightwastelands.item.pill.PillGrade;
import com.px8042.eightwastelands.item.storage.StorageBagContents;
import com.px8042.eightwastelands.menu.AlchemyFurnaceMenu;
import java.util.Optional;
import java.util.UUID;
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

public class AlchemyFurnaceBlockEntity extends BaseContainerBlockEntity {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_FUEL = 1;
    public static final int SLOT_OUTPUT_LOW = 2;
    public static final int SLOT_OUTPUT_MIDDLE = 3;
    public static final int SLOT_OUTPUT_HIGH = 4;
    public static final int SLOT_OUTPUT_IMMORTAL = 5;
    public static final int OUTPUT_SLOT_START = SLOT_OUTPUT_LOW;
    public static final int OUTPUT_SLOT_COUNT = 4;
    public static final int CONTAINER_SIZE = 6;

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
    private UUID lastAlchemistUuid;
    private String lastAlchemistName = "";
    private CultivationRealm pendingRealm;
    private PillGrade pendingGrade;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int dataId) {
            return switch (dataId) {
                case DATA_LIT_TIME -> AlchemyFurnaceBlockEntity.this.litTime;
                case DATA_LIT_DURATION -> AlchemyFurnaceBlockEntity.this.litDuration;
                case DATA_COOKING_PROGRESS -> AlchemyFurnaceBlockEntity.this.cookingProgress;
                case DATA_COOKING_TOTAL_TIME -> AlchemyFurnaceBlockEntity.this.cookingTotalTime;
                default -> 0;
            };
        }

        @Override
        public void set(int dataId, int value) {
            switch (dataId) {
                case DATA_LIT_TIME -> AlchemyFurnaceBlockEntity.this.litTime = value;
                case DATA_LIT_DURATION -> AlchemyFurnaceBlockEntity.this.litDuration = value;
                case DATA_COOKING_PROGRESS -> AlchemyFurnaceBlockEntity.this.cookingProgress = value;
                case DATA_COOKING_TOTAL_TIME -> AlchemyFurnaceBlockEntity.this.cookingTotalTime = value;
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return DATA_COUNT;
        }
    };

    public AlchemyFurnaceBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.ALCHEMY_FURNACE.get(), worldPosition, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AlchemyFurnaceBlockEntity entity) {
        if (level instanceof ServerLevel serverLevel && entity.tickServer(serverLevel)) {
            setChanged(level, pos, state);
        }
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

        Optional<AlchemyFormula> formula = AlchemyFormulas.find(input);
        if (formula.isEmpty() || !canFurnaceRefine(formula.get().realm())) {
            return resetWorkingState() || changed;
        }

        int totalTime = formula.get().cookingTime();
        if (this.cookingTotalTime != totalTime) {
            this.cookingTotalTime = totalTime;
            changed = true;
        }

        if (totalTime <= 0) {
            return coolCookingProgress() || changed;
        }

        ServerPlayer alchemist = getLastAlchemist(level);
        if (alchemist == null) {
            return changed;
        }

        if (!ensurePendingRecipe(alchemist, formula.get().realm())) {
            return coolCookingProgress() || changed;
        }

        ItemStack result = formula.get().createResult(this.pendingGrade);
        if (result.isEmpty()) {
            return changed;
        }

        ItemStack simulatedRemaining = StorageBagContents.simulateInsertIntoPlayerBags(
                alchemist.getInventory(),
                result
        );
        if (!simulatedRemaining.isEmpty() && !canAcceptResult(this.pendingGrade, simulatedRemaining)) {
            return coolCookingProgress() || changed;
        }

        if (this.litTime <= 0 && !consumeFuel(level)) {
            return coolCookingProgress() || changed;
        }

        this.cookingProgress++;
        changed = true;

        if (this.cookingProgress >= this.cookingTotalTime) {
            completePendingRecipe(alchemist, formula.get(), result);
        }

        return changed;
    }

    public void setLastAlchemist(Player player) {
        this.lastAlchemistUuid = player.getUUID();
        this.lastAlchemistName = player.getName().getString();
        setChanged();
    }

    public ContainerData getDataAccess() {
        return this.dataAccess;
    }

    public CultivationRealm getMaxRealm() {
        if (this.getBlockState().getBlock() instanceof AlchemyFurnaceBlock alchemyFurnaceBlock) {
            return alchemyFurnaceBlock.getMaxRealm();
        }

        return CultivationRealm.QI_REFINING;
    }

    public boolean canFurnaceRefine(CultivationRealm realm) {
        return realm.getIndex() <= getMaxRealm().getIndex();
    }

    private ServerPlayer getLastAlchemist(ServerLevel level) {
        if (this.lastAlchemistUuid == null) {
            return null;
        }

        return level.getServer().getPlayerList().getPlayer(this.lastAlchemistUuid);
    }

    private boolean ensurePendingRecipe(ServerPlayer serverPlayer, CultivationRealm realm) {
        if (this.pendingRealm == realm && this.pendingGrade != null) {
            return true;
        }

        Optional<PillGrade> rolledGrade = AlchemyManager.rollPillGrade(serverPlayer, realm);
        if (rolledGrade.isEmpty() || rolledGrade.get() == PillGrade.NONE) {
            return false;
        }

        this.pendingRealm = realm;
        this.pendingGrade = rolledGrade.get();
        return true;
    }

    private void completePendingRecipe(ServerPlayer serverPlayer, AlchemyFormula formula, ItemStack result) {
        if (this.pendingRealm == null || this.pendingGrade == null) {
            return;
        }

        int outputIndex = outputIndexFor(this.pendingGrade);
        if (outputIndex < 0) {
            return;
        }

        ItemStack simulatedRemaining = StorageBagContents.simulateInsertIntoPlayerBags(
                serverPlayer.getInventory(),
                result
        );
        if (!simulatedRemaining.isEmpty() && !canAcceptResult(this.pendingGrade, simulatedRemaining)) {
            return;
        }

        ItemStack remaining = StorageBagContents.insertIntoPlayerBags(serverPlayer.getInventory(), result);
        if (!remaining.isEmpty() && !canAcceptResult(this.pendingGrade, remaining)) {
            return;
        }

        if (!remaining.isEmpty()) {
            ItemStack output = this.items.get(outputIndex);
            if (output.isEmpty()) {
                this.items.set(outputIndex, remaining.copy());
            } else {
                output.grow(remaining.getCount());
            }
        }

        this.items.get(SLOT_INPUT).shrink(1);

        AlchemyManager.AlchemyProgressResult progress =
                AlchemyManager.recordPillRefined(
                        serverPlayer,
                        formula.realm(),
                        this.pendingGrade,
                        formula.experienceMultiplier()
                );
        CultivationManager.syncTo(serverPlayer);

        serverPlayer.sendSystemMessage(Component.translatable(
                "message.eightwastelands.alchemy.refined",
                result.getHoverName(),
                this.pendingGrade.getDisplayName(),
                progress.gainedExperience(),
                progress.newExperience(),
                progress.requiredExperience()
        ));

        if (progress.advanced()) {
            serverPlayer.sendSystemMessage(Component.translatable(
                    "message.eightwastelands.alchemy.advanced",
                    progress.newRealm().getDisplayName(),
                    progress.newMastery().getDisplayName()
            ));
        }

        resetPendingRecipe();
        this.cookingProgress = 0;
        this.cookingTotalTime = getCookingTime(this.items.get(SLOT_INPUT));
        setChanged();
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

    private boolean canAcceptResult(PillGrade grade, ItemStack result) {
        int outputIndex = outputIndexFor(grade);
        if (outputIndex < 0) {
            return false;
        }

        ItemStack output = this.items.get(outputIndex);
        if (output.isEmpty()) {
            return true;
        }

        if (!ItemStack.isSameItemSameComponents(output, result)) {
            return false;
        }

        return output.getCount() + result.getCount() <= Math.min(output.getMaxStackSize(), result.getMaxStackSize());
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
        boolean changed = this.pendingRealm != null
                || this.pendingGrade != null
                || this.cookingProgress != 0
                || this.cookingTotalTime != 0;

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
        return AlchemyFormulas.find(stack).map(AlchemyFormula::realm);
    }

    public static int getCookingTime(ItemStack stack) {
        return AlchemyFormulas.find(stack)
                .map(AlchemyFormula::cookingTime)
                .orElse(0);
    }

    public static int outputIndexFor(PillGrade grade) {
        if (grade == null) {
            return -1;
        }

        return switch (grade) {
            case LOW -> SLOT_OUTPUT_LOW;
            case MIDDLE -> SLOT_OUTPUT_MIDDLE;
            case HIGH -> SLOT_OUTPUT_HIGH;
            case IMMORTAL -> SLOT_OUTPUT_IMMORTAL;
            case NONE -> -1;
        };
    }

    public static int getFuelBurnDuration(ItemStack stack, Level level) {
        if (stack.isEmpty()) {
            return 0;
        }

        return stack.getBurnTime(RecipeType.SMELTING, level.fuelValues());
    }

    public static boolean canPlaceFuel(ItemStack stack, Level level) {
        return getFuelBurnDuration(stack, level) > 0 || stack.is(Items.BUCKET);
    }

    private static boolean sameItemKey(ItemStack first, ItemStack second) {
        if (first.isEmpty() && second.isEmpty()) {
            return true;
        }

        if (first.isEmpty() || second.isEmpty()) {
            return false;
        }

        return ItemStack.isSameItemSameComponents(first, second);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == SLOT_INPUT) {
            Optional<CultivationRealm> realm = resolveRealm(stack);
            return realm.isPresent() && canFurnaceRefine(realm.get());
        }

        if (slot == SLOT_FUEL && this.level != null) {
            return canPlaceFuel(stack, this.level);
        }

        return slot < OUTPUT_SLOT_START;
    }

    @Override
    public void setItem(int slot, ItemStack stack, boolean insideTransaction) {
        ItemStack oldKey = slot == SLOT_INPUT && !this.items.get(slot).isEmpty()
                ? this.items.get(slot).copyWithCount(1)
                : ItemStack.EMPTY;

        super.setItem(slot, stack, insideTransaction);

        if (slot == SLOT_INPUT) {
            ItemStack newKey = stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(1);
            if (!sameItemKey(oldKey, newKey)) {
                resetPendingRecipe();
                this.cookingProgress = 0;
                this.cookingTotalTime = getCookingTime(stack);
            }
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.items);
        this.litTime = input.getIntOr("lit_time_remaining", 0);
        this.litDuration = input.getIntOr("lit_total_time", 0);
        this.cookingProgress = input.getIntOr("cooking_time_spent", 0);
        this.cookingTotalTime = input.getIntOr("cooking_total_time", 0);
        this.lastAlchemistName = input.getStringOr("last_alchemist_name", "");

        String uuidText = input.getStringOr("last_alchemist_uuid", "");
        try {
            this.lastAlchemistUuid = uuidText.isBlank() ? null : UUID.fromString(uuidText);
        } catch (IllegalArgumentException ignored) {
            this.lastAlchemistUuid = null;
        }

        int pendingRealmIndex = input.getIntOr("pending_realm", -1);
        this.pendingRealm = CultivationRealm.byIndex(pendingRealmIndex).orElse(null);
        this.pendingGrade = PillGrade.bySerializedName(input.getStringOr("pending_grade", ""))
                .filter(grade -> grade != PillGrade.NONE)
                .orElse(null);
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
        output.putString("last_alchemist_uuid", this.lastAlchemistUuid == null ? "" : this.lastAlchemistUuid.toString());
        output.putString("last_alchemist_name", this.lastAlchemistName);
        output.putInt("pending_realm", this.pendingRealm == null ? -1 : this.pendingRealm.getIndex());
        output.putString("pending_grade", this.pendingGrade == null ? "" : this.pendingGrade.getSerializedName());
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
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
            setLastAlchemist(inventory.player);
        }

        return new AlchemyFurnaceMenu(
                containerId,
                inventory,
                ContainerLevelAccess.create(this.level, this.worldPosition),
                this,
                this.dataAccess,
                getMaxRealm()
        );
    }
}
