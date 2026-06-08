package com.px8042.eightwastelands.menu;

import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, EightWastelands.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<FabaoBagMenu>> FABAO_BAG =
            MENU_TYPES.register("fabao_bag", () -> IMenuTypeExtension.create(FabaoBagMenu::fromNetwork));

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
