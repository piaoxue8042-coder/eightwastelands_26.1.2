package com.px8042.eightwastelands.recipe;

import com.mojang.serialization.MapCodec;
import com.px8042.eightwastelands.item.ModItems;
import com.px8042.eightwastelands.item.artifact.ArtifactDurabilityHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class GhostBrainSubstitutionRecipe extends CustomRecipe {

    public static final GhostBrainSubstitutionRecipe INSTANCE = new GhostBrainSubstitutionRecipe();
    public static final MapCodec<GhostBrainSubstitutionRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, GhostBrainSubstitutionRecipe> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);

    private CraftingInput cachedInput;
    private CraftingVariantResolver.SubstitutionVariant cachedSubstitution;

    @Override
    public boolean matches(CraftingInput input, Level level) {
        this.cachedInput = input;
        this.cachedSubstitution = findSubstitution(input, level);

        return this.cachedSubstitution != null;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        CraftingVariantResolver.SubstitutionVariant substitution = this.cachedSubstitution;

        if (substitution == null || this.cachedInput == null || !this.cachedInput.equals(input)) {
            return ItemStack.EMPTY;
        }

        return substitution.assemble();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int slot = 0; slot < result.size(); slot++) {
            ItemStack stack = input.getItem(slot);

            if (isUsableGhostBrain(stack)) {
                ItemStack brokenGhostBrain = stack.copyWithCount(1);
                brokenGhostBrain.setDamageValue(brokenGhostBrain.getMaxDamage());
                result.set(slot, brokenGhostBrain);
                continue;
            }

            ItemStackTemplate remainder = stack.getCraftingRemainder();
            result.set(slot, remainder != null ? remainder.create() : ItemStack.EMPTY);
        }

        return result;
    }

    @Override
    public RecipeSerializer<GhostBrainSubstitutionRecipe> getSerializer() {
        return ModRecipeSerializers.GHOST_BRAIN_SUBSTITUTION.get();
    }

    private static CraftingVariantResolver.SubstitutionVariant findSubstitution(CraftingInput input, Level level) {
        int ghostBrainSlot = findGhostBrainSlot(input);

        if (ghostBrainSlot < 0) {
            return null;
        }

        return CraftingVariantResolver.firstSubstitutionVariant(
                level,
                input,
                ghostBrainSlot,
                material -> !material.is(ModItems.GHOST_BRAIN.get())
        );
    }

    private static int findGhostBrainSlot(CraftingInput input) {
        int ghostBrainSlot = -1;

        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);

            if (stack.isEmpty()) {
                continue;
            }

            if (!stack.is(ModItems.GHOST_BRAIN.get())) {
                continue;
            }

            if (!isUsableGhostBrain(stack)) {
                return -1;
            }

            if (ghostBrainSlot != -1) {
                return -1;
            }

            ghostBrainSlot = slot;
        }

        return ghostBrainSlot;
    }

    private static boolean isUsableGhostBrain(ItemStack stack) {
        return stack.is(ModItems.GHOST_BRAIN.get())
                && stack.getCount() == 1
                && !ArtifactDurabilityHelper.isBroken(stack);
    }

}
