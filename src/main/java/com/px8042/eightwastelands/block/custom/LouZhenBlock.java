package com.px8042.eightwastelands.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class LouZhenBlock extends AnvilBlock {

    public LouZhenBlock(BlockBehaviour.Properties properties) {
        super(properties);
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

        player.openMenu(createLouZhenMenuProvider());

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

        player.openMenu(createLouZhenMenuProvider());

        return InteractionResult.CONSUME;
    }

    @Override
    public MenuProvider getMenuProvider(
            BlockState state,
            Level level,
            BlockPos pos
    ) {
        return createLouZhenMenuProvider();
    }

    private MenuProvider createLouZhenMenuProvider() {

        return new SimpleMenuProvider(
                (containerId, inventory, player) -> new AnvilMenu(
                        containerId,
                        inventory,
                        ContainerLevelAccess.NULL
                ) {
                    @Override
                    protected boolean isValidBlock(BlockState blockState) {
                        return true;
                    }
                },
                Component.translatable("block.eightwastelands.lou_zhen")
        );
    }
}