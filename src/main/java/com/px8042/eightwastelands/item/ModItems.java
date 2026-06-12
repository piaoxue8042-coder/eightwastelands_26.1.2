package com.px8042.eightwastelands.item;

import com.px8042.eightwastelands.EightWastelands;
import com.px8042.eightwastelands.block.ModBlocks;
import com.px8042.eightwastelands.item.custom.*;
import com.px8042.eightwastelands.item.fabao.FabaoBagItem;
import com.px8042.eightwastelands.item.fabao.*;
import com.px8042.eightwastelands.item.material.ModMaterialItem;
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

    private static DeferredItem<Item> registerMaterial(String name) {
        return ITEMS.register(name, () -> new ModMaterialItem(itemProperties(name)));
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

    public static final DeferredItem<Item> SLEEPWALKING_GHOST =
            ITEMS.register("sleepwalking_ghost",
                    () -> new SleepwalkingGhostItem(
                            itemProperties("sleepwalking_ghost")
                                    .stacksTo(1)
                                    .durability(800)
                    ));

    public static final DeferredItem<Item> GHOST_BLOOD =
            ITEMS.register("ghost_blood",
                    () -> new GhostBloodItem(
                            itemProperties("ghost_blood")
                                    .stacksTo(1)
                                    .durability(800)
                    ));

    public static final DeferredItem<Item> GHOST_BRAIN =
            ITEMS.register("ghost_brain",
                    () -> new GhostBrainItem(
                            itemProperties("ghost_brain")
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

    public static final DeferredItem<Item> WEALTH_TABLE =
            ITEMS.register("wealth_table",
                    () -> new BlockItem(
                            ModBlocks.WEALTH_TABLE.get(),
                            itemProperties("wealth_table").useBlockDescriptionPrefix()
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

    public static final DeferredItem<Item> JADE_MARROW_PENDANT =
            ITEMS.register("jade_marrow_pendant",
                    () -> new JadeMarrowPendantItem(
                            itemProperties("jade_marrow_pendant")
                                    .stacksTo(1)
                                    .durability(500)
                    ));

    public static final DeferredItem<Item> FABAO_BAG =
            ITEMS.register("fabao_bag",
                    () -> new FabaoBagItem(
                            itemProperties("fabao_bag").stacksTo(1)
                    ));

    public static final DeferredItem<Item> SPIRIT_CORE = registerMaterial("spirit_core");
    public static final DeferredItem<Item> FORMATION_STONE = registerMaterial("formation_stone");
    public static final DeferredItem<Item> SPIRIT_IRON_INGOT = registerMaterial("spirit_iron_ingot");
    public static final DeferredItem<Item> COLD_IRON_INGOT = registerMaterial("cold_iron_ingot");
    public static final DeferredItem<Item> DARK_STEEL_INGOT = registerMaterial("dark_steel_ingot");
    public static final DeferredItem<Item> THUNDER_STEEL_INGOT = registerMaterial("thunder_steel_ingot");
    public static final DeferredItem<Item> SWORD_BLANK = registerMaterial("sword_blank");
    public static final DeferredItem<Item> SWORD_INTENT_SHARD = registerMaterial("sword_intent_shard");
    public static final DeferredItem<Item> EDGE_TEMPERING_STONE = registerMaterial("edge_tempering_stone");

    public static final DeferredItem<Item> IRONCOLD_BLADE =
            ITEMS.register("ironcold_blade",
                    () -> new IroncoldBladeItem(
                            itemProperties("ironcold_blade")
                                    .stacksTo(1)
                                    .enchantable(15)
                    ));

    public static final DeferredItem<Item> HUOYUN_SWORD =
            ITEMS.register("huoyun_sword",
                    () -> new HuoyunSwordItem(
                            itemProperties("huoyun_sword")
                                    .stacksTo(1)
                                    .enchantable(15)
                    ));

    public static final DeferredItem<Item> KAITIAN_SWORD =
            ITEMS.register("fucksky_sword",
                    () -> new KaitianSwordItem(
                            itemProperties("fucksky_sword")
                                    .stacksTo(1)
                                    .enchantable(15)
                    ));

    public static final DeferredItem<Item> BLOOD_DRINKING_BLADE =
            ITEMS.register("blood_drinking_blade",
                    () -> new BloodDrinkingBladeItem(
                            itemProperties("blood_drinking_blade")
                                    .stacksTo(1)
                                    .enchantable(15)
                    ));

    public static final DeferredItem<Item> ZAOHUA_PISHAN_AXE =
            ITEMS.register("fuckmountain_axe",
                    () -> new ZaohuaPishanAxeItem(
                            itemProperties("fuckmountain_axe")
                                    .stacksTo(1)
                                    .enchantable(15)
                    ));

    public static final DeferredItem<Item> TEST_FABAO =
            ITEMS.register("test_fabao",
                    () -> new TestFabaoItem(
                            itemProperties("test_fabao")
                                    .stacksTo(1)
                                    .enchantable(15)
                    ));

    public static final DeferredItem<Item> TEST_QUICK_BLADE =
            ITEMS.register("test_quick_blade",
                    () -> new TestQuickBladeItem(
                            itemProperties("test_quick_blade")
                                    .stacksTo(1)
                                    .enchantable(15)
                    ));

    public static final DeferredItem<Item> TEST_SPEAR_SHADOW =
            ITEMS.register("test_spear_shadow",
                    () -> new TestSpearShadowItem(
                            itemProperties("test_spear_shadow")
                                    .stacksTo(1)
                                    .enchantable(15)
                    ));

    public static final DeferredItem<Item> TEST_SPINNING_AXE =
            ITEMS.register("test_spinning_axe",
                    () -> new TestSpinningAxeItem(
                            itemProperties("test_spinning_axe")
                                    .stacksTo(1)
                                    .enchantable(15)
                    ));

    public static final DeferredItem<Item> TEST_FLAME_BAN_NEEDLE =
            ITEMS.register("test_flame_ban_needle",
                    () -> new TestFlameBanNeedleItem(
                            itemProperties("test_flame_ban_needle")
                                    .stacksTo(1)
                                    .enchantable(15)
                    ));

    public static final DeferredItem<Item> TEST_COLD_ACCUMULATION_NEEDLE =
            ITEMS.register("test_cold_accumulation_needle",
                    () -> new TestColdAccumulationNeedleItem(
                            itemProperties("test_cold_accumulation_needle")
                                    .stacksTo(1)
                                    .enchantable(15)
                    ));

    
}
