package com.px8042.eightwastelands.event;

import com.px8042.eightwastelands.item.fabao.IFabaoItem;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class FabaoCurioEvents {

    public static final String SLOT_ID = "fabao";

    private static final int CHECK_INTERVAL_TICKS = 5;
    private static final String COOLDOWN_END_KEY = "eightwastelands_fabao_cooldown_end";

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        long gameTime = player.level().getGameTime();
        if (gameTime % CHECK_INTERVAL_TICKS != 0L) {
            return;
        }

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory ->
                curiosInventory.getStacksHandler(SLOT_ID).ifPresent(stacksHandler -> {
                    var stacks = stacksHandler.getStacks();

                    for (int slot = 0; slot < stacks.getSlots(); slot++) {
                        ItemStack fabaoStack = stacks.getStackInSlot(slot);
                        if (!(fabaoStack.getItem() instanceof IFabaoItem fabaoItem)) {
                            continue;
                        }

                        if (!isReady(fabaoStack, gameTime)) {
                            continue;
                        }

                        List<Monster> targets = findTargets(
                                player,
                                fabaoItem.getAttackRange(fabaoStack),
                                fabaoItem.getMaxPierceTargets(fabaoStack)
                        );

                        if (!targets.isEmpty()) {
                            attack(player, fabaoStack, fabaoItem, targets, gameTime);
                        }
                    }
                })
        );
    }

    private static long getCooldownEnd(ItemStack fabaoStack) {
        CustomData customData = fabaoStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return customData.copyTag().getLongOr(COOLDOWN_END_KEY, 0L);
    }

    private static boolean isReady(ItemStack fabaoStack, long gameTime) {
        return gameTime >= getCooldownEnd(fabaoStack);
    }

    private static void setCooldownEnd(ItemStack fabaoStack, long cooldownEnd) {
        if (fabaoStack.isEmpty()) {
            return;
        }

        CustomData.update(DataComponents.CUSTOM_DATA, fabaoStack, tag -> updateCooldownTag(tag, cooldownEnd));
    }

    private static void updateCooldownTag(CompoundTag tag, long cooldownEnd) {
        if (cooldownEnd <= 0L) {
            tag.remove(COOLDOWN_END_KEY);
        } else {
            tag.putLong(COOLDOWN_END_KEY, cooldownEnd);
        }
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
            setCooldownEnd(fabaoStack, gameTime + fabaoItem.getCooldownTicks(fabaoStack));
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
