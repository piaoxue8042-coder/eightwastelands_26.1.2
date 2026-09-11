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
                                output.accept(ModItems.ALCHEMY_FURNACE.get());
                                output.accept(ModItems.FOUNDATION_BUILDING_ALCHEMY_FURNACE.get());
                                output.accept(ModItems.REFINING_FURNACE.get());
                                output.accept(ModItems.QI_REFINING_STORAGE_BAG.get());
                                output.accept(ModItems.FOUNDATION_BUILDING_STORAGE_BAG.get());
                                output.accept(ModItems.FENGXING_BOOTS.get());
                                output.accept(ModItems.BIHUO_PEARL.get());
                                output.accept(ModItems.JADE_MARROW_PENDANT.get());
                                output.accept(ModItems.QI_REFINING_MONSTER_CORE.get());
                                output.accept(ModItems.FOUNDATION_BUILDING_MONSTER_CORE.get());
                                output.accept(ModItems.QI_REFINING_SPIRIT_HERB.get());
                                output.accept(ModItems.FOUNDATION_BUILDING_SPIRIT_HERB.get());
                                output.accept(ModItems.QI_REFINING_PILL_BASE.get());
                                output.accept(ModItems.FOUNDATION_BUILDING_PILL_BASE.get());
                                output.accept(ModItems.QI_REFINING_BREAKTHROUGH_PILL_BASE.get());
                                output.accept(ModItems.FOUNDATION_BUILDING_BREAKTHROUGH_PILL_BASE.get());
                                output.accept(ModItems.WUDAO_SAND.get());
                                output.accept(ModItems.QI_REFINING_SWORD_BLANK.get());
                                output.accept(ModItems.QI_REFINING_RING_BLANK.get());
                                output.accept(ModItems.QI_REFINING_HELMET_BLANK.get());
                                output.accept(ModItems.QI_REFINING_CHESTPLATE_BLANK.get());
                                output.accept(ModItems.QI_REFINING_ROBE_BLANK.get());
                                output.accept(ModItems.QI_REFINING_LEGGINGS_BLANK.get());
                                output.accept(ModItems.QI_REFINING_BOOTS_BLANK.get());
                                output.accept(ModItems.QI_REFINING_LONG_SWORD_FABAO_BLANK.get());
                                output.accept(ModItems.QI_REFINING_SWORD_BLANK.get());
                                output.accept(ModItems.QI_REFINING_RING_BLANK.get());
                                output.accept(ModItems.QI_REFINING_HELMET_BLANK.get());
                                output.accept(ModItems.QI_REFINING_CHESTPLATE_BLANK.get());
                                output.accept(ModItems.QI_REFINING_ROBE_BLANK.get());
                                output.accept(ModItems.QI_REFINING_LEGGINGS_BLANK.get());
                                output.accept(ModItems.QI_REFINING_BOOTS_BLANK.get());
                                output.accept(ModItems.QI_REFINING_LONG_SWORD_FABAO_BLANK.get());
                                output.accept(ModItems.LOW_GRADE_QI_REFINING_SWORD.get());
                                output.accept(ModItems.LOW_GRADE_QI_REFINING_RING.get());
                                output.accept(ModItems.LOW_GRADE_QI_REFINING_HELMET.get());
                                output.accept(ModItems.LOW_GRADE_QI_REFINING_CHESTPLATE.get());
                                output.accept(ModItems.LOW_GRADE_QI_REFINING_ROBE.get());
                                output.accept(ModItems.LOW_GRADE_QI_REFINING_LEGGINGS.get());
                                output.accept(ModItems.LOW_GRADE_QI_REFINING_BOOTS.get());
                                output.accept(ModItems.LOW_GRADE_QI_REFINING_LONG_SWORD_FABAO.get());
                                output.accept(ModItems.QI_REFINING_MANUAL.get());
                                output.accept(ModItems.BODY_TEMPERING_MANUAL.get());
                                output.accept(ModItems.LOW_GRADE_QI_REFINING_CULTIVATION_PILL.get());
                                output.accept(ModItems.MIDDLE_GRADE_QI_REFINING_CULTIVATION_PILL.get());
                                output.accept(ModItems.HIGH_GRADE_QI_REFINING_CULTIVATION_PILL.get());
                                output.accept(ModItems.IMMORTAL_GRADE_QI_REFINING_CULTIVATION_PILL.get());
                                output.accept(ModItems.LOW_GRADE_FOUNDATION_BUILDING_CULTIVATION_PILL.get());
                                output.accept(ModItems.MIDDLE_GRADE_FOUNDATION_BUILDING_CULTIVATION_PILL.get());
                                output.accept(ModItems.HIGH_GRADE_FOUNDATION_BUILDING_CULTIVATION_PILL.get());
                                output.accept(ModItems.IMMORTAL_GRADE_FOUNDATION_BUILDING_CULTIVATION_PILL.get());
                                output.accept(ModItems.HEALING_PILL.get());
                                output.accept(ModItems.LOW_GRADE_QI_REFINING_BREAKTHROUGH_PILL.get());
                                output.accept(ModItems.MIDDLE_GRADE_QI_REFINING_BREAKTHROUGH_PILL.get());
                                output.accept(ModItems.HIGH_GRADE_QI_REFINING_BREAKTHROUGH_PILL.get());
                                output.accept(ModItems.IMMORTAL_GRADE_QI_REFINING_BREAKTHROUGH_PILL.get());
                                output.accept(ModItems.LOW_GRADE_FOUNDATION_BUILDING_BREAKTHROUGH_PILL.get());
                                output.accept(ModItems.MIDDLE_GRADE_FOUNDATION_BUILDING_BREAKTHROUGH_PILL.get());
                                output.accept(ModItems.HIGH_GRADE_FOUNDATION_BUILDING_BREAKTHROUGH_PILL.get());
                                output.accept(ModItems.IMMORTAL_GRADE_FOUNDATION_BUILDING_BREAKTHROUGH_PILL.get());

                                
                            })
                            .build()
            );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
