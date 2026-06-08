package com.px8042.eightwastelands;

import com.px8042.eightwastelands.client.gui.FabaoBagScreen;
import com.px8042.eightwastelands.menu.ModMenuTypes;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;


@Mod(value = EightWastelands.MODID, dist = Dist.CLIENT)

@EventBusSubscriber(modid = EightWastelands.MODID, value = Dist.CLIENT)
public class EightWastelandsClient {
    public EightWastelandsClient(ModContainer container) {
        
        
        
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        
        EightWastelands.LOGGER.info("HELLO FROM CLIENT SETUP");
        EightWastelands.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.FABAO_BAG.get(), FabaoBagScreen::new);
    }
}
