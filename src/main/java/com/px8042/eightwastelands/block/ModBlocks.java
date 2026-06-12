package com.px8042.eightwastelands.block;

import com.px8042.eightwastelands.EightWastelands;
import com.px8042.eightwastelands.block.custom.LouZhenBlock;
import com.px8042.eightwastelands.block.custom.WealthTableBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(EightWastelands.MODID);

    private static ResourceKey<Block> blockKey(String name) {
        return ResourceKey.create(
                Registries.BLOCK,
                Identifier.fromNamespaceAndPath(EightWastelands.MODID, name)
        );
    }

    private static BlockBehaviour.Properties blockProperties(String name, BlockBehaviour.Properties properties) {
        return properties.setId(blockKey(name));
    }

    
    public static final DeferredBlock<Block> LOU_ZHEN =
            BLOCKS.register("lou_zhen",
                    () -> new LouZhenBlock(
                            blockProperties("lou_zhen", BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL))
                    ));

    public static final DeferredBlock<Block> WEALTH_TABLE =
            BLOCKS.register("wealth_table",
                    () -> new WealthTableBlock(
                            blockProperties("wealth_table", BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE))
                    ));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
