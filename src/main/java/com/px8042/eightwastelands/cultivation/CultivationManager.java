package com.px8042.eightwastelands.cultivation;

import com.px8042.eightwastelands.alchemy.AlchemyManager;
import com.px8042.eightwastelands.item.pill.AbstractBreakthroughPillItem;
import com.px8042.eightwastelands.item.cultivation.CultivationGearManager;
import com.px8042.eightwastelands.network.CultivationSyncPayload;
import com.px8042.eightwastelands.refining.RefiningManager;
import com.px8042.eightwastelands.xinfa.XinfaManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;

public class CultivationManager {

    public static final int BASE_TUNAI_DAILY_USES = 5;
    public static final int TUNAI_QI_GAIN = 10;
    public static final double BREAKTHROUGH_PILL_CHANCE_BONUS = 0.25D;
    public static final double BREAKTHROUGH_FAILURE_CHANCE_STEP = 0.05D;
    public static final long BREAKTHROUGH_PILL_COOLDOWN_REDUCTION_TICKS = 2L * 60L * 20L;
    public static final int ZHENYUAN_SCALE = 100;

    public static final String STAGE = "eightwastelands_cultivation_stage";
    public static final String QI = "eightwastelands_cultivation_qi";
    public static final String ZHENYUAN = "eightwastelands_zhenyuan";
    private static final String LEGACY_SPIRITUAL_POWER = "eightwastelands_spiritual_power";
    private static final String LEGACY_BREAKTHROUGH_PILL_SLOT = "eightwastelands_breakthrough_pill_slot";
    public static final String TUNAI_USED_TODAY = "eightwastelands_tunai_used_today";
    public static final String TUNAI_LAST_DAY = "eightwastelands_tunai_last_day";
    public static final String TUNAI_EXTRA_DAILY_USES = "eightwastelands_tunai_extra_daily_uses";
    public static final String BREAKTHROUGH_COOLDOWN_END = "eightwastelands_breakthrough_cooldown_end";
    public static final String BREAKTHROUGH_FAILURE_COUNT = "eightwastelands_breakthrough_failure_count";
    private static final String SAVED_HEALTH = "eightwastelands_saved_health";

    private CultivationManager() {
    }

    public static void handleMainButton(ServerPlayer player) {
        if (getStage(player).isMortal()) {
            handleInduceQi(player);
        } else {
            handleTunai(player);
        }
    }

    public static void handleInduceQi(ServerPlayer player) {
        refreshDailyTunai(player);

        if (!getStage(player).isMortal()) {
            syncTo(player);
            return;
        }

        setStage(player, CultivationStage.QI_REFINING_EARLY);
        setQi(player, 0);
        setZhenyuanToMax(player, CultivationStage.QI_REFINING_EARLY);
        resetBreakthroughFailureCount(player);
        applyAttributes(player);
        XinfaManager.applyAttributes(player);
        CultivationGearManager.applyAttributes(player);
        syncTo(player);
        player.sendOverlayMessage(Component.translatable("message.eightwastelands.cultivation.induced_qi"));
    }

    public static void handleTunai(ServerPlayer player) {
        refreshDailyTunai(player);

        if (getStage(player).isMortal()) {
            syncTo(player);
            return;
        }

        if (getRemainingTunaiUses(player) <= 0) {
            player.sendOverlayMessage(Component.translatable("message.eightwastelands.cultivation.tunai_empty"));
            syncTo(player);
            return;
        }

        int qiGain = getTunaiQiGain(player);
        setTunaiUsedToday(player, getTunaiUsedToday(player) + 1);
        setQi(player, getQi(player) + qiGain);
        syncTo(player);
        player.sendOverlayMessage(Component.translatable("message.eightwastelands.cultivation.tunai_gain", qiGain));
    }

