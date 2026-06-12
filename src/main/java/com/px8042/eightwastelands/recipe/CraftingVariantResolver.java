package com.px8042.eightwastelands.recipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public final class CraftingVariantResolver {

    public static final int OUTPUT_SLOT_COUNT = 9;
    public static final int MAX_MATERIAL_CHOICES_PER_INGREDIENT = 64;
    public static final int MAX_VARIANTS_PER_RECIPE = 256;

    private CraftingVariantResolver() {
    }

    public static boolean isCleanStack(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        if (stack.isEnchanted()) {
            return false;
        }

        if (stack.isDamaged()) {
            return false;
        }

        return stack.getComponentsPatch().isEmpty();
    }

    public static boolean isValidInputForCurrentVariant(ItemStack input, DecompositionVariant variant) {
        if (variant.kind() == DecompositionKind.DISENCHANT) {
            return isDisenchantableInput(input);
        }

        if (variant.kind() == DecompositionKind.DURABILITY_SCALED) {
            return isDurabilityScaledInput(input);
        }

        return isCleanStack(input);
    }

    public static @Nullable DecompositionVariant firstDecompositionVariant(Level level, ItemStack input) {
        List<DecompositionVariant> variants = findDecompositionVariants(level, input, 1);

        return variants.isEmpty() ? null : variants.getFirst();
    }

    public static List<DecompositionVariant> findDecompositionVariants(
            Level level,
            ItemStack input,
            int limit
    ) {
        if (level.getServer() == null || !isCleanStack(input) || limit <= 0) {
            if (limit <= 0) {
                return List.of();
            }

            if (isDisenchantableInput(input)) {
                DecompositionVariant variant = buildDisenchantVariant(input);
                return variant == null ? List.of() : List.of(variant);
            }

            if (isDurabilityScaledInput(input)) {
                return findDurabilityScaledVariants(level, input, limit);
            }

            return List.of();
        }

        List<DecompositionVariant> variants = new ArrayList<>();

        for (RecipeHolder<CraftingRecipe> holder : level.getServer()
                .getRecipeManager()
                .recipeMap()
                .byType(RecipeType.CRAFTING)) {
            CraftingRecipe recipe = holder.value();

            if (!isSupportedOrdinaryCraftingRecipe(recipe)) {
                continue;
            }

            ItemStack result = recipe.assemble(CraftingInput.EMPTY);

            if (!isCleanStack(result)) {
                continue;
            }

            if (!ItemStack.isSameItemSameComponents(input, result)) {
                continue;
            }

            for (NonNullList<ItemStack> outputs : buildOutputVariants(recipe)) {
                ScaledOutputs scaledOutputs = scaleOutputsForResultCount(outputs, result.getCount());

                if (scaledOutputs == null || input.getCount() < scaledOutputs.inputCost()) {
                    continue;
                }

                variants.add(new DecompositionVariant(
                        recipe,
                        DecompositionKind.NORMAL,
                        scaledOutputs.inputCost(),
                        scaledOutputs.outputs(),
                        List.of()
                ));

                if (variants.size() >= limit) {
                    return variants;
                }
            }
        }

        return variants;
    }

    private static List<DecompositionVariant> findDurabilityScaledVariants(
            Level level,
            ItemStack input,
            int limit
    ) {
        if (level.getServer() == null) {
            return List.of();
        }

        List<DecompositionVariant> variants = new ArrayList<>();

        for (RecipeHolder<CraftingRecipe> holder : level.getServer()
                .getRecipeManager()
                .recipeMap()
                .byType(RecipeType.CRAFTING)) {
            CraftingRecipe recipe = holder.value();

            if (!isSupportedOrdinaryCraftingRecipe(recipe)) {
                continue;
            }

            ItemStack result = recipe.assemble(CraftingInput.EMPTY);

            if (!isCleanStack(result) || !input.is(result.getItem())) {
                continue;
            }

            for (NonNullList<ItemStack> outputs : buildOutputVariants(recipe)) {
                ScaledOutputs recipeScaledOutputs = scaleOutputsForResultCount(outputs, result.getCount());

                if (recipeScaledOutputs == null || input.getCount() < recipeScaledOutputs.inputCost()) {
                    continue;
                }

                NonNullList<ItemStack> scaledOutputs = scaleOutputsByDurability(recipeScaledOutputs.outputs(), input);

                if (scaledOutputs == null) {
                    continue;
                }

                variants.add(new DecompositionVariant(
                        recipe,
                        DecompositionKind.DURABILITY_SCALED,
                        recipeScaledOutputs.inputCost(),
                        scaledOutputs,
                        List.of()
                ));

                if (variants.size() >= limit) {
                    return variants;
                }
            }
        }

        return variants;
    }

    private static boolean isDisenchantableInput(ItemStack input) {
        if (input.isEmpty() || input.getCount() != 1 || input.is(Items.ENCHANTED_BOOK)) {
            return false;
        }

        if (!hasOnlyAllowedInputComponents(
                input,
                component -> component == DataComponents.ENCHANTMENTS
                        || component == DataComponents.DAMAGE
                        || component == DataComponents.REPAIR_COST
                        || component == DataComponents.ENCHANTMENT_GLINT_OVERRIDE
        )) {
            return false;
        }

        return !EnchantmentHelper.getEnchantmentsForCrafting(input).isEmpty();
    }

    private static boolean isDurabilityScaledInput(ItemStack input) {
        if (input.isEmpty() || input.getCount() != 1 || !input.isDamaged()) {
            return false;
        }

        if (EnchantmentHelper.hasAnyEnchantments(input)) {
            return false;
        }

        return hasOnlyAllowedInputComponents(input, component -> component == DataComponents.DAMAGE);
    }

    private static boolean hasOnlyAllowedInputComponents(
            ItemStack input,
            Predicate<DataComponentType<?>> allowedComponent
    ) {
        return input.getComponentsPatch()
                .entrySet()
                .stream()
                .allMatch(entry -> allowedComponent.test(entry.getKey()));
    }

    private static @Nullable DecompositionVariant buildDisenchantVariant(ItemStack input) {
        ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(input);

        if (enchantments.isEmpty()) {
            return null;
        }

        NonNullList<ItemStack> outputs = NonNullList.withSize(OUTPUT_SLOT_COUNT, ItemStack.EMPTY);
        List<ItemStack> extraOutputs = new ArrayList<>();
        ItemStack baseItem = input.copyWithCount(1);

        baseItem.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        baseItem.set(DataComponents.REPAIR_COST, 0);
        baseItem.remove(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);

        outputs.set(0, baseItem);

        int outputSlot = 1;

        for (it.unimi.dsi.fastutil.objects.Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            ItemStack enchantedBook = createEnchantedBook(entry.getKey(), entry.getIntValue());

            if (outputSlot < OUTPUT_SLOT_COUNT) {
                outputs.set(outputSlot, enchantedBook);
                outputSlot++;
            } else {
                extraOutputs.add(enchantedBook);
            }
        }

        return new DecompositionVariant(
                null,
                DecompositionKind.DISENCHANT,
                1,
                outputs,
                List.copyOf(extraOutputs)
        );
    }

    private static ItemStack createEnchantedBook(Holder<Enchantment> enchantment, int level) {
        ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);

        enchantments.set(enchantment, level);
        EnchantmentHelper.setEnchantments(book, enchantments.toImmutable());

        return book;
    }

    private static @Nullable NonNullList<ItemStack> scaleOutputsByDurability(
            NonNullList<ItemStack> outputs,
            ItemStack input
    ) {
        int maxDamage = input.getMaxDamage();

        if (maxDamage <= 0) {
            return null;
        }

        int remainingDurability = Math.max(0, maxDamage - input.getDamageValue());

        if (remainingDurability <= 0) {
            return null;
        }

        Map<Item, Integer> countsByItem = new LinkedHashMap<>();

        for (ItemStack output : outputs) {
            if (output.isEmpty()) {
                continue;
            }

            countsByItem.merge(output.getItem(), output.getCount(), Integer::sum);
        }

        NonNullList<ItemStack> scaledOutputs = NonNullList.withSize(OUTPUT_SLOT_COUNT, ItemStack.EMPTY);
        int slot = 0;

        for (Map.Entry<Item, Integer> entry : countsByItem.entrySet()) {
            int scaledCount = (int)Math.round((double)entry.getValue() * remainingDurability / (double)maxDamage);
            scaledCount = Math.min(entry.getValue(), scaledCount);

            if (scaledCount <= 0) {
                continue;
            }

            scaledOutputs.set(slot, new ItemStack(entry.getKey(), scaledCount));
            slot++;

            if (slot >= OUTPUT_SLOT_COUNT) {
                break;
            }
        }

        return slot == 0 ? null : scaledOutputs;
    }

    private static @Nullable ScaledOutputs scaleOutputsForResultCount(
            NonNullList<ItemStack> outputs,
            int resultCount
    ) {
        if (resultCount <= 0) {
            return null;
        }

        if (resultCount == 1) {
            return new ScaledOutputs(1, copyOutputs(outputs));
        }

        Map<Item, Integer> countsByItem = aggregateOutputs(outputs);

        if (countsByItem.isEmpty()) {
            return null;
        }

        int inputCost = 1;

        for (int count : countsByItem.values()) {
            int divisor = resultCount / gcd(resultCount, count);
            inputCost = lcm(inputCost, divisor);
        }

        if (inputCost <= 0 || inputCost > resultCount) {
            return null;
        }

        NonNullList<ItemStack> scaledOutputs = NonNullList.withSize(OUTPUT_SLOT_COUNT, ItemStack.EMPTY);
        int slot = 0;

        for (Map.Entry<Item, Integer> entry : countsByItem.entrySet()) {
            int scaledCount = entry.getValue() * inputCost / resultCount;

            if (scaledCount <= 0) {
                continue;
            }

            scaledOutputs.set(slot, new ItemStack(entry.getKey(), scaledCount));
            slot++;

            if (slot >= OUTPUT_SLOT_COUNT) {
                break;
            }
        }

        return slot == 0 ? null : new ScaledOutputs(inputCost, scaledOutputs);
    }

    private static Map<Item, Integer> aggregateOutputs(NonNullList<ItemStack> outputs) {
        Map<Item, Integer> countsByItem = new LinkedHashMap<>();

        for (ItemStack output : outputs) {
            if (output.isEmpty()) {
                continue;
            }

            countsByItem.merge(output.getItem(), output.getCount(), Integer::sum);
        }

        return countsByItem;
    }

    private static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);

        while (b != 0) {
            int temp = a % b;
            a = b;
            b = temp;
        }

        return a;
    }

    private static int lcm(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }

        return Math.abs(a / gcd(a, b) * b);
    }

    public static @Nullable SubstitutionVariant firstSubstitutionVariant(
            Level level,
            CraftingInput input,
            int substitutionSlot,
            Predicate<ItemStack> materialFilter
    ) {
        if (level.getServer() == null || substitutionSlot < 0 || substitutionSlot >= input.size()) {
            return null;
        }

        for (RecipeHolder<CraftingRecipe> holder : level.getServer()
                .getRecipeManager()
                .recipeMap()
                .byType(RecipeType.CRAFTING)) {
            CraftingRecipe recipe = holder.value();

            if (!isSupportedOrdinaryCraftingRecipe(recipe)) {
                continue;
            }

            SubstitutionVariant variant = findRecipeSubstitutionVariant(
                    recipe,
                    input,
                    substitutionSlot,
                    level,
                    materialFilter
            );

            if (variant != null) {
                return variant;
            }
        }

        return null;
    }

    private static @Nullable SubstitutionVariant findRecipeSubstitutionVariant(
            CraftingRecipe recipe,
            CraftingInput input,
            int substitutionSlot,
            Level level,
            Predicate<ItemStack> materialFilter
    ) {
        for (Ingredient ingredient : recipe.placementInfo().ingredients()) {
            for (ItemStack material : materialChoices(ingredient)) {
                if (!materialFilter.test(material)) {
                    continue;
                }

                List<ItemStack> substitutedItems = new ArrayList<>(input.size());

                for (ItemStack stack : input.items()) {
                    substitutedItems.add(stack.copy());
                }

                substitutedItems.set(substitutionSlot, material.copyWithCount(1));

                CraftingInput substitutedInput = CraftingInput.of(
                        input.width(),
                        input.height(),
                        substitutedItems
                );

                if (!recipe.matches(substitutedInput, level)) {
                    continue;
                }

                ItemStack result = recipe.assemble(substitutedInput);

                if (!result.isEmpty()) {
                    return new SubstitutionVariant(recipe, substitutedInput, material.copyWithCount(1));
                }
            }
        }

        return null;
    }

    private static boolean isSupportedOrdinaryCraftingRecipe(CraftingRecipe recipe) {
        if (!(recipe instanceof ShapedRecipe) && !(recipe instanceof ShapelessRecipe)) {
            return false;
        }

        PlacementInfo placementInfo = recipe.placementInfo();

        return !placementInfo.isImpossibleToPlace()
                && placementInfo.ingredients().size() <= OUTPUT_SLOT_COUNT;
    }

    private static List<NonNullList<ItemStack>> buildOutputVariants(CraftingRecipe recipe) {
        List<List<ItemStack>> choicesBySlot = new ArrayList<>(OUTPUT_SLOT_COUNT);

        for (int slot = 0; slot < OUTPUT_SLOT_COUNT; slot++) {
            choicesBySlot.add(List.of(ItemStack.EMPTY));
        }

        if (recipe instanceof ShapedRecipe shapedRecipe) {
            List<Optional<Ingredient>> ingredients = shapedRecipe.getIngredients();
            int width = shapedRecipe.getWidth();
            int height = shapedRecipe.getHeight();

            for (int row = 0; row < height; row++) {
                for (int column = 0; column < width; column++) {
                    int ingredientIndex = column + row * width;
                    int outputIndex = column + row * 3;
                    Optional<Ingredient> ingredient = ingredients.get(ingredientIndex);

                    if (ingredient.isEmpty()) {
                        continue;
                    }

                    List<ItemStack> choices = materialChoices(ingredient.get());

                    if (choices.isEmpty()) {
                        return List.of();
                    }

                    choicesBySlot.set(outputIndex, choices);
                }
            }
        } else if (recipe instanceof ShapelessRecipe) {
            List<Ingredient> ingredients = recipe.placementInfo().ingredients();

            for (int index = 0; index < ingredients.size(); index++) {
                List<ItemStack> choices = materialChoices(ingredients.get(index));

                if (choices.isEmpty()) {
                    return List.of();
                }

                choicesBySlot.set(index, choices);
            }
        }

        List<NonNullList<ItemStack>> variants = new ArrayList<>();
        appendOutputVariants(choicesBySlot, 0, NonNullList.withSize(OUTPUT_SLOT_COUNT, ItemStack.EMPTY), variants);

        return variants;
    }

    private static void appendOutputVariants(
            List<List<ItemStack>> choicesBySlot,
            int slot,
            NonNullList<ItemStack> current,
            List<NonNullList<ItemStack>> variants
    ) {
        if (variants.size() >= MAX_VARIANTS_PER_RECIPE) {
            return;
        }

        if (slot >= choicesBySlot.size()) {
            variants.add(copyOutputs(current));
            return;
        }

        for (ItemStack choice : choicesBySlot.get(slot)) {
            current.set(slot, choice.copy());
            appendOutputVariants(choicesBySlot, slot + 1, current, variants);

            if (variants.size() >= MAX_VARIANTS_PER_RECIPE) {
                return;
            }
        }
    }

    private static NonNullList<ItemStack> copyOutputs(NonNullList<ItemStack> outputs) {
        NonNullList<ItemStack> copy = NonNullList.withSize(outputs.size(), ItemStack.EMPTY);

        for (int slot = 0; slot < outputs.size(); slot++) {
            copy.set(slot, outputs.get(slot).copy());
        }

        return copy;
    }

    private static List<ItemStack> materialChoices(Ingredient ingredient) {
        if (ingredient.isEmpty() || ingredient.isCustom()) {
            return List.of();
        }

        List<ItemStack> choices = new ArrayList<>();
        Set<Item> seenItems = new HashSet<>();

        for (Holder<Item> item : ingredient.items().toList()) {
            if (!seenItems.add(item.value())) {
                continue;
            }

            ItemStack stack = new ItemStack(item);

            if (!isSafeMaterialChoice(stack)) {
                continue;
            }

            choices.add(stack);

            if (choices.size() >= MAX_MATERIAL_CHOICES_PER_INGREDIENT) {
                break;
            }
        }

        return choices;
    }

    private static boolean isSafeMaterialChoice(ItemStack stack) {
        return isCleanStack(stack) && stack.getCraftingRemainder() == null;
    }

    public enum DecompositionKind {
        NORMAL,
        DISENCHANT,
        DURABILITY_SCALED
    }

    public record DecompositionVariant(
            @Nullable CraftingRecipe recipe,
            DecompositionKind kind,
            int inputCost,
            NonNullList<ItemStack> outputs,
            List<ItemStack> extraOutputs
    ) {
    }

    private record ScaledOutputs(
            int inputCost,
            NonNullList<ItemStack> outputs
    ) {
    }

    public record SubstitutionVariant(
            CraftingRecipe recipe,
            CraftingInput input,
            ItemStack substitutedMaterial
    ) {

        public ItemStack assemble() {
            return this.recipe.assemble(this.input).copy();
        }
    }
}
