package com.px8042.eightwastelands.item.artifact;

import net.minecraft.world.item.ItemStack;

public interface IGhostArtifactItem {

    String SLOT_ID = "guiqi";

    int GHOST_REPAIR_INTERVAL = 20;

    int GHOST_REPAIR_AMOUNT = 10;

    int EXPERIENCE_COST_PER_REPAIR = 1;

    float HEALTH_COST_PER_REPAIR = 1.0F;

    float MIN_HEALTH_AFTER_REPAIR = 1.0F;

    default boolean canUseGhostArtifact(ItemStack stack) {
        return !ArtifactDurabilityHelper.isBroken(stack);
    }
}