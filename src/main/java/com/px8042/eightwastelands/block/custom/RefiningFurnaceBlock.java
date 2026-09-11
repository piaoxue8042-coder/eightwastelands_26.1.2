package com.px8042.eightwastelands.block.custom;

import com.px8042.eightwastelands.block.entity.ModBlockEntities;
import com.px8042.eightwastelands.block.entity.RefiningFurnaceBlockEntity;
import com.px8042.eightwastelands.cultivation.CultivationRealm;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class RefiningFurnaceBlock extends Block implements EntityBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private final CultivationRealm maxRealm;

    public RefiningFurnaceBlock(BlockBehaviour.Properties properties, CultivationRealm maxRealm) {
        super(properties);
        this.maxRealm = maxRealm;
        registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    public CultivationRealm getMaxRealm() {
        return this.maxRealm;
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult
    ) {
        return openMenu(level, pos, player);
    }

    @Override
    protected InteractionResult useItemOn(
            ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hitResult
    ) {
        return openMenu(level, pos, player);
    }

    private InteractionResult openMenu(Level level, BlockPos pos, Player player) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (player instanceof ServerPlayer serverPlayer
                && level.getBlockEntity(pos) instanceof RefiningFurnaceBlockEntity furnace) {
            furnace.setLastRefiner(serverPlayer);
            serverPlayer.openMenu(furnace, buffer -> buffer.writeInt(this.maxRealm.getIndex()));
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof MenuProvider menuProvider ? menuProvider : null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new RefiningFurnaceBlockEntity(worldPosition, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level, BlockState blockState, BlockEntityType<T> blockEntityType
    ) {
        if (blockEntityType != ModBlockEntities.REFINING_FURNACE.get()) {
            return null;
        }
        return (BlockEntityTicker<T>) (BlockEntityTicker<RefiningFurnaceBlockEntity>)
                RefiningFurnaceBlockEntity::serverTick;
    }
}
