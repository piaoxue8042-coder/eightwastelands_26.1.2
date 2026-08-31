package com.px8042.eightwastelands.alchemy;

import com.px8042.eightwastelands.cultivation.CultivationRealm;
import com.px8042.eightwastelands.item.ModItems;
import com.px8042.eightwastelands.item.pill.PillData;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class AlchemyFormulas {

    private static final List<FormulaEntry> TABLE = List.of(
            formula(
                    ModItems.QI_REFINING_PILL_BASE,
                    AlchemyFormulaType.CULTIVATION,
                    CultivationRealm.QI_REFINING,
                    ModItems.LOW_GRADE_QI_REFINING_CULTIVATION_PILL,
                    ModItems.MIDDLE_GRADE_QI_REFINING_CULTIVATION_PILL,
                    ModItems.HIGH_GRADE_QI_REFINING_CULTIVATION_PILL,
                    ModItems.IMMORTAL_GRADE_QI_REFINING_CULTIVATION_PILL
            ),
            formula(
                    ModItems.FOUNDATION_BUILDING_PILL_BASE,
                    AlchemyFormulaType.CULTIVATION,
                    CultivationRealm.FOUNDATION_BUILDING,
                    ModItems.LOW_GRADE_FOUNDATION_BUILDING_CULTIVATION_PILL,
                    ModItems.MIDDLE_GRADE_FOUNDATION_BUILDING_CULTIVATION_PILL,
                    ModItems.HIGH_GRADE_FOUNDATION_BUILDING_CULTIVATION_PILL,
                    ModItems.IMMORTAL_GRADE_FOUNDATION_BUILDING_CULTIVATION_PILL
            ),
            formula(
                    ModItems.QI_REFINING_BREAKTHROUGH_PILL_BASE,
                    AlchemyFormulaType.BREAKTHROUGH,
                    CultivationRealm.QI_REFINING,
                    ModItems.LOW_GRADE_QI_REFINING_BREAKTHROUGH_PILL,
                    ModItems.MIDDLE_GRADE_QI_REFINING_BREAKTHROUGH_PILL,
                    ModItems.HIGH_GRADE_QI_REFINING_BREAKTHROUGH_PILL,
                    ModItems.IMMORTAL_GRADE_QI_REFINING_BREAKTHROUGH_PILL
            ),
            formula(
                    ModItems.FOUNDATION_BUILDING_BREAKTHROUGH_PILL_BASE,
                    AlchemyFormulaType.BREAKTHROUGH,
                    CultivationRealm.FOUNDATION_BUILDING,
                    ModItems.LOW_GRADE_FOUNDATION_BUILDING_BREAKTHROUGH_PILL,
                    ModItems.MIDDLE_GRADE_FOUNDATION_BUILDING_BREAKTHROUGH_PILL,
                    ModItems.HIGH_GRADE_FOUNDATION_BUILDING_BREAKTHROUGH_PILL,
                    ModItems.IMMORTAL_GRADE_FOUNDATION_BUILDING_BREAKTHROUGH_PILL
            )
    );
    private static final List<AlchemyFormula> FORMULAS = TABLE.stream()
            .map(FormulaEntry::toFormula)
            .toList();

    private AlchemyFormulas() {
    }

    public static Optional<AlchemyFormula> find(ItemStack stack) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }

        for (AlchemyFormula formula : FORMULAS) {
            if (formula.matches(stack)) {
                return Optional.of(formula);
            }
        }

        return Optional.empty();
    }

    private static FormulaEntry formula(
            Supplier<? extends Item> input,
            AlchemyFormulaType type,
            CultivationRealm realm,
            Supplier<? extends Item> lowResult,
            Supplier<? extends Item> middleResult,
            Supplier<? extends Item> highResult,
            Supplier<? extends Item> immortalResult
    ) {
        return new FormulaEntry(
                input,
                type,
                realm,
                PillData.cookingTime(realm),
                1.0D,
                lowResult,
                middleResult,
                highResult,
                immortalResult
        );
    }

    private record FormulaEntry(
            Supplier<? extends Item> input,
            AlchemyFormulaType type,
            CultivationRealm realm,
            int cookingTime,
            double experienceMultiplier,
            Supplier<? extends Item> lowResult,
            Supplier<? extends Item> middleResult,
            Supplier<? extends Item> highResult,
            Supplier<? extends Item> immortalResult
    ) {
        private AlchemyFormula toFormula() {
            return new AlchemyFormula(
                    this.input,
                    this.type,
                    this.realm,
                    this.cookingTime,
                    this.experienceMultiplier,
                    this.lowResult,
                    this.middleResult,
                    this.highResult,
                    this.immortalResult
            );
        }
    }
}