    public static void handleBreakthrough(ServerPlayer player) {
        refreshDailyTunai(player);

        CultivationStage stage = getStage(player);

        if (stage.isMortal()) {
            syncTo(player);
            return;
        }

        if (stage.isMaxStage()) {
            player.sendOverlayMessage(Component.translatable("message.eightwastelands.cultivation.max_stage"));
            syncTo(player);
            return;
        }

        if (getQi(player) < stage.getRequiredQi()) {
            player.sendOverlayMessage(Component.translatable("message.eightwastelands.cultivation.not_enough_qi"));
            syncTo(player);
            return;
        }

        if (getBreakthroughCooldownTicks(player) > 0L) {
            sendBreakthroughCooldownMessage(player);
            syncTo(player);
            return;
        }

        attemptBreakthrough(player, stage, 0.0D);
    }

    public static void handleBreakthroughWithPill(ServerPlayer player) {
        handleBreakthroughWithPill(player, BREAKTHROUGH_PILL_CHANCE_BONUS);
    }

    public static void handleBreakthroughWithPill(ServerPlayer player, double pillChanceBonus) {
        refreshDailyTunai(player);

        CultivationStage stage = getStage(player);

        if (stage.isMortal()) {
            syncTo(player);
            return;
        }

        if (stage.isMaxStage()) {
            player.sendOverlayMessage(Component.translatable("message.eightwastelands.cultivation.max_stage"));
            syncTo(player);
            return;
        }

        if (getQi(player) < stage.getRequiredQi()) {
            player.sendOverlayMessage(Component.translatable("message.eightwastelands.cultivation.not_enough_qi"));
            syncTo(player);
            return;
        }

        if (getBreakthroughCooldownTicks(player) > 0L) {
            sendBreakthroughCooldownMessage(player);
            syncTo(player);
            return;
        }

        attemptBreakthrough(player, stage, pillChanceBonus);
    }

    public static boolean canUseBreakthroughPill(ServerPlayer player, AbstractBreakthroughPillItem pillItem) {
        CultivationStage stage = getStage(player);
        Optional<CultivationRealm> requiredRealm = getRequiredBreakthroughPillRealm(stage);
        if (requiredRealm.isEmpty() || pillItem.getRealm().filter(requiredRealm.get()::equals).isEmpty()) {
            return false;
        }

        if (getBreakthroughCooldownTicks(player) > 0L) {
            return true;
        }

        return getQiAmount(player) >= stage.getRequiredQi();
    }

    public static CultivationSnapshot getSnapshot(ServerPlayer player) {
        refreshDailyTunai(player);
        return new CultivationSnapshot(
                getStage(player).getIndex(),
                getQi(player),
                getTunaiUsedToday(player),
                getDailyTunaiUses(player),
                getBreakthroughCooldownTicks(player),
                getBreakthroughFailureCount(player),
                getZhenyuanAmount(player),
                getMaxZhenyuanAmount(player),
                getZhenyuanRegenPerSecond(player),
                player.getHealth(),
                player.getMaxHealth(),
                CultivationGearManager.getHealthRegenPerSecond(player),
                player.getAttributeValue(Attributes.ATTACK_DAMAGE),
                player.getAttributeValue(Attributes.ATTACK_SPEED),
                player.getAttributeValue(Attributes.MOVEMENT_SPEED),
                AlchemyManager.getRealm(player).getIndex(),
                AlchemyManager.getMastery(player).getIndex(),
                AlchemyManager.getExperience(player),
                AlchemyManager.getRequiredExperience(player),
                RefiningManager.getRealm(player).getIndex(),
                RefiningManager.getMastery(player).getIndex(),
                RefiningManager.getExperience(player),
                RefiningManager.getRequiredExperience(player)
        );
    }

