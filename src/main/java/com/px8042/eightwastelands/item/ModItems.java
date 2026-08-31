package com.px8042.eightwastelands.item;

import com.px8042.eightwastelands.EightWastelands;
import com.px8042.eightwastelands.block.ModBlocks;
import com.px8042.eightwastelands.cultivation.CultivationRealm;
import com.px8042.eightwastelands.item.custom.*;
import com.px8042.eightwastelands.item.fabao.*;
import com.px8042.eightwastelands.item.material.ModMaterialItem;
import com.px8042.eightwastelands.item.pill.BreakthroughPillItem;
import com.px8042.eightwastelands.item.pill.CultivationPillItem;
import com.px8042.eightwastelands.item.pill.FoundationBuildingCultivationPillItem;
import com.px8042.eightwastelands.item.pill.HealingPillItem;
import com.px8042.eightwastelands.item.pill.PillData;
import com.px8042.eightwastelands.item.pill.PillGrade;
import com.px8042.eightwastelands.item.pill.QiRefiningCultivationPillItem;
import com.px8042.eightwastelands.item.xinfa.QiRefiningManualItem;
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

    public static final DeferredItem<Item> ALCHEMY_FURNACE =
            ITEMS.register("alchemy_furnace",
                    () -> new AlchemyFurnaceBlockItem(
                            ModBlocks.ALCHEMY_FURNACE.get(),
                            CultivationRealm.QI_REFINING,
                            itemProperties("alchemy_furnace").useBlockDescriptionPrefix()
                    ));

    public static final DeferredItem<Item> FOUNDATION_BUILDING_ALCHEMY_FURNACE =
            ITEMS.register("foundation_building_alchemy_furnace",
                    () -> new AlchemyFurnaceBlockItem(
                            ModBlocks.FOUNDATION_BUILDING_ALCHEMY_FURNACE.get(),
                            CultivationRealm.FOUNDATION_BUILDING,
                            itemProperties("foundation_building_alchemy_furnace").useBlockDescriptionPrefix()
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

    public static final DeferredItem<Item> QI_REFINING_MONSTER_CORE =
            registerMaterial("qi_refining_monster_core");
    public static final DeferredItem<Item> FOUNDATION_BUILDING_MONSTER_CORE =
            registerMaterial("foundation_building_monster_core");
    public static final DeferredItem<Item> QI_REFINING_SPIRIT_HERB =
            registerMaterial("qi_refining_spirit_herb");
    public static final DeferredItem<Item> FOUNDATION_BUILDING_SPIRIT_HERB =
            registerMaterial("foundation_building_spirit_herb");
    public static final DeferredItem<Item> QI_REFINING_PILL_BASE = registerMaterial("qi_refining_pill_base");
    public static final DeferredItem<Item> FOUNDATION_BUILDING_PILL_BASE =
            registerMaterial("foundation_building_pill_base");
    public static final DeferredItem<Item> QI_REFINING_BREAKTHROUGH_PILL_BASE =
            registerMaterial("qi_refining_breakthrough_pill_base");
    public static final DeferredItem<Item> FOUNDATION_BUILDING_BREAKTHROUGH_PILL_BASE =
            registerMaterial("foundation_building_breakthrough_pill_base");
    public static final DeferredItem<Item> WUDAO_SAND =
            registerMaterial("wudao_sand");

    public static final DeferredItem<Item> QI_REFINING_MANUAL =
            ITEMS.register("qi_refining_manual",
                    () -> new QiRefiningManualItem(
                            itemProperties("qi_refining_manual").stacksTo(1)
                    ));

    public static final DeferredItem<Item> CULTIVATION_PILL =
            ITEMS.register("cultivation_pill",
                    () -> new CultivationPillItem(
                            itemProperties("cultivation_pill").stacksTo(16)
                    ));

    public static final DeferredItem<Item> QI_REFINING_CULTIVATION_PILL =
            ITEMS.register("qi_refining_cultivation_pill",
                    () -> new QiRefiningCultivationPillItem(
                            itemProperties("qi_refining_cultivation_pill").stacksTo(16)
                    ));

    public static final DeferredItem<Item> LOW_GRADE_QI_REFINING_CULTIVATION_PILL =
            ITEMS.register("low_grade_qi_refining_cultivation_pill",
                    () -> new QiRefiningCultivationPillItem(
                            itemProperties("low_grade_qi_refining_cultivation_pill").stacksTo(16),
                            PillGrade.LOW,
                            PillData.cultivationQiGain(CultivationRealm.QI_REFINING, PillGrade.LOW)
                    ));

    public static final DeferredItem<Item> MIDDLE_GRADE_QI_REFINING_CULTIVATION_PILL =
            ITEMS.register("middle_grade_qi_refining_cultivation_pill",
                    () -> new QiRefiningCultivationPillItem(
                            itemProperties("middle_grade_qi_refining_cultivation_pill").stacksTo(16),
                            PillGrade.MIDDLE,
                            PillData.cultivationQiGain(CultivationRealm.QI_REFINING, PillGrade.MIDDLE)
                    ));

    public static final DeferredItem<Item> HIGH_GRADE_QI_REFINING_CULTIVATION_PILL =
            ITEMS.register("high_grade_qi_refining_cultivation_pill",
                    () -> new QiRefiningCultivationPillItem(
                            itemProperties("high_grade_qi_refining_cultivation_pill").stacksTo(16),
                            PillGrade.HIGH,
                            PillData.cultivationQiGain(CultivationRealm.QI_REFINING, PillGrade.HIGH)
                    ));

    public static final DeferredItem<Item> IMMORTAL_GRADE_QI_REFINING_CULTIVATION_PILL =
            ITEMS.register("immortal_grade_qi_refining_cultivation_pill",
                    () -> new QiRefiningCultivationPillItem(
                            itemProperties("immortal_grade_qi_refining_cultivation_pill").stacksTo(16),
                            PillGrade.IMMORTAL,
                            PillData.cultivationQiGain(CultivationRealm.QI_REFINING, PillGrade.IMMORTAL)
                    ));

    public static final DeferredItem<Item> FOUNDATION_BUILDING_CULTIVATION_PILL =
            ITEMS.register("foundation_building_cultivation_pill",
                    () -> new FoundationBuildingCultivationPillItem(
                            itemProperties("foundation_building_cultivation_pill").stacksTo(16)
                    ));

    public static final DeferredItem<Item> LOW_GRADE_FOUNDATION_BUILDING_CULTIVATION_PILL =
            ITEMS.register("low_grade_foundation_building_cultivation_pill",
                    () -> new FoundationBuildingCultivationPillItem(
                            itemProperties("low_grade_foundation_building_cultivation_pill").stacksTo(16),
                            PillGrade.LOW,
                            PillData.cultivationQiGain(CultivationRealm.FOUNDATION_BUILDING, PillGrade.LOW)
                    ));

    public static final DeferredItem<Item> MIDDLE_GRADE_FOUNDATION_BUILDING_CULTIVATION_PILL =
            ITEMS.register("middle_grade_foundation_building_cultivation_pill",
                    () -> new FoundationBuildingCultivationPillItem(
                            itemProperties("middle_grade_foundation_building_cultivation_pill").stacksTo(16),
                            PillGrade.MIDDLE,
                            PillData.cultivationQiGain(CultivationRealm.FOUNDATION_BUILDING, PillGrade.MIDDLE)
                    ));

    public static final DeferredItem<Item> HIGH_GRADE_FOUNDATION_BUILDING_CULTIVATION_PILL =
            ITEMS.register("high_grade_foundation_building_cultivation_pill",
                    () -> new FoundationBuildingCultivationPillItem(
                            itemProperties("high_grade_foundation_building_cultivation_pill").stacksTo(16),
                            PillGrade.HIGH,
                            PillData.cultivationQiGain(CultivationRealm.FOUNDATION_BUILDING, PillGrade.HIGH)
                    ));

    public static final DeferredItem<Item> IMMORTAL_GRADE_FOUNDATION_BUILDING_CULTIVATION_PILL =
            ITEMS.register("immortal_grade_foundation_building_cultivation_pill",
                    () -> new FoundationBuildingCultivationPillItem(
                            itemProperties("immortal_grade_foundation_building_cultivation_pill").stacksTo(16),
                            PillGrade.IMMORTAL,
                            PillData.cultivationQiGain(CultivationRealm.FOUNDATION_BUILDING, PillGrade.IMMORTAL)
                    ));

    public static final DeferredItem<Item> HEALING_PILL =
            ITEMS.register("healing_pill",
                    () -> new HealingPillItem(
                            itemProperties("healing_pill").stacksTo(16)
                    ));

    public static final DeferredItem<Item> LOW_GRADE_QI_REFINING_BREAKTHROUGH_PILL =
            ITEMS.register("low_grade_qi_refining_breakthrough_pill",
                    () -> new BreakthroughPillItem(
                            itemProperties("low_grade_qi_refining_breakthrough_pill").stacksTo(16),
                            CultivationRealm.QI_REFINING,
                            PillGrade.LOW,
                            PillData.breakthroughChanceBonus(PillGrade.LOW),
                            PillData.breakthroughCooldownReductionTicks(PillGrade.LOW)
                    ));

    public static final DeferredItem<Item> MIDDLE_GRADE_QI_REFINING_BREAKTHROUGH_PILL =
            ITEMS.register("middle_grade_qi_refining_breakthrough_pill",
                    () -> new BreakthroughPillItem(
                            itemProperties("middle_grade_qi_refining_breakthrough_pill").stacksTo(16),
                            CultivationRealm.QI_REFINING,
                            PillGrade.MIDDLE,
                            PillData.breakthroughChanceBonus(PillGrade.MIDDLE),
                            PillData.breakthroughCooldownReductionTicks(PillGrade.MIDDLE)
                    ));

    public static final DeferredItem<Item> HIGH_GRADE_QI_REFINING_BREAKTHROUGH_PILL =
            ITEMS.register("high_grade_qi_refining_breakthrough_pill",
                    () -> new BreakthroughPillItem(
                            itemProperties("high_grade_qi_refining_breakthrough_pill").stacksTo(16),
                            CultivationRealm.QI_REFINING,
                            PillGrade.HIGH,
                            PillData.breakthroughChanceBonus(PillGrade.HIGH),
                            PillData.breakthroughCooldownReductionTicks(PillGrade.HIGH)
                    ));

    public static final DeferredItem<Item> IMMORTAL_GRADE_QI_REFINING_BREAKTHROUGH_PILL =
            ITEMS.register("immortal_grade_qi_refining_breakthrough_pill",
                    () -> new BreakthroughPillItem(
                            itemProperties("immortal_grade_qi_refining_breakthrough_pill").stacksTo(16),
                            CultivationRealm.QI_REFINING,
                            PillGrade.IMMORTAL,
                            PillData.breakthroughChanceBonus(PillGrade.IMMORTAL),
                            PillData.breakthroughCooldownReductionTicks(PillGrade.IMMORTAL)
                    ));

    public static final DeferredItem<Item> LOW_GRADE_FOUNDATION_BUILDING_BREAKTHROUGH_PILL =
            ITEMS.register("low_grade_foundation_building_breakthrough_pill",
                    () -> new BreakthroughPillItem(
                            itemProperties("low_grade_foundation_building_breakthrough_pill").stacksTo(16),
                            CultivationRealm.FOUNDATION_BUILDING,
                            PillGrade.LOW,
                            PillData.breakthroughChanceBonus(PillGrade.LOW),
                            PillData.breakthroughCooldownReductionTicks(PillGrade.LOW)
                    ));

    public static final DeferredItem<Item> MIDDLE_GRADE_FOUNDATION_BUILDING_BREAKTHROUGH_PILL =
            ITEMS.register("middle_grade_foundation_building_breakthrough_pill",
                    () -> new BreakthroughPillItem(
                            itemProperties("middle_grade_foundation_building_breakthrough_pill").stacksTo(16),
                            CultivationRealm.FOUNDATION_BUILDING,
                            PillGrade.MIDDLE,
                            PillData.breakthroughChanceBonus(PillGrade.MIDDLE),
                            PillData.breakthroughCooldownReductionTicks(PillGrade.MIDDLE)
                    ));

    public static final DeferredItem<Item> HIGH_GRADE_FOUNDATION_BUILDING_BREAKTHROUGH_PILL =
            ITEMS.register("high_grade_foundation_building_breakthrough_pill",
                    () -> new BreakthroughPillItem(
                            itemProperties("high_grade_foundation_building_breakthrough_pill").stacksTo(16),
                            CultivationRealm.FOUNDATION_BUILDING,
                            PillGrade.HIGH,
                            PillData.breakthroughChanceBonus(PillGrade.HIGH),
                            PillData.breakthroughCooldownReductionTicks(PillGrade.HIGH)
                    ));

    public static final DeferredItem<Item> IMMORTAL_GRADE_FOUNDATION_BUILDING_BREAKTHROUGH_PILL =
            ITEMS.register("immortal_grade_foundation_building_breakthrough_pill",
                    () -> new BreakthroughPillItem(
                            itemProperties("immortal_grade_foundation_building_breakthrough_pill").stacksTo(16),
                            CultivationRealm.FOUNDATION_BUILDING,
                            PillGrade.IMMORTAL,
                            PillData.breakthroughChanceBonus(PillGrade.IMMORTAL),
                            PillData.breakthroughCooldownReductionTicks(PillGrade.IMMORTAL)
                    ));

    
}
