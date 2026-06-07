package com.px8042.eightwastelands.item.artifact;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public abstract class AbstractHeavenlyArtifactItem extends Item
        implements ICurioItem, IHeavenlyArtifactItem {

    public AbstractHeavenlyArtifactItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.identifier().equals(IHeavenlyArtifactItem.SLOT_ID);
    }

    public boolean isArtifactBroken(ItemStack stack) {
        return ArtifactDurabilityHelper.isBroken(stack);
    }

    public boolean canUseArtifact(ItemStack stack) {
        return !ArtifactDurabilityHelper.isBroken(stack);
    }

    public void damageArtifact(ItemStack stack, int amount) {
        ArtifactDurabilityHelper.damageArtifact(stack, amount);
    }

    public void repairArtifact(ItemStack stack, int amount) {
        ArtifactDurabilityHelper.repairArtifact(stack, amount);
    }
}