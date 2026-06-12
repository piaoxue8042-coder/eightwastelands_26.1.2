package com.px8042.eightwastelands.recipe;

import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, EightWastelands.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<GhostBrainSubstitutionRecipe>>
            GHOST_BRAIN_SUBSTITUTION =
            RECIPE_SERIALIZERS.register(
                    "ghost_brain_substitution",
                    () -> new RecipeSerializer<>(
                            GhostBrainSubstitutionRecipe.MAP_CODEC,
                            GhostBrainSubstitutionRecipe.STREAM_CODEC
                    )
            );

    private ModRecipeSerializers() {
    }

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
