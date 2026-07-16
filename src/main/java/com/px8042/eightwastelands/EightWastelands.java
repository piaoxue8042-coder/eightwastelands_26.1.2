package com.px8042.eightwastelands;
import com.px8042.eightwastelands.block.ModBlocks;
import com.px8042.eightwastelands.block.entity.ModBlockEntities;
import com.px8042.eightwastelands.command.CultivationCommands;
import com.px8042.eightwastelands.compat.damagenexus.DamageNexusBridge;
import com.px8042.eightwastelands.event.CultivationCombatEvents;
import com.px8042.eightwastelands.event.CultivationEvents;
import com.px8042.eightwastelands.event.CultivationMonsterEvents;
import com.px8042.eightwastelands.event.FabaoCurioEvents;
import com.px8042.eightwastelands.event.ModEvents;
import com.px8042.eightwastelands.event.ModNegativeEffectEvents;
import com.px8042.eightwastelands.item.ModItems;
import com.px8042.eightwastelands.menu.ModMenuTypes;
import com.px8042.eightwastelands.network.ModNetwork;
import com.px8042.eightwastelands.recipe.ModRecipeSerializers;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import com.px8042.eightwastelands.item.ModCreativeModeTabs;
import com.px8042.eightwastelands.effect.ModMobEffects;


@Mod(EightWastelands.MODID)
public class EightWastelands {
    
    public static final String MODID = "eightwastelands";
    
    public static final Logger LOGGER = LogUtils.getLogger();


    
    
    public EightWastelands(IEventBus modEventBus, ModContainer modContainer) {
        
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModMobEffects.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(ModNetwork::registerPayloads);


        
        
        
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new ModEvents());
        NeoForge.EVENT_BUS.register(new FabaoCurioEvents());
        NeoForge.EVENT_BUS.register(new ModNegativeEffectEvents());
        NeoForge.EVENT_BUS.register(new CultivationEvents());
        NeoForge.EVENT_BUS.register(new CultivationCombatEvents());
        NeoForge.EVENT_BUS.register(new CultivationMonsterEvents());
        NeoForge.EVENT_BUS.register(new CultivationCommands());
        NeoForge.EVENT_BUS.register(new DamageNexusBridge());


        
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


    private void commonSetup(FMLCommonSetupEvent event) {
        
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }


    
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        
        LOGGER.info("HELLO from server starting");
    }
}
