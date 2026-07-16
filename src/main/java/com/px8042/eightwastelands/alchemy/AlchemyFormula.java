package com.px8042.eightwastelands.alchemy;

import com.px8042.eightwastelands.cultivation.CultivationRealm;
import com.px8042.eightwastelands.item.pill.PillGrade;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record AlchemyFormula(
        Supplier<? extends Item> input,
        CultivationRealm realm,
        int cookingTime,
        Supplier<? extends Item> lowResult,
        Supplier<? extends Item> middleResult,
        Supplier<? extends Item> highResult,
        Supplier<? extends Item> immortalResult
) {

    public AlchemyFormula {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(realm, "realm");
        Objects.requireNonNull(lowResult, "lowResult");
        Objects.requireNonNull(middleResult, "middleResult");
        Objects.requireNonNull(highResult, "highResult");
        Objects.requireNonNull(immortalResult, "immortalResult");
        cookingTime = Math.max(1, cookingTime);
    }

    public boolean matches(ItemStack stack) {
        return !stack.isEmpty() && stack.is(this.input.get());
    }

    public ItemStack createResult(PillGrade grade) {
        Item item = switch (grade) {
            case LOW -> this.lowResult.get();
            case MIDDLE -> this.middleResult.get();
            case HIGH -> this.highResult.get();
            case IMMORTAL -> this.immortalResult.get();
            case NONE -> null;
        };

        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }
}
