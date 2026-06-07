package com.px8042.eightwastelands.item.artifact;

import net.minecraft.world.item.ItemStack;

public final class ArtifactDurabilityHelper {

    private ArtifactDurabilityHelper() {
    }

    public static boolean hasDurability(ItemStack stack) {

        return !stack.isEmpty()
                && stack.isDamageableItem()
                && stack.getMaxDamage() > 0;
    }

    public static boolean isDamaged(ItemStack stack) {

        if (!hasDurability(stack)) {
            return false;
        }

        return stack.getDamageValue() > 0;
    }

    public static boolean isBroken(ItemStack stack) {

        if (!hasDurability(stack)) {
            return false;
        }

        return stack.getDamageValue() >= stack.getMaxDamage();
    }

    public static int getRemainingDurability(ItemStack stack) {

        if (!hasDurability(stack)) {
            return 0;
        }

        return Math.max(0, stack.getMaxDamage() - stack.getDamageValue());
    }

    public static void damageArtifact(ItemStack stack, int amount) {

        if (!hasDurability(stack)) {
            return;
        }

        if (amount <= 0) {
            return;
        }

        int currentDamage = stack.getDamageValue();
        int maxDamage = stack.getMaxDamage();

        int newDamage = Math.min(maxDamage, currentDamage + amount);

        stack.setDamageValue(newDamage);
    }

    public static void repairArtifact(ItemStack stack, int amount) {

        if (!hasDurability(stack)) {
            return;
        }

        if (amount <= 0) {
            return;
        }

        int currentDamage = stack.getDamageValue();

        int newDamage = Math.max(0, currentDamage - amount);

        stack.setDamageValue(newDamage);
    }
}