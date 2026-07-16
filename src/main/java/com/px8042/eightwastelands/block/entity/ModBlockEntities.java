package com.px8042.eightwastelands.block.entity;

import com.px8042.eightwastelands.EightWastelands;
import com.px8042.eightwastelands.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, EightWastelands.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AlchemyFurnaceBlockEntity>>
            ALCHEMY_FURNACE =
            BLOCK_ENTITY_TYPES.register(
                    "alchemy_furnace",
                    () -> new BlockEntityType<>(
                            AlchemyFurnaceBlockEntity::new,
                            ModBlocks.ALCHEMY_FURNACE.get(),
                            ModBlocks.FOUNDATION_BUILDING_ALCHEMY_FURNACE.get()
                    )
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
