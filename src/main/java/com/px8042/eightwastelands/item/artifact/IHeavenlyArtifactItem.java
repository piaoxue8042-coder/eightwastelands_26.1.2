package com.px8042.eightwastelands.item.artifact;

import net.minecraft.world.item.ItemStack;

public interface IHeavenlyArtifactItem {

    String SLOT_ID = "xianqi";

    default boolean canUseHeavenlyArtifact(ItemStack stack) {
        return !ArtifactDurabilityHelper.isBroken(stack);
    }
}