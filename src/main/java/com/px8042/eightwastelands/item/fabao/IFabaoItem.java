package com.px8042.eightwastelands.item.fabao;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;

public interface IFabaoItem {

    int DEFAULT_COOLDOWN_TICKS = 5 * 20;
    double DEFAULT_ATTACK_RANGE = 16.0D;

    float getBaseDamage(ItemStack stack);

    default int getCooldownTicks(ItemStack stack) {
        return DEFAULT_COOLDOWN_TICKS;
    }

    default double getAttackRange(ItemStack stack) {
        return DEFAULT_ATTACK_RANGE;
    }

    default String getGrade(ItemStack stack) {
        return "none";
    }

    default void onAutoAttack(ServerPlayer player, ItemStack stack, Monster target) {
    }
}
