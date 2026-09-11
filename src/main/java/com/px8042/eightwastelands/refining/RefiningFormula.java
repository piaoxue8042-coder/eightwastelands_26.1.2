package com.px8042.eightwastelands.refining;

import com.px8042.eightwastelands.cultivation.CultivationRealm;
import com.px8042.eightwastelands.item.cultivation.CultivationGearGrade;
import com.px8042.eightwastelands.item.cultivation.GearInstanceData;
import com.px8042.eightwastelands.item.cultivation.ICultivationGear;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.function.Supplier;

public record RefiningFormula(
        Supplier<? extends Item> input,
        CultivationRealm realm,
        int cookingTime,
        Supplier<? extends Item> output
) {

    public RefiningFormula {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(realm, "realm");
        Objects.requireNonNull(output, "output");
        cookingTime = Math.max(1, cookingTime);
    }

    public boolean matches(ItemStack stack) {
        return !stack.isEmpty() && stack.is(this.input.get());
    }

    public ItemStack createResult(CultivationGearGrade grade, RandomSource random) {
        ItemStack result = new ItemStack(this.output.get());
        if (result.getItem() instanceof ICultivationGear gear) {
            GearInstanceData.setGrade(result, grade);
            GearInstanceData.ensureGenerated(result, gear, random);
        }
        return result;
    }
}