    public static void syncTo(ServerPlayer player) {
        CultivationSnapshot snapshot = getSnapshot(player);
        PacketDistributor.sendToPlayer(player, new CultivationSyncPayload(
                snapshot.stageIndex(),
                snapshot.qi(),
                snapshot.tunaiUsedToday(),
                snapshot.tunaiDailyUses(),
                snapshot.breakthroughCooldownTicks(),
                snapshot.breakthroughFailureCount(),
                snapshot.zhenyuan(),
                snapshot.maxZhenyuan(),
                snapshot.zhenyuanRegenPerSecond(),
                snapshot.health(),
                snapshot.maxHealth(),
                snapshot.healthRegenPerSecond(),
                snapshot.attackDamage(),
                snapshot.attackSpeed(),
                snapshot.movementSpeed(),
                snapshot.alchemyRealmIndex(),
                snapshot.alchemyMasteryIndex(),
                snapshot.alchemyExperience(),
                snapshot.alchemyRequiredExperience(),
                snapshot.refiningRealmIndex(),
                snapshot.refiningMasteryIndex(),
                snapshot.refiningExperience(),
                snapshot.refiningRequiredExperience()
        ));
    }

    public static void refreshPlayerState(ServerPlayer player) {
        refreshPlayerState(player, false);
    }

    public static void refreshPlayerState(ServerPlayer player, boolean restoreSavedHealth) {
        refreshDailyTunai(player);
        CultivationGearManager.initializeGearInstances(player);
        applyAttributes(player);
        XinfaManager.applyAttributes(player);
        CultivationGearManager.applyAttributes(player);
        maintainCultivationFood(player);
        if (restoreSavedHealth) {
            restoreSavedHealth(player);
        }

        syncTo(player);
    }

    public static void saveCurrentHealth(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        float health = player.getHealth();
        if (health <= 0.0F || !player.isAlive()) {
            data.remove(SAVED_HEALTH);
            return;
        }

        data.putFloat(SAVED_HEALTH, health);
    }

    public static void maintainCultivationFood(ServerPlayer player) {
        if (getStage(player).isMortal()) {
            return;
        }

        var foodData = player.getFoodData();
        if (foodData.getFoodLevel() < 20) {
            foodData.setFoodLevel(20);
        }

        if (foodData.getSaturationLevel() < 5.0F) {
            foodData.setSaturation(5.0F);
        }
    }

    public static void applyAttributes(ServerPlayer player) {
        CultivationStage stage = getStage(player);

        clearCultivationAttributeModifiers(player);
        for (CultivationAttributeBonus bonus : stage.getAttributeBonuses()) {
            applyAttributeBonus(player, bonus);
        }

        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }

