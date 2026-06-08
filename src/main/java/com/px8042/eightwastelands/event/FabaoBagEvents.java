package com.px8042.eightwastelands.event;

import com.px8042.eightwastelands.item.ModItems;
import com.px8042.eightwastelands.item.fabao.FabaoBagItem;
import com.px8042.eightwastelands.item.fabao.IFabaoItem;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class FabaoBagEvents {

    private static final int CHECK_INTERVAL_TICKS = 5;

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        long gameTime = player.level().getGameTime();
        if (gameTime % CHECK_INTERVAL_TICKS != 0L) {
            return;
        }

        Optional<ItemStack> equippedBag = findEquippedBag(player);
        if (equippedBag.isEmpty()) {
            return;
        }

        ItemStack bagStack = equippedBag.get();
        NonNullList<ItemStack> contents = FabaoBagItem.getContents(bagStack);
        for (int slot = 0; slot < contents.size(); slot++) {
            ItemStack fabaoStack = contents.get(slot);
            if (!(fabaoStack.getItem() instanceof IFabaoItem fabaoItem)) {
                continue;
            }

            if (!FabaoBagItem.isSlotReady(bagStack, slot, gameTime)) {
                continue;
            }

            List<Monster> targets = findTargets(
                    player,
                    fabaoItem.getAttackRange(fabaoStack),
                    fabaoItem.getMaxPierceTargets(fabaoStack)
            );
            if (!targets.isEmpty()) {
                attack(player, bagStack, slot, fabaoStack, fabaoItem, targets, gameTime);
            }
        }
    }

    private Optional<ItemStack> findEquippedBag(ServerPlayer player) {
        AtomicReference<ItemStack> foundStack = new AtomicReference<>(ItemStack.EMPTY);

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory ->
                curiosInventory.getStacksHandler(FabaoBagItem.SLOT_ID).ifPresent(stacksHandler -> {
                    for (int slot = 0; slot < stacksHandler.getStacks().getSlots(); slot++) {
                        ItemStack stack = stacksHandler.getStacks().getStackInSlot(slot);
                        if (stack.is(ModItems.FABAO_BAG.get())) {
                            foundStack.set(stack);
                            return;
                        }
                    }
                })
        );

        ItemStack stack = foundStack.get();
        return stack.isEmpty() ? Optional.empty() : Optional.of(stack);
    }

    private List<Monster> findTargets(ServerPlayer player, double range, int maxTargets) {
        double rangeSqr = range * range;
        return player.level()
                .getEntitiesOfClass(
                        Monster.class,
                        player.getBoundingBox().inflate(range),
                        monster -> monster.isAlive() && player.distanceToSqr(monster) <= rangeSqr
                )
                .stream()
                .sorted(Comparator.comparingDouble(player::distanceToSqr))
                .limit(Math.max(1, maxTargets))
                .toList();
    }

    private void attack(
            ServerPlayer player,
            ItemStack bagStack,
            int bagSlot,
            ItemStack fabaoStack,
            IFabaoItem fabaoItem,
            List<Monster> targets,
            long gameTime
    ) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        boolean hitAnyTarget = false;
        int attackCount = Math.max(1, fabaoItem.getAttackCount(fabaoStack));

        for (Monster target : targets) {
            for (int attack = 0; attack < attackCount; attack++) {
                if (!target.isAlive()) {
                    break;
                }

                if (attackSingleTarget(serverLevel, player, fabaoStack, fabaoItem, target)) {
                    hitAnyTarget = true;
                }
            }
        }

        if (hitAnyTarget) {
            FabaoBagItem.setCooldownEnd(bagStack, bagSlot, gameTime + fabaoItem.getCooldownTicks(fabaoStack));
        }
    }

    private boolean attackSingleTarget(
            ServerLevel serverLevel,
            ServerPlayer player,
            ItemStack fabaoStack,
            IFabaoItem fabaoItem,
            Monster target
    ) {
        fabaoItem.beforeAutoAttack(player, fabaoStack, target);

        DamageSource source = fabaoItem.createDamageSource(serverLevel, player, fabaoStack, target);
        float baseDamage = fabaoItem.modifyBaseDamage(
                player,
                fabaoStack,
                target,
                fabaoItem.getBaseDamage(fabaoStack)
        );
        float damage = EnchantmentHelper.modifyDamage(
                serverLevel,
                fabaoStack,
                target,
                source,
                baseDamage
        );
        damage = fabaoItem.modifyFinalDamage(player, fabaoStack, target, source, damage);

        if (damage <= 0.0F) {
            return false;
        }

        target.invulnerableTime = 0;
        if (!target.hurtServer(serverLevel, source, damage)) {
            return false;
        }

        EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, target, source, fabaoStack);
        fabaoItem.onAutoAttack(player, fabaoStack, target);
        fabaoItem.afterAutoAttack(player, fabaoStack, target, source, damage);
        return true;
    }
}
