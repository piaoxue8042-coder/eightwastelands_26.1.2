package com.px8042.eightwastelands.alchemy;

import com.px8042.eightwastelands.cultivation.CultivationRealm;
import com.px8042.eightwastelands.item.ModItems;
import com.px8042.eightwastelands.item.pill.PillData;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.item.ItemStack;

public final class AlchemyFormulas {

    private static final List<AlchemyFormula> FORMULAS = List.of(
            new AlchemyFormula(
                    ModItems.QI_REFINING_PILL_BASE,
                    CultivationRealm.QI_REFINING,
                    PillData.cookingTime(CultivationRealm.QI_REFINING),
                    ModItems.LOW_GRADE_QI_REFINING_CULTIVATION_PILL,
                    ModItems.MIDDLE_GRADE_QI_REFINING_CULTIVATION_PILL,
                    ModItems.HIGH_GRADE_QI_REFINING_CULTIVATION_PILL,
                    ModItems.IMMORTAL_GRADE_QI_REFINING_CULTIVATION_PILL
            ),
            new AlchemyFormula(
                    ModItems.FOUNDATION_BUILDING_PILL_BASE,
                    CultivationRealm.FOUNDATION_BUILDING,
                    PillData.cookingTime(CultivationRealm.FOUNDATION_BUILDING),
                    ModItems.LOW_GRADE_FOUNDATION_BUILDING_CULTIVATION_PILL,
                    ModItems.MIDDLE_GRADE_FOUNDATION_BUILDING_CULTIVATION_PILL,
                    ModItems.HIGH_GRADE_FOUNDATION_BUILDING_CULTIVATION_PILL,
                    ModItems.IMMORTAL_GRADE_FOUNDATION_BUILDING_CULTIVATION_PILL
            ),
            new AlchemyFormula(
                    ModItems.QI_REFINING_BREAKTHROUGH_PILL_BASE,
                    CultivationRealm.QI_REFINING,
                    PillData.cookingTime(CultivationRealm.QI_REFINING),
                    ModItems.LOW_GRADE_QI_REFINING_BREAKTHROUGH_PILL,
                    ModItems.MIDDLE_GRADE_QI_REFINING_BREAKTHROUGH_PILL,
                    ModItems.HIGH_GRADE_QI_REFINING_BREAKTHROUGH_PILL,
                    ModItems.IMMORTAL_GRADE_QI_REFINING_BREAKTHROUGH_PILL
            ),
            new AlchemyFormula(
                    ModItems.FOUNDATION_BUILDING_BREAKTHROUGH_PILL_BASE,
                    CultivationRealm.FOUNDATION_BUILDING,
                    PillData.cookingTime(CultivationRealm.FOUNDATION_BUILDING),
                    ModItems.LOW_GRADE_FOUNDATION_BUILDING_BREAKTHROUGH_PILL,
                    ModItems.MIDDLE_GRADE_FOUNDATION_BUILDING_BREAKTHROUGH_PILL,
                    ModItems.HIGH_GRADE_FOUNDATION_BUILDING_BREAKTHROUGH_PILL,
                    ModItems.IMMORTAL_GRADE_FOUNDATION_BUILDING_BREAKTHROUGH_PILL
            )
    );

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
}
