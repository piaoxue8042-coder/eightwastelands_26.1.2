package com.px8042.eightwastelands.item;

import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EightWastelands.MODID);

    public static final Supplier<CreativeModeTab> EIGHT_WASTELANDS_TAB =
            CREATIVE_MODE_TABS.register("eight_wastelands_tab",
                    () -> CreativeModeTab.builder()
                            .icon(() -> new ItemStack(ModItems.NINE_HEAVENS_PUNISHMENT.get()))
                            .title(Component.translatable("creativetab.eightwastelands.eight_wastelands"))
                            .displayItems((parameters, output) -> {
                                output.accept(ModItems.NINE_HEAVENS_PUNISHMENT.get());
                                output.accept(ModItems.WEALTH.get());
                                output.accept(ModItems.SUMMER_FAN.get());
                                output.accept(ModItems.SPRING_SHEARS.get());
                                output.accept(ModItems.HEART_DEMON_MASK.get());
                                output.accept(ModItems.GUIYUAN_BELL.get());
                                output.accept(ModItems.BLOOD_SKULL.get());
                                output.accept(ModItems.SHENG_ZHUANG.get());
                                output.accept(ModItems.HEAVENLY_THUNDER_SEAL.get());
                                output.accept(ModItems.FORGETFULNESS.get());
                                output.accept(ModItems.SLEEPWALKING_GHOST.get());
                                output.accept(ModItems.GHOST_BLOOD.get());
                                output.accept(ModItems.GHOST_BRAIN.get());
                                output.accept(ModItems.JIEHUI_RING.get());
                                output.accept(ModItems.LOU_ZHEN.get());
                                output.accept(ModItems.WEALTH_TABLE.get());
                                output.accept(ModItems.FENGXING_BOOTS.get());
                                output.accept(ModItems.BIHUO_PEARL.get());
                                output.accept(ModItems.JADE_MARROW_PENDANT.get());
                                output.accept(ModItems.FABAO_BAG.get());
                                output.accept(ModItems.SPIRIT_CORE.get());
                                output.accept(ModItems.FORMATION_STONE.get());
                                output.accept(ModItems.SPIRIT_IRON_INGOT.get());
                                output.accept(ModItems.COLD_IRON_INGOT.get());
                                output.accept(ModItems.DARK_STEEL_INGOT.get());
                                output.accept(ModItems.THUNDER_STEEL_INGOT.get());
                                output.accept(ModItems.SWORD_BLANK.get());
                                output.accept(ModItems.SWORD_INTENT_SHARD.get());
                                output.accept(ModItems.EDGE_TEMPERING_STONE.get());
                                output.accept(ModItems.IRONCOLD_BLADE.get());
                                output.accept(ModItems.HUOYUN_SWORD.get());
                                output.accept(ModItems.KAITIAN_SWORD.get());
                                output.accept(ModItems.BLOOD_DRINKING_BLADE.get());
                                output.accept(ModItems.ZAOHUA_PISHAN_AXE.get());
                                output.accept(ModItems.TEST_FABAO.get());
                                output.accept(ModItems.TEST_QUICK_BLADE.get());
                                output.accept(ModItems.TEST_SPEAR_SHADOW.get());
                                output.accept(ModItems.TEST_SPINNING_AXE.get());
                                output.accept(ModItems.TEST_FLAME_BAN_NEEDLE.get());
                                output.accept(ModItems.TEST_COLD_ACCUMULATION_NEEDLE.get());

                                
                            })
                            .build()
            );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
