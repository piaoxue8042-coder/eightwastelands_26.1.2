package com.px8042.eightwastelands;

import com.px8042.eightwastelands.client.gui.AlchemyFurnaceScreen;
import com.px8042.eightwastelands.client.gui.RefiningFurnaceScreen;
import com.px8042.eightwastelands.client.gui.WealthTableScreen;
import com.px8042.eightwastelands.client.CultivationClientEvents;
import com.px8042.eightwastelands.client.ModKeyMappings;
import com.px8042.eightwastelands.client.CultivationHudOverlay;
import com.px8042.eightwastelands.menu.ModMenuTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;


@Mod(value = EightWastelands.MODID, dist = Dist.CLIENT)

@EventBusSubscriber(modid = EightWastelands.MODID, value = Dist.CLIENT)
public class EightWastelandsClient {
    private static final Identifier CULTIVATION_HUD =
            Identifier.fromNamespaceAndPath(EightWastelands.MODID, "cultivation_hud");

    public EightWastelandsClient(ModContainer container) {
        
        
        
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        NeoForge.EVENT_BUS.register(new CultivationClientEvents());
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        
        EightWastelands.LOGGER.info("HELLO FROM CLIENT SETUP");
        EightWastelands.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.WEALTH_TABLE.get(), WealthTableScreen::new);
        event.register(ModMenuTypes.ALCHEMY_FURNACE.get(), AlchemyFurnaceScreen::new);
        event.register(ModMenuTypes.REFINING_FURNACE.get(), RefiningFurnaceScreen::new);
    }

    @SubscribeEvent
    static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        ModKeyMappings.register(event);
    }

    @SubscribeEvent
    static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CONTEXTUAL_INFO_BAR, CULTIVATION_HUD, CultivationHudOverlay::render);
    }
}
