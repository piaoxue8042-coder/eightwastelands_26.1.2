package com.px8042.eightwastelands.item.fabao;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;

public interface IFabaoItem {

    int DEFAULT_COOLDOWN_TICKS = 5 * 20;
    double DEFAULT_ATTACK_RANGE = 16.0D;

    float getBaseDamage(ItemStack stack);

    FabaoType getFabaoType(ItemStack stack);

    default int getCooldownTicks(ItemStack stack) {
        return DEFAULT_COOLDOWN_TICKS;
    }

    default double getAttackRange(ItemStack stack) {
        return DEFAULT_ATTACK_RANGE;
    }

    default int getAttackCount(ItemStack stack) {
        return 1;
    }

    default int getMaxPierceTargets(ItemStack stack) {
        return 1;
    }

    default String getGrade(ItemStack stack) {
        return "none";
    }

    default DamageSource createDamageSource(ServerLevel level, ServerPlayer player, ItemStack stack, Monster target) {
        return level.damageSources().playerAttack(player);
    }

    default float modifyBaseDamage(ServerPlayer player, ItemStack stack, Monster target, float damage) {
        return damage;
    }

    default float modifyFinalDamage(ServerPlayer player, ItemStack stack, Monster target, DamageSource source, float damage) {
        return damage;
    }

    default void beforeAutoAttack(ServerPlayer player, ItemStack stack, Monster target) {
    }

    default void onAutoAttack(ServerPlayer player, ItemStack stack, Monster target) {
    }

    default void afterAutoAttack(ServerPlayer player, ItemStack stack, Monster target, DamageSource source, float damage) {
    }
}
