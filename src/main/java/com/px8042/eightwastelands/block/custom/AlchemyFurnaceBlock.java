package com.px8042.eightwastelands.block.custom;

import com.px8042.eightwastelands.block.entity.AlchemyFurnaceBlockEntity;
import com.px8042.eightwastelands.block.entity.ModBlockEntities;
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
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class AlchemyFurnaceBlock extends Block implements EntityBlock {

    private final CultivationRealm maxRealm;

    public AlchemyFurnaceBlock(BlockBehaviour.Properties properties, CultivationRealm maxRealm) {
        super(properties);
        this.maxRealm = maxRealm;
    }

    public CultivationRealm getMaxRealm() {
        return this.maxRealm;
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult
    ) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        openAlchemyMenu(level, pos, player);

        return InteractionResult.CONSUME;
    }

    @Override
    protected InteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult
    ) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        openAlchemyMenu(level, pos, player);

        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable MenuProvider getMenuProvider(
            BlockState state,
            Level level,
            BlockPos pos
    ) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof MenuProvider menuProvider ? menuProvider : null;
    }

    private void openAlchemyMenu(Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = getOrCreateAlchemyBlockEntity(level, pos);
        if (player instanceof ServerPlayer serverPlayer
                && blockEntity instanceof AlchemyFurnaceBlockEntity alchemyFurnaceBlockEntity) {
            alchemyFurnaceBlockEntity.setLastAlchemist(serverPlayer);
            serverPlayer.openMenu(
                    alchemyFurnaceBlockEntity,
                    buffer -> buffer.writeInt(this.maxRealm.getIndex())
            );
        }
    }

    private BlockEntity getOrCreateAlchemyBlockEntity(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AlchemyFurnaceBlockEntity) {
            return blockEntity;
        }

        AlchemyFurnaceBlockEntity created = new AlchemyFurnaceBlockEntity(pos, level.getBlockState(pos));
        level.setBlockEntity(created);
        return created;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new AlchemyFurnaceBlockEntity(worldPosition, blockState);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level,
            BlockState blockState,
            BlockEntityType<T> blockEntityType
    ) {
        if (blockEntityType != ModBlockEntities.ALCHEMY_FURNACE.get()) {
            return null;
        }

        return (BlockEntityTicker<T>) (BlockEntityTicker<AlchemyFurnaceBlockEntity>)
                AlchemyFurnaceBlockEntity::serverTick;
    }
}