        clampZhenyuan(player, stage);
    }

    public static void copyData(CompoundTag oldData, CompoundTag newData) {
        copyInt(oldData, newData, STAGE);
        copyQiData(oldData, newData);
        copyInt(oldData, newData, ZHENYUAN);
        copyInt(oldData, newData, TUNAI_USED_TODAY);
        copyLong(oldData, newData, TUNAI_LAST_DAY);
        copyInt(oldData, newData, TUNAI_EXTRA_DAILY_USES);
        copyLong(oldData, newData, BREAKTHROUGH_COOLDOWN_END);
        copyInt(oldData, newData, BREAKTHROUGH_FAILURE_COUNT);
    }

    public static CultivationStage getStage(ServerPlayer player) {
        return CultivationStage.byIndex(player.getPersistentData().getIntOr(STAGE, 0));
    }

    public static void setStageForCommand(ServerPlayer player, CultivationStage stage) {
        refreshDailyTunai(player);
        setStage(player, stage);
        setZhenyuanToMax(player, stage);
        resetBreakthroughFailureCount(player);
        applyAttributes(player);
        XinfaManager.applyAttributes(player);
        CultivationGearManager.applyAttributes(player);
        syncTo(player);
    }

    public static void addQi(ServerPlayer player, int amount) {
        refreshDailyTunai(player);
        setQi(player, getQi(player) + Math.max(0, amount));
        syncTo(player);
    }

    public static int getQiAmount(ServerPlayer player) {
        refreshDailyTunai(player);
        return getQi(player);
    }

    public static void resetTunaiForCommand(ServerPlayer player) {
        refreshDailyTunai(player);
        setTunaiUsedToday(player, 0);
        syncTo(player);
    }

    public static void clearForCommand(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        data.remove(STAGE);
        data.remove(QI);
        data.remove(ZHENYUAN);
        data.remove(LEGACY_BREAKTHROUGH_PILL_SLOT);
        data.remove(LEGACY_SPIRITUAL_POWER);
        data.remove(TUNAI_USED_TODAY);
        data.remove(TUNAI_LAST_DAY);
        data.remove(TUNAI_EXTRA_DAILY_USES);
        data.remove(BREAKTHROUGH_COOLDOWN_END);
        data.remove(BREAKTHROUGH_FAILURE_COUNT);
        applyAttributes(player);
        XinfaManager.applyAttributes(player);
        CultivationGearManager.applyAttributes(player);
        syncTo(player);
    }

    private static void attemptBreakthrough(
            ServerPlayer player,
            CultivationStage currentStage,
            double providedPillChanceBonus
    ) {
        double pillChanceBonus = Math.max(0.0D, providedPillChanceBonus);
        if (pillChanceBonus <= 0.0D) {
            pillChanceBonus = consumeBreakthroughPillFromInventory(player);
        }

        double chance = getBreakthroughChance(
                currentStage,
                getBreakthroughFailureCount(player),
                pillChanceBonus
        );

        if (player.getRandom().nextDouble() < chance) {
            CultivationStage nextStage = currentStage.next();
            setStage(player, nextStage);
            setQi(player, 0);
            setZhenyuanToMax(player, nextStage);
            resetBreakthroughFailureCount(player);
            applyAttributes(player);
            XinfaManager.applyAttributes(player);
            CultivationGearManager.applyAttributes(player);
            syncTo(player);
            player.sendOverlayMessage(Component.translatable(
                    "message.eightwastelands.cultivation.breakthrough_success",
                    nextStage.getDisplayName(),
                    formatChance(chance)
            ));
            return;
        }

        increaseBreakthroughFailureCount(player);
        setBreakthroughCooldown(player, currentStage.getBreakthroughFailureCooldownTicks());
        syncTo(player);
        player.sendOverlayMessage(Component.translatable(
                "message.eightwastelands.cultivation.breakthrough_failed",
                formatChance(chance),
                formatTicks(currentStage.getBreakthroughFailureCooldownTicks())
        ));
    }

    public static double getBreakthroughChance(CultivationStage stage, boolean hasPillBonus) {
        return getBreakthroughChance(
                stage,
                0,
                hasPillBonus ? BREAKTHROUGH_PILL_CHANCE_BONUS : 0.0D
        );
    }

    public static double getBreakthroughChance(
            CultivationStage stage,
            int breakthroughFailureCount,
            boolean hasPillBonus
    ) {
        return getBreakthroughChance(
                stage,
                breakthroughFailureCount,
                hasPillBonus ? BREAKTHROUGH_PILL_CHANCE_BONUS : 0.0D
        );
    }

    public static double getBreakthroughChance(
            CultivationStage stage,
            int breakthroughFailureCount,
            double pillChanceBonus
    ) {
        double chance = stage.getBaseBreakthroughChance();
        chance += Math.max(0, breakthroughFailureCount) * BREAKTHROUGH_FAILURE_CHANCE_STEP;
        chance += Math.max(0.0D, pillChanceBonus);

        return Math.max(0.0D, Math.min(1.0D, chance));
    }

    public static Optional<CultivationRealm> getRequiredBreakthroughPillRealm(CultivationStage stage) {
        if (stage.isMortal() || stage.isMaxStage()) {
            return Optional.empty();
        }

        return stage.next().getRealm();
    }

    public static int getBreakthroughFailureCount(ServerPlayer player) {
        return Math.max(0, player.getPersistentData().getIntOr(BREAKTHROUGH_FAILURE_COUNT, 0));
    }

    private static String formatChance(double chance) {
        return Integer.toString((int) Math.round(chance * 100.0D));
    }

    public static long getBreakthroughCooldownTicks(ServerPlayer player) {
        long cooldownEnd = player.getPersistentData().getLongOr(BREAKTHROUGH_COOLDOWN_END, 0L);
        long remaining = cooldownEnd - player.level().getOverworldClockTime();
        if (remaining <= 0L) {
            player.getPersistentData().remove(BREAKTHROUGH_COOLDOWN_END);
            return 0L;
        }

        return remaining;
    }

    private static void setBreakthroughCooldown(ServerPlayer player, long cooldownTicks) {
        long cooldownEnd = player.level().getOverworldClockTime() + Math.max(0L, cooldownTicks);
        player.getPersistentData().putLong(BREAKTHROUGH_COOLDOWN_END, cooldownEnd);
    }

    private static void increaseBreakthroughFailureCount(ServerPlayer player) {
        player.getPersistentData().putInt(
                BREAKTHROUGH_FAILURE_COUNT,
                getBreakthroughFailureCount(player) + 1
        );
    }

    private static void resetBreakthroughFailureCount(ServerPlayer player) {
        player.getPersistentData().remove(BREAKTHROUGH_FAILURE_COUNT);
    }

    private static void sendBreakthroughCooldownMessage(ServerPlayer player) {
        player.sendOverlayMessage(Component.translatable(
                "message.eightwastelands.cultivation.breakthrough_cooldown",
                formatTicks(getBreakthroughCooldownTicks(player))
        ));
    }

    public static void reduceBreakthroughCooldownWithPill(ServerPlayer player) {
        reduceBreakthroughCooldownWithPill(player, BREAKTHROUGH_PILL_COOLDOWN_REDUCTION_TICKS);
    }

    public static void reduceBreakthroughCooldownWithPill(ServerPlayer player, long cooldownReductionTicks) {
        long remaining = getBreakthroughCooldownTicks(player);
        if (remaining <= 0L) {
            syncTo(player);
            return;
        }

        long reductionTicks = Math.max(0L, cooldownReductionTicks);
        long newRemaining = Math.max(0L, remaining - reductionTicks);
        if (newRemaining <= 0L) {
            player.getPersistentData().remove(BREAKTHROUGH_COOLDOWN_END);
            syncTo(player);
            player.sendOverlayMessage(Component.translatable(
                    "message.eightwastelands.pill.breakthrough_cooldown_cleared"
            ));
            return;
        }

        long cooldownEnd = player.level().getOverworldClockTime() + newRemaining;
        player.getPersistentData().putLong(BREAKTHROUGH_COOLDOWN_END, cooldownEnd);
        syncTo(player);
        player.sendOverlayMessage(Component.translatable(
                "message.eightwastelands.pill.breakthrough_cooldown_reduced",
                formatTicks(reductionTicks),
                formatTicks(newRemaining)
        ));
    }

    public static String formatTicks(long ticks) {
        long totalSeconds = Math.max(0L, (ticks + 19L) / 20L);
        long minutes = totalSeconds / 60L;
        long seconds = totalSeconds % 60L;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static boolean tickZhenyuan(ServerPlayer player) {
        CultivationStage stage = getStage(player);
        if (stage.isMortal()) {
            return setZhenyuanScaled(player, 0);
        }

        int current = getZhenyuanScaled(player);
        int max = getMaxZhenyuanScaled(player);
        if (current >= max) {
            return false;
        }

        int regen = toScaled(getZhenyuanRegenPerSecond(player));
        if (regen <= 0) {
            return false;
        }

        return setZhenyuanScaled(player, Math.min(max, current + regen));
    }

    public static double getZhenyuanAmount(ServerPlayer player) {
        return fromScaled(getZhenyuanScaled(player));
    }

    public static double getMaxZhenyuanAmount(ServerPlayer player) {
        return getStage(player).getMaxZhenyuan()
                + XinfaManager.getMaxZhenyuanBonus(player)
                + CultivationGearManager.getMaxZhenyuanBonus(player);
    }

    public static double getZhenyuanRegenPerSecond(ServerPlayer player) {
        return getStage(player).getZhenyuanRegenPerSecond()
                + XinfaManager.getZhenyuanRegenBonus(player)
                + CultivationGearManager.getZhenyuanRegenBonus(player);
    }

    private static double consumeBreakthroughPillFromInventory(ServerPlayer player) {
        Optional<CultivationRealm> requiredRealm = getRequiredBreakthroughPillRealm(getStage(player));
        if (requiredRealm.isEmpty()) {
            return 0.0D;
        }

        int bestSlot = -1;
        AbstractBreakthroughPillItem bestPill = null;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.getItem() instanceof AbstractBreakthroughPillItem pillItem
                    && pillItem.getRealm().filter(requiredRealm.get()::equals).isPresent()
                    && (bestPill == null || pillGradePriority(pillItem) > pillGradePriority(bestPill))) {
                bestSlot = slot;
                bestPill = pillItem;
            }
        }

        if (bestPill == null) {
            return 0.0D;
        }

        ItemStack stack = player.getInventory().getItem(bestSlot);
        double chanceBonus = bestPill.getChanceBonus();
        stack.shrink(1);
        player.getInventory().setChanged();
        return chanceBonus;
    }

    private static int pillGradePriority(AbstractBreakthroughPillItem pillItem) {
        return switch (pillItem.getGrade()) {
            case IMMORTAL -> 4;
            case HIGH -> 3;
            case MIDDLE -> 2;
            case LOW -> 1;
            case NONE -> 0;
        };
    }

    private static int getQi(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        if (!data.contains(QI) && data.contains(LEGACY_SPIRITUAL_POWER)) {
            int legacyQi = Math.max(0, data.getIntOr(LEGACY_SPIRITUAL_POWER, 0));
            data.putInt(QI, legacyQi);
            data.remove(LEGACY_SPIRITUAL_POWER);
            return legacyQi;
        }

        return Math.max(0, data.getIntOr(QI, 0));
    }

    private static void setQi(ServerPlayer player, int qi) {
        CompoundTag data = player.getPersistentData();
        data.putInt(QI, Math.max(0, qi));
        data.remove(LEGACY_SPIRITUAL_POWER);
    }

    private static int getZhenyuanScaled(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        CultivationStage stage = getStage(player);
        int max = getMaxZhenyuanScaled(player);
        if (max <= 0) {
            data.remove(ZHENYUAN);
            return 0;
        }

        if (!data.contains(ZHENYUAN)) {
            data.putInt(ZHENYUAN, max);
            return max;
        }

        int current = Math.max(0, data.getIntOr(ZHENYUAN, 0));
        int clamped = Math.min(current, max);
        if (clamped != current) {
            data.putInt(ZHENYUAN, clamped);
        }

        return clamped;
    }

    private static void clampZhenyuan(ServerPlayer player, CultivationStage stage) {
        int max = getMaxZhenyuanScaled(player);
        if (max <= 0) {
            player.getPersistentData().remove(ZHENYUAN);
            return;
        }

        int current = Math.max(0, player.getPersistentData().getIntOr(ZHENYUAN, max));
        player.getPersistentData().putInt(ZHENYUAN, Math.min(current, max));
    }

    private static void setZhenyuanToMax(ServerPlayer player, CultivationStage stage) {
        setZhenyuanScaled(player, getMaxZhenyuanScaled(player));
    }

    private static boolean setZhenyuanScaled(ServerPlayer player, int zhenyuan) {
        int clamped = Math.max(0, Math.min(zhenyuan, getMaxZhenyuanScaled(player)));
        int oldValue = player.getPersistentData().getIntOr(ZHENYUAN, 0);
        if (clamped <= 0) {
            player.getPersistentData().remove(ZHENYUAN);
            return oldValue != 0;
        }

        player.getPersistentData().putInt(ZHENYUAN, clamped);
        return oldValue != clamped;
    }

    private static int getMaxZhenyuanScaled(ServerPlayer player) {
        return toScaled(getMaxZhenyuanAmount(player));
    }

    private static int toScaled(double value) {
        return Math.max(0, (int) Math.round(value * ZHENYUAN_SCALE));
    }

    private static double fromScaled(int value) {
        return value / (double) ZHENYUAN_SCALE;
    }

    private static int getTunaiUsedToday(ServerPlayer player) {
        return Math.max(0, player.getPersistentData().getIntOr(TUNAI_USED_TODAY, 0));
    }

    private static void setTunaiUsedToday(ServerPlayer player, int usedToday) {
        player.getPersistentData().putInt(TUNAI_USED_TODAY, Math.max(0, usedToday));
    }

    private static int getDailyTunaiUses(ServerPlayer player) {
        int extraUses = Math.max(0, player.getPersistentData().getIntOr(TUNAI_EXTRA_DAILY_USES, 0));
        return BASE_TUNAI_DAILY_USES + extraUses + XinfaManager.getTunaiDailyUsesBonus(player);
    }

    private static int getTunaiQiGain(ServerPlayer player) {
        return TUNAI_QI_GAIN + XinfaManager.getTunaiQiGainBonus(player);
    }

    private static int getRemainingTunaiUses(ServerPlayer player) {
        return Math.max(0, getDailyTunaiUses(player) - getTunaiUsedToday(player));
    }

    private static void refreshDailyTunai(ServerPlayer player) {
        long currentDay = player.level().getOverworldClockTime() / 24000L;
        CompoundTag data = player.getPersistentData();

        if (!data.contains(TUNAI_LAST_DAY)) {
            data.putLong(TUNAI_LAST_DAY, currentDay);
            return;
        }

        long lastDay = data.getLongOr(TUNAI_LAST_DAY, currentDay);
        if (lastDay == currentDay) {
            return;
        }

        data.putLong(TUNAI_LAST_DAY, currentDay);
        data.putInt(TUNAI_USED_TODAY, 0);
    }

    private static void setStage(ServerPlayer player, CultivationStage stage) {
        player.getPersistentData().putInt(STAGE, stage.getIndex());
    }

    private static void clearCultivationAttributeModifiers(ServerPlayer player) {
        for (CultivationAttribute attribute : CultivationAttribute.values()) {
            AttributeInstance instance = player.getAttribute(attribute.getAttribute());
            if (instance != null) {
                instance.removeModifier(attribute.getModifierId());
            }
        }
    }

    private static void applyAttributeBonus(ServerPlayer player, CultivationAttributeBonus bonus) {
        AttributeInstance attribute = player.getAttribute(bonus.attribute().getAttribute());
        if (attribute == null) {
            return;
        }

        if (bonus.amount() == 0.0D) {
            return;
        }

        attribute.addOrUpdateTransientModifier(new AttributeModifier(
                bonus.attribute().getModifierId(),
                bonus.amount(),
                AttributeModifier.Operation.ADD_VALUE
        ));
    }

    private static void copyInt(CompoundTag oldData, CompoundTag newData, String key) {
        if (oldData.contains(key)) {
            newData.putInt(key, oldData.getIntOr(key, 0));
        }
    }

    private static void copyQiData(CompoundTag oldData, CompoundTag newData) {
        if (oldData.contains(QI)) {
            newData.putInt(QI, oldData.getIntOr(QI, 0));
            return;
        }

        if (oldData.contains(LEGACY_SPIRITUAL_POWER)) {
            newData.putInt(QI, oldData.getIntOr(LEGACY_SPIRITUAL_POWER, 0));
        }
    }

    private static void copyLong(CompoundTag oldData, CompoundTag newData, String key) {
        if (oldData.contains(key)) {
            newData.putLong(key, oldData.getLongOr(key, 0L));
        }
    }

    private static void restoreSavedHealth(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        if (!data.contains(SAVED_HEALTH)) {
            return;
        }

        float savedHealth = data.getFloatOr(SAVED_HEALTH, player.getHealth());
        data.remove(SAVED_HEALTH);
        if (savedHealth <= 0.0F || !player.isAlive()) {
            return;
        }

        player.setHealth(Math.min(savedHealth, player.getMaxHealth()));
    }
}
