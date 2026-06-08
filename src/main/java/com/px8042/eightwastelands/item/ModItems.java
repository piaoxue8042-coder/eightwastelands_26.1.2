package com.px8042.eightwastelands.item;

import com.px8042.eightwastelands.EightWastelands;
import com.px8042.eightwastelands.block.ModBlocks;
import com.px8042.eightwastelands.item.custom.*;
import com.px8042.eightwastelands.item.fabao.FabaoBagItem;
import com.px8042.eightwastelands.item.fabao.TestFabaoItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(EightWastelands.MODID);

    private static ResourceKey<Item> itemKey(String name) {
        return ResourceKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(EightWastelands.MODID, name)
        );
    }

    private static Item.Properties itemProperties(String name) {
        return new Item.Properties().setId(itemKey(name));
    }


    
    public static final DeferredItem<Item> NINE_HEAVENS_PUNISHMENT =
            ITEMS.register("nine_heavens_punishment",
                    () -> new NineHeavensPunishmentItem(
                            itemProperties("nine_heavens_punishment").stacksTo(1)
                    ));

    public static void register(IEventBus eventBus) {

        ITEMS.register(eventBus);
    }
    
    public static final DeferredItem<Item> WEALTH =
            ITEMS.register("wealth",
                    () -> new WealthItem(
                            itemProperties("wealth").stacksTo(1)
                    ));
    
    public static final DeferredItem<Item> SUMMER_FAN =
            ITEMS.register("summer_fan",
                    () -> new SummerFanItem(
                            itemProperties("summer_fan").stacksTo(1)
                    ));
    
    public static final DeferredItem<Item> SPRING_SHEARS =
            ITEMS.register("spring_shears",
                    () -> new SpringShearsItem(
                            itemProperties("spring_shears").stacksTo(1)
                    ));
    
    public static final DeferredItem<Item> HEART_DEMON_MASK =
            ITEMS.register("heart_demon_mask",
                    () -> new HeartDemonMaskItem(
                            itemProperties("heart_demon_mask").stacksTo(1)
                    ));
    
    public static final DeferredItem<Item> GUIYUAN_BELL =
            ITEMS.register("guiyuan_bell",
                    () -> new GuiyuanBellItem(
                            itemProperties("guiyuan_bell").stacksTo(1)
                    ));
    
    public static final DeferredItem<Item> BLOOD_SKULL =
            ITEMS.register("blood_skull",
                    () -> new BloodSkullItem(
                            itemProperties("blood_skull").stacksTo(1)
                    ));
    
    public static final DeferredItem<Item> SHENG_ZHUANG =
            ITEMS.register("sheng_zhuang",
                    () -> new ShengZhuangItem(
                            itemProperties("sheng_zhuang").stacksTo(1)
                    ));
    
    public static final DeferredItem<Item> HEAVENLY_THUNDER_SEAL =
            ITEMS.register("heavenly_thunder_seal",
                    () -> new HeavenlyThunderSealItem(
                            itemProperties("heavenly_thunder_seal").stacksTo(1)
                    ));
    
    public static final DeferredItem<Item> FORGETFULNESS =
            ITEMS.register("forgetfulness",
                    () -> new ForgetfulnessItem(
                            itemProperties("forgetfulness")
                                    .stacksTo(1)
                                    .durability(800)
                    ));
    
    public static final DeferredItem<Item> JIEHUI_RING =
            ITEMS.register("jiehui_ring",
                    () -> new JiehuiRingItem(
                            itemProperties("jiehui_ring")
                                    .stacksTo(1)
                                    .durability(500)
                    ));
    
    public static final DeferredItem<Item> LOU_ZHEN =
            ITEMS.register("lou_zhen",
                    () -> new BlockItem(
                            ModBlocks.LOU_ZHEN.get(),
                            itemProperties("lou_zhen").useBlockDescriptionPrefix()
                    ));
    
    public static final DeferredItem<Item> FENGXING_BOOTS =
            ITEMS.register("fengxing_boots",
                    () -> new FengxingBootsItem(
                            itemProperties("fengxing_boots")
                                    .stacksTo(1)
                                    .durability(600)
                    ));
    
    public static final DeferredItem<Item> BIHUO_PEARL =
            ITEMS.register("bihuo_pearl",
                    () -> new BihuoPearlItem(
                            itemProperties("bihuo_pearl")
                                    .stacksTo(1)
                                    .durability(400)
                    ));

    public static final DeferredItem<Item> FABAO_BAG =
            ITEMS.register("fabao_bag",
                    () -> new FabaoBagItem(
                            itemProperties("fabao_bag").stacksTo(1)
                    ));

    public static final DeferredItem<Item> TEST_FABAO =
            ITEMS.register("test_fabao",
                    () -> new TestFabaoItem(
                            itemProperties("test_fabao")
                                    .stacksTo(1)
                                    .enchantable(15)
                    ));

    
}
