package com.px8042.eightwastelands.refining;

import com.px8042.eightwastelands.cultivation.CultivationRealm;
import com.px8042.eightwastelands.item.ModItems;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public final class RefiningFormulas {

    private static final List<RefiningFormula> FORMULAS = List.of(
            new RefiningFormula(ModItems.QI_REFINING_SWORD_BLANK, CultivationRealm.QI_REFINING, 200,
                    ModItems.LOW_GRADE_QI_REFINING_SWORD),
            new RefiningFormula(ModItems.QI_REFINING_RING_BLANK, CultivationRealm.QI_REFINING, 200,
                    ModItems.LOW_GRADE_QI_REFINING_RING),
            new RefiningFormula(ModItems.QI_REFINING_HELMET_BLANK, CultivationRealm.QI_REFINING, 200,
                    ModItems.LOW_GRADE_QI_REFINING_HELMET),
            new RefiningFormula(ModItems.QI_REFINING_CHESTPLATE_BLANK, CultivationRealm.QI_REFINING, 200,
                    ModItems.LOW_GRADE_QI_REFINING_CHESTPLATE),
            new RefiningFormula(ModItems.QI_REFINING_ROBE_BLANK, CultivationRealm.QI_REFINING, 200,
                    ModItems.LOW_GRADE_QI_REFINING_ROBE),
            new RefiningFormula(ModItems.QI_REFINING_LEGGINGS_BLANK, CultivationRealm.QI_REFINING, 200,
                    ModItems.LOW_GRADE_QI_REFINING_LEGGINGS),
            new RefiningFormula(ModItems.QI_REFINING_BOOTS_BLANK, CultivationRealm.QI_REFINING, 200,
                    ModItems.LOW_GRADE_QI_REFINING_BOOTS),
            new RefiningFormula(ModItems.QI_REFINING_LONG_SWORD_FABAO_BLANK, CultivationRealm.QI_REFINING, 200,
                    ModItems.LOW_GRADE_QI_REFINING_LONG_SWORD_FABAO)
    );

    private RefiningFormulas() {
    }

    public static Optional<RefiningFormula> find(ItemStack stack) {
        return FORMULAS.stream().filter(formula -> formula.matches(stack)).findFirst();
    }
}
