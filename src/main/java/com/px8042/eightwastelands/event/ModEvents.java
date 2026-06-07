package com.px8042.eightwastelands.event;
import com.px8042.eightwastelands.item.custom.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import com.px8042.eightwastelands.item.ModItems;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import top.theillusivec4.curios.api.CuriosApi;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import com.px8042.eightwastelands.effect.ModMobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import com.px8042.eightwastelands.damage.ModDamageTypes;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LightningBolt;
import com.px8042.eightwastelands.item.artifact.ArtifactDurabilityHelper;
import com.px8042.eightwastelands.item.artifact.IGhostArtifactItem;
import com.px8042.eightwastelands.item.artifact.ArtifactDurabilityHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import com.px8042.eightwastelands.item.artifact.ArtifactDurabilityHelper;
import com.px8042.eightwastelands.item.artifact.IHeavenlyArtifactItem;
import com.px8042.eightwastelands.block.ModBlocks;
import com.px8042.eightwastelands.item.artifact.IHeavenlyArtifactItem;
import com.px8042.eightwastelands.item.artifact.ArtifactDurabilityHelper;
import com.px8042.eightwastelands.item.custom.FengxingBootsItem;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import com.px8042.eightwastelands.item.custom.BihuoPearlItem;
import net.minecraft.tags.DamageTypeTags;

import java.util.Comparator;







public class ModEvents {
    private static final double LUCK_DEPRIVATION_RANGE = 32.0D;//控制夺运范围
    private static final String HAS_RECEIVED_NINE_HEAVENS_PUNISHMENT =
            "eightwastelands_has_received_nine_heavens_punishment";  //添加首次获得标记
    private static final String SHOULD_RESTORE_NINE_HEAVENS_PUNISHMENT =
            "eightwastelands_should_restore_nine_heavens_punishment";  //用于制作死亡时不掉落
    private static final float WITHERED_BLOOD_REVERSE_MAX_HEALTH = 100.0F;//用于反转绝灵,到达血量即可反转
    private static final int LOU_ZHEN_REPAIR_AMOUNT = 100;
    private static final int LOU_ZHEN_REPAIR_LEVEL_COST = 3;  //楼砧常量



    //上面是常量
    //让挖掘疲劳进绝灵里
    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {

        Player player = event.getEntity();

        if (!player.hasEffect(ModMobEffects.SPIRIT_EXHAUSTION)) {
            return;
        }

        if (NineHeavensPunishmentItem.isSpiritExhaustionReversed(player)) {
            return;
        }

        event.setNewSpeed(event.getOriginalSpeed() * 0.09F);
    }

    //右键楼砧事件
    @SubscribeEvent
    public void onLouZhenRepair(PlayerInteractEvent.RightClickBlock event) {

        Player player = event.getEntity();

        if (player.level().isClientSide()) {
            return;
        }

        if (!event.getLevel().getBlockState(event.getPos()).is(ModBlocks.LOU_ZHEN.get())) {
            return;
        }

        if (!player.isShiftKeyDown()) {
            return;
        }

        ItemStack stack = player.getItemInHand(event.getHand());

        if (!(stack.getItem() instanceof IHeavenlyArtifactItem)) {
            return;
        }

        event.setCanceled(true);

        if (!ArtifactDurabilityHelper.isDamaged(stack)) {
            player.sendOverlayMessage(
                    Component.literal("楼砧：这件仙器无需修复。"));
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }

        if (!player.isCreative()) {

            if (!hasEmerald(player)) {
                player.sendOverlayMessage(
                        Component.literal("楼砧：修复仙器需要 1 个绿宝石。"));
                event.setCancellationResult(InteractionResult.FAIL);
                return;
            }

            if (player.experienceLevel < LOU_ZHEN_REPAIR_LEVEL_COST) {
                player.sendOverlayMessage(
                        Component.literal("楼砧：修复仙器需要 " + LOU_ZHEN_REPAIR_LEVEL_COST + " 级经验。"));
                event.setCancellationResult(InteractionResult.FAIL);
                return;
            }

            consumeEmerald(player);
            player.giveExperienceLevels(-LOU_ZHEN_REPAIR_LEVEL_COST);
        }

        ArtifactDurabilityHelper.repairArtifact(
                stack,
                LOU_ZHEN_REPAIR_AMOUNT
        );

        player.sendOverlayMessage(
                Component.literal("楼砧：仙器灵纹已修复。"));

        event.setCancellationResult(InteractionResult.SUCCESS);
    }
    //楼砧绿宝石检测
    private boolean hasEmerald(Player player) {

        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {

            ItemStack stack = player.getInventory().getItem(slot);

            if (stack.is(Items.EMERALD)) {
                return true;
            }
        }

        return false;
    }
    private void consumeEmerald(Player player) {

        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {

            ItemStack stack = player.getInventory().getItem(slot);

            if (!stack.is(Items.EMERALD)) {
                continue;
            }

            stack.shrink(1);
            return;
        }
    }

    //下面是死亡时九重天罚不掉落事件
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        resetHeavenlyThunderSealCountOnDeath(player);
        clearForgetfulnessMemory(player);

        if (hasNineHeavensPunishment(player)) {
            player.getPersistentData().putBoolean(
                    SHOULD_RESTORE_NINE_HEAVENS_PUNISHMENT,
                    true
            );
        }

    }
    //遗忘鬼
    private ItemStack getForgetfulnessEquippedStack(Player player) {

        final ItemStack[] found = {ItemStack.EMPTY};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("guiqi").ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {

                    ItemStack stack = stacks.getStackInSlot(slot);

                    if (stack.is(ModItems.FORGETFULNESS.get())) {
                        found[0] = stack;
                        return;
                    }
                }
            });
        });

        return found[0];
    }
    //遗忘鬼记录伤害
    private void recordForgetfulnessDamage(Pre event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        if (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD)) {
            return;
        }

        float damage = event.getNewDamage();

        if (damage <= 0.0F) {
            return;
        }

        if (player.getHealth() - damage <= 0.0F) {
            return;
        }

        ItemStack forgetfulness = getForgetfulnessEquippedStack(player);

        if (forgetfulness.isEmpty()) {
            return;
        }

        if (ArtifactDurabilityHelper.isBroken(forgetfulness)) {
            return;
        }

        CompoundTag data = player.getPersistentData();

        data.putFloat(
                ForgetfulnessItem.FORGOTTEN_HEALTH_BEFORE_DAMAGE,
                player.getHealth()
        );

        data.putLong(
                ForgetfulnessItem.FORGOTTEN_HEALTH_RESTORE_TIME,
                player.level().getGameTime() + ForgetfulnessItem.DAMAGE_RESTORE_DELAY
        );
    }
    //遗忘鬼处理遗忘
    private void tickForgetfulness(Player player) {

        ItemStack forgetfulness = getForgetfulnessEquippedStack(player);

        if (forgetfulness.isEmpty()) {
            clearForgetfulnessMemory(player);
            return;
        }

        if (ArtifactDurabilityHelper.isBroken(forgetfulness)) {
            clearForgetfulnessMemory(player);
            return;
        }

        restoreForgottenDamage(player, forgetfulness);
        tickForgetfulnessBuffMemory(player, forgetfulness);
    }
    private void restoreForgottenDamage(Player player, ItemStack forgetfulness) {

        CompoundTag data = player.getPersistentData();

        if (!data.contains(ForgetfulnessItem.FORGOTTEN_HEALTH_RESTORE_TIME)) {
            return;
        }

        long restoreTime = data.getLongOr(ForgetfulnessItem.FORGOTTEN_HEALTH_RESTORE_TIME, 0L);

        if (player.level().getGameTime() < restoreTime) {
            return;
        }

        float rememberedHealth = data.getFloatOr(ForgetfulnessItem.FORGOTTEN_HEALTH_BEFORE_DAMAGE, 0.0F);

        data.remove(ForgetfulnessItem.FORGOTTEN_HEALTH_RESTORE_TIME);
        data.remove(ForgetfulnessItem.FORGOTTEN_HEALTH_BEFORE_DAMAGE);

        if (!player.isAlive()) {
            return;
        }

        float targetHealth = Math.min(
                rememberedHealth,
                player.getMaxHealth()
        );

        if (targetHealth <= player.getHealth()) {
            return;
        }

        player.setHealth(targetHealth);

        ArtifactDurabilityHelper.damageArtifact(
                forgetfulness,
                ForgetfulnessItem.DAMAGE_MEMORY_DURABILITY_COST
        );

        player.sendOverlayMessage(
                Component.literal("遗忘：你忘记了刚才的伤痛。"));
    }
    private void tickForgetfulnessBuffMemory(Player player, ItemStack forgetfulness) {

        CompoundTag data = player.getPersistentData();

        CompoundTag memory = data.getCompoundOrEmpty(ForgetfulnessItem.BUFF_MEMORY);

        Set<String> activeKeys = new HashSet<>();

        for (MobEffectInstance effectInstance : new ArrayList<>(player.getActiveEffects())) {

            Holder<MobEffect> effect = effectInstance.getEffect();

            if (!isForgetfulnessBuffSupported(effect, effectInstance)) {
                continue;
            }

            Identifier effectId = BuiltInRegistries.MOB_EFFECT.getKey(effect.value());

            if (effectId == null) {
                continue;
            }

            String key = effectId.toString();

            activeKeys.add(key);

            int currentDuration = effectInstance.getDuration();
            int currentAmplifier = effectInstance.getAmplifier();

            CompoundTag saved = memory.getCompoundOrEmpty(key);

            if (!memory.contains(key)
                    || saved.getIntOr("amplifier", 0) != currentAmplifier
                    || currentDuration > saved.getIntOr("duration", 0)) {

                saved.putInt("duration", currentDuration);
                saved.putInt("amplifier", currentAmplifier);
                saved.putBoolean("ambient", effectInstance.isAmbient());
                saved.putBoolean("visible", effectInstance.isVisible());
                saved.putBoolean("show_icon", effectInstance.showIcon());

                memory.put(key, saved);
            }

            int rememberedDuration = memory.getCompoundOrEmpty(key).getIntOr("duration", 0);

            if (currentDuration > ForgetfulnessItem.BUFF_RENEW_THRESHOLD) {
                continue;
            }

            player.addEffect(new MobEffectInstance(
                    effect,
                    rememberedDuration,
                    currentAmplifier,
                    effectInstance.isAmbient(),
                    effectInstance.isVisible(),
                    effectInstance.showIcon()
            ));

            ArtifactDurabilityHelper.damageArtifact(
                    forgetfulness,
                    ForgetfulnessItem.BUFF_RENEW_DURABILITY_COST
            );
        }

        for (String key : new ArrayList<>(memory.keySet())) {
            if (!activeKeys.contains(key)) {
                memory.remove(key);
            }
        }

        if (memory.keySet().isEmpty()) {
            data.remove(ForgetfulnessItem.BUFF_MEMORY);
        } else {
            data.put(ForgetfulnessItem.BUFF_MEMORY, memory);
        }
    }
    private boolean isForgetfulnessBuffSupported(
            Holder<MobEffect> effect,
            MobEffectInstance effectInstance
    ) {

        if (effect.value().getCategory() != MobEffectCategory.BENEFICIAL) {
            return false;
        }

        if (effect.value().isInstantenous()) {
            return false;
        }

        if (effect.value() == MobEffects.ABSORPTION.value()) {
            return false;
        }

        return effectInstance.getDuration() > 0;
    }
    private void clearForgetfulnessMemory(Player player) {

        CompoundTag data = player.getPersistentData();

        data.remove(ForgetfulnessItem.FORGOTTEN_HEALTH_RESTORE_TIME);
        data.remove(ForgetfulnessItem.FORGOTTEN_HEALTH_BEFORE_DAMAGE);
        data.remove(ForgetfulnessItem.BUFF_MEMORY);
    }
    //重新计算天劫的雷次数
    private void resetHeavenlyThunderSealCountOnDeath(Player player) {

        if (NineHeavensPunishmentItem.isHeavenlyTribulationReversed(player)) {
            return;
        }

        CompoundTag data = player.getPersistentData();

        data.remove(HeavenlyThunderSealItem.HEAVENLY_THUNDER_COUNT);
    }
    //玩家重生事件
    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {

        Player player = event.getEntity();

        if (player.level().isClientSide()) {
            return;
        }

        CompoundTag data = player.getPersistentData();

        if (!data.getBooleanOr(SHOULD_RESTORE_NINE_HEAVENS_PUNISHMENT, false)) {
            return;
        }

        data.remove(SHOULD_RESTORE_NINE_HEAVENS_PUNISHMENT);

        if (hasNineHeavensPunishment(player)) {
            return;
        }

        ItemStack stack = new ItemStack(ModItems.NINE_HEAVENS_PUNISHMENT.get());

        if (!tryEquipToCalamitySlot(player, stack)) {
            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
        }
    }
    //检测是否已经有天罚
    private boolean hasNineHeavensPunishment(Player player) {

        if (player.getInventory().contains(new ItemStack(ModItems.NINE_HEAVENS_PUNISHMENT.get()))) {
            return true;
        }

        final boolean[] found = {false};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("calamity").ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {
                    if (stacks.getStackInSlot(slot).is(ModItems.NINE_HEAVENS_PUNISHMENT.get())) {
                        found[0] = true;
                        return;
                    }
                }
            });
        });

        return found[0];
    }
    //修复死亡后消失
    // 修复死亡后数据丢失
    // 修复死亡后数据丢失
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {

        if (!event.isWasDeath()) {
            return;
        }

        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        CompoundTag oldData = oldPlayer.getPersistentData();
        CompoundTag newData = newPlayer.getPersistentData();

        if (oldData.getBooleanOr(SHOULD_RESTORE_NINE_HEAVENS_PUNISHMENT, false)) {
            newData.putBoolean(SHOULD_RESTORE_NINE_HEAVENS_PUNISHMENT, true);
        }

        if (oldData.getBooleanOr(HAS_RECEIVED_NINE_HEAVENS_PUNISHMENT, false)) {
            newData.putBoolean(HAS_RECEIVED_NINE_HEAVENS_PUNISHMENT, true);
        }

        copyBooleanFlag(oldData, newData, NineHeavensPunishmentItem.LUCK_DEPRIVATION_REVERSED);
        copyBooleanFlag(oldData, newData, NineHeavensPunishmentItem.WIND_EVIL_REVERSED);
        copyBooleanFlag(oldData, newData, NineHeavensPunishmentItem.LIFE_EXHAUSTION_REVERSED);
        copyBooleanFlag(oldData, newData, NineHeavensPunishmentItem.HEART_DEMON_REVERSED);
        copyBooleanFlag(oldData, newData, NineHeavensPunishmentItem.SPIRIT_EXHAUSTION_REVERSED);
        copyBooleanFlag(oldData, newData, NineHeavensPunishmentItem.WITHERED_BLOOD_REVERSED);
        copyBooleanFlag(oldData, newData, NineHeavensPunishmentItem.KARMA_REVERSED);
        copyBooleanFlag(oldData, newData, NineHeavensPunishmentItem.EARTH_DISASTER_REVERSED);
        copyBooleanFlag(oldData, newData, NineHeavensPunishmentItem.HEAVENLY_TRIBULATION_REVERSED);

        if (oldData.contains(BloodSkullItem.VILLAGER_KILL_COUNT)) {
            newData.putInt(
                    BloodSkullItem.VILLAGER_KILL_COUNT,
                    oldData.getIntOr(BloodSkullItem.VILLAGER_KILL_COUNT, 0)
            );
        }



        if (oldData.contains(HeavenlyThunderSealItem.NEXT_THUNDER_STRIKE_TIME)) {
            newData.putLong(
                    HeavenlyThunderSealItem.NEXT_THUNDER_STRIKE_TIME,
                    oldData.getLongOr(HeavenlyThunderSealItem.NEXT_THUNDER_STRIKE_TIME, 0L)
            );
        }
    }
    private void ensureNineHeavensPunishment(Player player) {

        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        CompoundTag data = player.getPersistentData();

        if (!data.getBooleanOr(HAS_RECEIVED_NINE_HEAVENS_PUNISHMENT, false)) {
            data.putBoolean(HAS_RECEIVED_NINE_HEAVENS_PUNISHMENT, true);
        }

        if (!hasNineHeavensPunishment(player)) {
            ItemStack stack = new ItemStack(ModItems.NINE_HEAVENS_PUNISHMENT.get());

            if (!tryEquipToCalamitySlot(player, stack)) {
                player.getInventory().add(stack);
            }
        }

        removeDuplicateNineHeavensPunishment(player);
    }
    private void removeDuplicateNineHeavensPunishment(Player player) {

        boolean hasCalamityOne = hasNineHeavensPunishmentInCalamity(player);

        if (!hasCalamityOne) {
            return;
        }

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {

            ItemStack stack = player.getInventory().getItem(i);

            if (!stack.is(ModItems.NINE_HEAVENS_PUNISHMENT.get())) {
                continue;
            }

            player.getInventory().setItem(i, ItemStack.EMPTY);
        }
    }
    private boolean hasNineHeavensPunishmentInCalamity(Player player) {

        final boolean[] found = {false};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("calamity").ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {

                    if (stacks.getStackInSlot(slot).is(ModItems.NINE_HEAVENS_PUNISHMENT.get())) {
                        found[0] = true;
                        return;
                    }
                }
            });
        });

        return found[0];
    }
    //反转因果
    @SubscribeEvent
    public void onVillagerKilledByBloodSkull(LivingDeathEvent event) {

        if (event.getEntity().level().isClientSide()) {
            return;
        }

        if (event.getEntity().getType() != EntityType.VILLAGER) {
            return;
        }

        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }

        if (!hasBloodSkullEquipped(player)) {
            return;
        }

        if (NineHeavensPunishmentItem.isKarmaReversed(player)) {
            return;
        }

        CompoundTag data = player.getPersistentData();

        int currentCount = data.getIntOr(BloodSkullItem.VILLAGER_KILL_COUNT, 0);
        int newCount = currentCount + 1;

        data.putInt(BloodSkullItem.VILLAGER_KILL_COUNT, newCount);

        player.sendOverlayMessage(
                Component.literal("血颅骨：" + newCount + " / " + BloodSkullItem.REQUIRED_VILLAGER_KILLS));

        if (newCount < BloodSkullItem.REQUIRED_VILLAGER_KILLS) {
            return;
        }

        data.putBoolean(
                NineHeavensPunishmentItem.KARMA_REVERSED,
                true
        );

        player.removeEffect(ModMobEffects.KARMA);

        player.sendOverlayMessage(
                Component.literal("因果已反转：血债已满，因果归寂。"));
    }
    //检测血颅骨佩戴
    private boolean hasBloodSkullEquipped(Player player) {

        final boolean[] found = {false};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("calamity").ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {

                    if (stacks.getStackInSlot(slot).is(ModItems.BLOOD_SKULL.get())) {
                        found[0] = true;
                        return;
                    }
                }
            });
        });

        return found[0];
    }




    //下面是开局给予功能
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {

        Player player = event.getEntity();

        if (player.level().isClientSide()) {
            return;
        }

        CompoundTag data = player.getPersistentData();

        if (data.getBooleanOr(HAS_RECEIVED_NINE_HEAVENS_PUNISHMENT, false)) {
            return;
        }

        data.putBoolean(HAS_RECEIVED_NINE_HEAVENS_PUNISHMENT, true);

        giveAndEquipNineHeavensPunishment(player);
    }
    private void giveAndEquipNineHeavensPunishment(Player player) {

        ItemStack stack = new ItemStack(ModItems.NINE_HEAVENS_PUNISHMENT.get());

        if (tryEquipToCalamitySlot(player, stack)) {
            return;
        }

        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }
    private boolean tryEquipToCalamitySlot(Player player, ItemStack stack) {

        final boolean[] equipped = {false};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("calamity").ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {

                    if (!stacks.getStackInSlot(slot).isEmpty()) {
                        continue;
                    }

                    stacks.setStackInSlot(slot, stack.copy());
                    stack.setCount(0);
                    equipped[0] = true;
                    return;
                }
            });
        });

        return equipped[0];
    }
    //上面写的是开局给予功能

    //这个是防止玩家丢弃功能
    @SubscribeEvent
    public void onItemToss(ItemTossEvent event) {

        Player player = event.getPlayer();

        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        ItemStack stack = event.getEntity().getItem();

        if (!stack.is(ModItems.NINE_HEAVENS_PUNISHMENT.get())) {
            return;
        }

        event.setCanceled(true);

        if (!player.getInventory().add(stack.copy())) {
            player.drop(stack.copy(), false);
        }

        event.getEntity().discard();
    }


    @SubscribeEvent
    public void onLivingJump(LivingEvent.LivingJumpEvent event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        if (player.getAbilities().flying) {
            return;
        }

        if (!player.hasEffect(ModMobEffects.EARTH_DISASTER)) {
            return;
        }

        Vec3 movement = player.getDeltaMovement();

        player.setDeltaMovement(
                movement.x,
                -0.25D,
                movement.z
        );

        player.fallDistance = 0.0F;
        player.hurtMarked = true;
    }

    private void removeLifeExhaustionIfInactive(Player player) {

        if (!player.hasEffect(ModMobEffects.LIFE_EXHAUSTION)) {
            removeLifeExhaustionModifier(player);
        }
    }
    private void removeLifeExhaustionModifier(Player player) {

        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);

        if (maxHealth == null) {
            return;
        }

        if (maxHealth.getModifier(NineHeavensPunishmentItem.LIFE_EXHAUSTION_MAX_HEALTH_MODIFIER_ID) != null) {
            maxHealth.removeModifier(NineHeavensPunishmentItem.LIFE_EXHAUSTION_MAX_HEALTH_MODIFIER_ID);
        }
    }
    @SubscribeEvent
    public void onLivingDamage(Pre event) {

        applyBihuoPearl(event);

        applyHeavenlyThunderSeal(event);
        applySummerFanShield(event);
        applyJiehuiRingLifeSave(event);

        applyHeartDemonMaskBacklash(event);
        applySpringShearsFateCut(event);
        applyHeartDemon(event);
        applySpiritExhaustion(event);

        recordForgetfulnessDamage(event);
        clearFengxingBootsOnDamage(event);
    }
    //避火珠
    private void applyBihuoPearl(Pre event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        ItemStack pearl = getBihuoPearlEquippedStack(player);

        if (pearl.isEmpty()) {
            return;
        }

        if (ArtifactDurabilityHelper.isBroken(pearl)) {
            return;
        }

        if (event.getSource().is(DamageTypeTags.IS_FIRE)) {

            if (event.getNewDamage() <= 0.0F) {
                return;
            }

            event.setNewDamage(0.0F);
            player.clearFire();

            ArtifactDurabilityHelper.damageArtifact(
                    pearl,
                    BihuoPearlItem.FIRE_BLOCK_DURABILITY_COST
            );

            if (ArtifactDurabilityHelper.isBroken(pearl)) {
                player.sendOverlayMessage(
                        Component.literal("避火珠：珠光黯淡，避火之力已尽。"));
            }

            return;
        }

        if (event.getSource().is(DamageTypes.FREEZE)) {
            event.setNewDamage(
                    event.getNewDamage() * BihuoPearlItem.FREEZE_DAMAGE_MULTIPLIER
            );
        }
    }
    private ItemStack getBihuoPearlEquippedStack(Player player) {

        final ItemStack[] found = {ItemStack.EMPTY};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler(IHeavenlyArtifactItem.SLOT_ID).ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {

                    ItemStack stack = stacks.getStackInSlot(slot);

                    if (stack.is(ModItems.BIHUO_PEARL.get())) {
                        found[0] = stack;
                        return;
                    }
                }
            });
        });

        return found[0];
    }

    //风行鞋
    private void tickFengxingBoots(Player player) {

        if (player.level().isClientSide()) {
            return;
        }

        ItemStack boots = getFengxingBootsEquippedStack(player);

        if (boots.isEmpty()) {
            clearFengxingBoots(player);
            return;
        }

        if (ArtifactDurabilityHelper.isBroken(boots)) {
            clearFengxingBoots(player);
            return;
        }

        if (!player.isSprinting()
                || player.isShiftKeyDown()
                || !player.onGround()
                || player.isCreative()
                || player.isSpectator()) {

            clearFengxingBoots(player);
            return;
        }

        CompoundTag data = player.getPersistentData();

        int sprintTicks = data.getIntOr(FengxingBootsItem.SPRINT_TICKS, 0) + 1;

        data.putInt(FengxingBootsItem.SPRINT_TICKS, sprintTicks);

        int stage = getFengxingBootsStage(sprintTicks);

        applyFengxingBootsSpeed(player, stage);

        if (sprintTicks % FengxingBootsItem.DURABILITY_COST_INTERVAL == 0) {

            ArtifactDurabilityHelper.damageArtifact(
                    boots,
                    FengxingBootsItem.DURABILITY_COST
            );

            if (ArtifactDurabilityHelper.isBroken(boots)) {
                clearFengxingBoots(player);

                player.sendOverlayMessage(
                        Component.literal("风行履：灵纹耗尽，风息已止。"));
            }
        }
    }
    private ItemStack getFengxingBootsEquippedStack(Player player) {

        final ItemStack[] found = {ItemStack.EMPTY};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler(IHeavenlyArtifactItem.SLOT_ID).ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {

                    ItemStack stack = stacks.getStackInSlot(slot);

                    if (stack.is(ModItems.FENGXING_BOOTS.get())) {
                        found[0] = stack;
                        return;
                    }
                }
            });
        });

        return found[0];
    }
    private int getFengxingBootsStage(int sprintTicks) {

        if (sprintTicks >= FengxingBootsItem.STAGE_3_TICKS) {
            return 3;
        }

        if (sprintTicks >= FengxingBootsItem.STAGE_2_TICKS) {
            return 2;
        }

        if (sprintTicks >= FengxingBootsItem.STAGE_1_TICKS) {
            return 1;
        }

        return 0;
    }
    private void applyFengxingBootsSpeed(Player player, int stage) {

        removeFengxingBootsSpeed(player);

        if (stage <= 0) {
            return;
        }

        AttributeInstance movementSpeed = player.getAttribute(
                Attributes.MOVEMENT_SPEED
        );

        if (movementSpeed == null) {
            return;
        }

        double bonus = switch (stage) {
            case 1 -> FengxingBootsItem.STAGE_1_SPEED_BONUS;
            case 2 -> FengxingBootsItem.STAGE_2_SPEED_BONUS;
            case 3 -> FengxingBootsItem.STAGE_3_SPEED_BONUS;
            default -> 0.0D;
        };

        if (bonus <= 0.0D) {
            return;
        }

        movementSpeed.addTransientModifier(new AttributeModifier(
                FengxingBootsItem.SPEED_MODIFIER_ID,
                bonus,
                AttributeModifier.Operation.ADD_VALUE
        ));
    }
    private void removeFengxingBootsSpeed(Player player) {

        AttributeInstance movementSpeed = player.getAttribute(
                Attributes.MOVEMENT_SPEED
        );

        if (movementSpeed == null) {
            return;
        }

        if (movementSpeed.getModifier(FengxingBootsItem.SPEED_MODIFIER_ID) != null) {
            movementSpeed.removeModifier(FengxingBootsItem.SPEED_MODIFIER_ID);
        }
    }
    private void clearFengxingBoots(Player player) {

        player.getPersistentData().remove(FengxingBootsItem.SPRINT_TICKS);
        removeFengxingBootsSpeed(player);
    }
    private void clearFengxingBootsOnDamage(Pre event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        if (event.getNewDamage() <= 0.0F) {
            return;
        }

        clearFengxingBoots(player);
    }
    //劫灰戒
    private void applyJiehuiRingLifeSave(Pre event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        if (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD)) {
            return;
        }

        float damage = event.getNewDamage();

        if (damage <= 0.0F) {
            return;
        }

        if (player.getHealth() - damage > 0.0F) {
            return;
        }

        ItemStack ring = getJiehuiRingEquippedStack(player);

        if (ring.isEmpty()) {
            return;
        }

        if (ArtifactDurabilityHelper.isBroken(ring)) {
            return;
        }

        if (!JiehuiRingItem.isReady(player)) {
            return;
        }

        event.setNewDamage(0.0F);

        player.setHealth(1.0F);

        player.addEffect(new MobEffectInstance(
                MobEffects.RESISTANCE,
                JiehuiRingItem.RESISTANCE_DURATION,
                2,
                false,
                false,
                true
        ));

        player.addEffect(new MobEffectInstance(
                MobEffects.FIRE_RESISTANCE,
                JiehuiRingItem.FIRE_RESISTANCE_DURATION,
                0,
                false,
                false,
                true
        ));

        player.addEffect(new MobEffectInstance(
                MobEffects.SLOWNESS,
                JiehuiRingItem.SLOWNESS_DURATION,
                1,
                false,
                false,
                true
        ));

        ArtifactDurabilityHelper.damageArtifact(
                ring,
                JiehuiRingItem.DURABILITY_COST
        );

        JiehuiRingItem.startCooldown(player);

        player.sendOverlayMessage(
                Component.literal("劫灰戒：灰烬护命，留你一息。"));
    }
    private ItemStack getJiehuiRingEquippedStack(Player player) {

        final ItemStack[] found = {ItemStack.EMPTY};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler(IHeavenlyArtifactItem.SLOT_ID).ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {

                    ItemStack stack = stacks.getStackInSlot(slot);

                    if (stack.is(ModItems.JIEHUI_RING.get())) {
                        found[0] = stack;
                        return;
                    }
                }
            });
        });

        return found[0];
    }



    //夏扇实现
    private void applySummerFanShield(Pre event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!SummerFanItem.isDamageShieldReady(player)) {
            return;
        }

        event.setNewDamage(0.0F);

        SummerFanItem.consumeDamageShield(player);

        player.sendOverlayMessage(
                Component.literal("夏扇护体：本次伤害已被化解。"));
    }
    //春剪实现
    private void applySpringShearsFateCut(Pre event) {

        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        LivingEntity target = event.getEntity();

        if (!target.isAlive()) {
            return;
        }

        if (target instanceof Player) {
            return;
        }

        if (target.getHealth() <= 1.0F) {
            return;
        }

        if (event.getNewDamage() <= 0.0F) {
            return;
        }

        if (!hasSpringShearsEquipped(player)) {
            return;
        }

        if (!SpringShearsItem.isFateCutReady(player)) {
            return;
        }

        float immediateDamage = target.getMaxHealth()
                * SpringShearsItem.FATE_CUT_IMMEDIATE_MAX_HEALTH_RATIO;

        SpringShearsItem.cutHealthWithoutKilling(target, immediateDamage);

        target.addEffect(new MobEffectInstance(
                ModMobEffects.SPRING_SHEARS_FATE_CUT,
                SpringShearsItem.FATE_CUT_DURATION,
                0,
                false,
                false,
                true
        ));

        target.addEffect(new MobEffectInstance(
                MobEffects.WEAKNESS,
                SpringShearsItem.FATE_CUT_DURATION,
                SpringShearsItem.FATE_CUT_WEAKNESS_AMPLIFIER,
                false,
                false,
                true
        ));

        target.addEffect(new MobEffectInstance(
                MobEffects.WITHER,
                SpringShearsItem.FATE_CUT_DURATION,
                SpringShearsItem.FATE_CUT_WITHER_AMPLIFIER,
                false,
                false,
                true
        ));

        SpringShearsItem.startFateCutCooldown(player);

        player.sendOverlayMessage(
                Component.literal("春剪裁命：已剪去其半数天命。"));
    }





    //检测春剪是否佩戴
    private boolean hasSpringShearsEquipped(Player player) {

        final boolean[] found = {false};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("calamity").ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {

                    if (stacks.getStackInSlot(slot).is(ModItems.SPRING_SHEARS.get())) {
                        found[0] = true;
                        return;
                    }
                }
            });
        });

        return found[0];
    }
    //心魔板块
    @SubscribeEvent
    public void onWitherKilledByHeartDemonMask(LivingDeathEvent event) {

        if (event.getEntity().level().isClientSide()) {
            return;
        }

        if (event.getEntity().getType() != EntityType.WITHER) {
            return;
        }

        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }

        if (!hasHeartDemonMaskEquipped(player)) {
            return;
        }

        if (NineHeavensPunishmentItem.isHeartDemonReversed(player)) {
            return;
        }

        player.getPersistentData().putBoolean(
                NineHeavensPunishmentItem.HEART_DEMON_REVERSED,
                true
        );

        player.removeEffect(ModMobEffects.HEART_DEMON);

        player.sendOverlayMessage(
                Component.literal("心魔已反转：魔由心生，亦由心灭。"));
    }
    private boolean hasHeartDemonMaskEquipped(Player player) {

        final boolean[] found = {false};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("calamity").ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {

                    if (stacks.getStackInSlot(slot).is(ModItems.HEART_DEMON_MASK.get())) {
                        found[0] = true;
                        return;
                    }
                }
            });
        });

        return found[0];
    }
    // 心魔面具：受到攻击时，让攻击者反胃
    private void applyHeartDemonMaskBacklash(Pre event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        if (!hasHeartDemonMaskEquipped(player)) {
            return;
        }

        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
            return;
        }

        if (attacker == player) {
            return;
        }

        attacker.addEffect(new MobEffectInstance(
                MobEffects.NAUSEA,
                5 * 20,
                0,
                false,
                false,
                true
        ));
    }



    //=============================================================================
    //tick
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {

        Player player = event.getEntity();

        if (player.level().isClientSide()) {
            return;
        }

        removeLifeExhaustionIfInactive(player);
        removeWindEvilIfInactive(player);

        tickGhostArtifactAutoRepair(player);
        tickForgetfulness(player);
        tickFengxingBoots(player);

        tickShengZhuangKnockbackResistance(player);
        tickHeavenlyThunderSealStrike(player);

        tryReverseWitheredBlood(player);
        tryReverseEarthDisaster(player);

        ensureNineHeavensPunishment(player);
    }
    //鬼器修复
    private void tickGhostArtifactAutoRepair(Player player) {

        if (player.level().getGameTime() % IGhostArtifactItem.GHOST_REPAIR_INTERVAL != 0) {
            return;
        }

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler(IGhostArtifactItem.SLOT_ID).ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {

                    ItemStack stack = stacks.getStackInSlot(slot);

                    if (!(stack.getItem() instanceof IGhostArtifactItem)) {
                        continue;
                    }

                    repairGhostArtifact(player, stack);
                }
            });
        });
    }
    private void repairGhostArtifact(Player player, ItemStack stack) {

        if (!ArtifactDurabilityHelper.isDamaged(stack)) {
            return;
        }

        if (player.totalExperience >= IGhostArtifactItem.EXPERIENCE_COST_PER_REPAIR) {

            player.giveExperiencePoints(-IGhostArtifactItem.EXPERIENCE_COST_PER_REPAIR);

            ArtifactDurabilityHelper.repairArtifact(
                    stack,
                    IGhostArtifactItem.GHOST_REPAIR_AMOUNT
            );

            return;
        }

        float health = player.getHealth();

        if (health <= IGhostArtifactItem.MIN_HEALTH_AFTER_REPAIR) {
            return;
        }

        float healthCost = Math.min(
                IGhostArtifactItem.HEALTH_COST_PER_REPAIR,
                health - IGhostArtifactItem.MIN_HEALTH_AFTER_REPAIR
        );

        if (healthCost <= 0.0F) {
            return;
        }

        player.setHealth(health - healthCost);

        ArtifactDurabilityHelper.repairArtifact(
                stack,
                IGhostArtifactItem.GHOST_REPAIR_AMOUNT
        );
    }
    //天雷效果
    private void applyHeavenlyThunderSeal(Pre event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        if (!hasHeavenlyThunderSealEquipped(player)) {
            return;
        }

        if (!event.getSource().is(DamageTypeTags.IS_LIGHTNING)) {
            return;
        }

        if (NineHeavensPunishmentItem.isHeavenlyTribulationReversed(player)) {
            event.setNewDamage(0.0F);
            return;
        }

        if (!event.getSource().is(ModDamageTypes.HEAVENLY_TRIBULATION_LIGHTNING)) {
            return;
        }

        if (!player.hasEffect(ModMobEffects.HEAVENLY_TRIBULATION)) {
            return;
        }

        if (player.getHealth() - event.getNewDamage() <= 0.0F) {
            return;
        }

        CompoundTag data = player.getPersistentData();

        int oldCount = data.getIntOr(HeavenlyThunderSealItem.HEAVENLY_THUNDER_COUNT, 0);
        int newCount = oldCount + 1;

        data.putInt(HeavenlyThunderSealItem.HEAVENLY_THUNDER_COUNT, newCount);

        player.sendOverlayMessage(
                Component.literal("天雷印：" + newCount + " / "
                        + HeavenlyThunderSealItem.REQUIRED_THUNDER_COUNT));

        if (newCount < HeavenlyThunderSealItem.REQUIRED_THUNDER_COUNT) {
            return;
        }

        data.putBoolean(
                NineHeavensPunishmentItem.HEAVENLY_TRIBULATION_REVERSED,
                true
        );

        player.removeEffect(ModMobEffects.HEAVENLY_TRIBULATION);

        player.sendOverlayMessage(
                Component.literal("天劫已反转：九雷归印，天罚已寂。"));
    }
    private void tickHeavenlyThunderSealStrike(Player player) {

        if (player.level().isClientSide()) {
            return;
        }

        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        if (!hasHeavenlyThunderSealEquipped(player)) {
            return;
        }

        if (!NineHeavensPunishmentItem.isHeavenlyTribulationReversed(player)) {
            return;
        }

        long currentTime = level.getGameTime();

        long nextStrikeTime = player.getPersistentData().getLongOr(HeavenlyThunderSealItem.NEXT_THUNDER_STRIKE_TIME, 0L);

        if (currentTime < nextStrikeTime) {
            return;
        }

        LivingEntity target = findHeavenlyThunderSealTarget(player);

        if (target == null) {
            return;
        }

        summonHeavenlyThunderSealVisualLightning(level, target);

        target.hurt(
                level.damageSources().source(
                        ModDamageTypes.HEAVENLY_THUNDER_SEAL_LIGHTNING,
                        player
                ),
                HeavenlyThunderSealItem.THUNDER_STRIKE_DAMAGE
        );

        player.getPersistentData().putLong(
                HeavenlyThunderSealItem.NEXT_THUNDER_STRIKE_TIME,
                currentTime + HeavenlyThunderSealItem.THUNDER_STRIKE_COOLDOWN
        );
    }
    private LivingEntity findHeavenlyThunderSealTarget(Player player) {

        AABB range = player.getBoundingBox().inflate(
                HeavenlyThunderSealItem.THUNDER_STRIKE_RANGE
        );

        return player.level().getEntitiesOfClass(
                        LivingEntity.class,
                        range,
                        entity -> entity.isAlive()
                                && !(entity instanceof Player)
                                && entity != player
                )
                .stream()
                .min(Comparator.comparingDouble(player::distanceToSqr))
                .orElse(null);
    }
    private void summonHeavenlyThunderSealVisualLightning(ServerLevel level, LivingEntity target) {

        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED);

        if (lightning == null) {
            return;
        }

        lightning.setPos(
                target.getX(),
                target.getY(),
                target.getZ()
        );

        lightning.setVisualOnly(true);

        level.addFreshEntity(lightning);
    }
    private boolean hasHeavenlyThunderSealEquipped(Player player) {

        final boolean[] found = {false};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("calamity").ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {

                    if (stacks.getStackInSlot(slot).is(ModItems.HEAVENLY_THUNDER_SEAL.get())) {
                        found[0] = true;
                        return;
                    }
                }
            });
        });

        return found[0];
    }
    //生桩效果
    private void tickShengZhuangKnockbackResistance(Player player) {

        if (hasShengZhuangEquipped(player)) {
            applyShengZhuangKnockbackResistance(player);
        } else {
            removeShengZhuangKnockbackResistance(player);
        }
    }
    private void applyShengZhuangKnockbackResistance(Player player) {

        AttributeInstance knockbackResistance = player.getAttribute(
                Attributes.KNOCKBACK_RESISTANCE
        );

        if (knockbackResistance == null) {
            return;
        }

        if (knockbackResistance.getModifier(
                ShengZhuangItem.KNOCKBACK_RESISTANCE_MODIFIER_ID
        ) != null) {
            return;
        }

        knockbackResistance.addTransientModifier(new AttributeModifier(
                ShengZhuangItem.KNOCKBACK_RESISTANCE_MODIFIER_ID,
                ShengZhuangItem.KNOCKBACK_RESISTANCE_BONUS,
                AttributeModifier.Operation.ADD_VALUE
        ));
    }
    private void removeShengZhuangKnockbackResistance(Player player) {

        AttributeInstance knockbackResistance = player.getAttribute(
                Attributes.KNOCKBACK_RESISTANCE
        );

        if (knockbackResistance == null) {
            return;
        }

        if (knockbackResistance.getModifier(
                ShengZhuangItem.KNOCKBACK_RESISTANCE_MODIFIER_ID
        ) != null) {
            knockbackResistance.removeModifier(
                    ShengZhuangItem.KNOCKBACK_RESISTANCE_MODIFIER_ID
            );
        }
    }

    //反转地灾
    private void tryReverseEarthDisaster(Player player) {

        if (NineHeavensPunishmentItem.isEarthDisasterReversed(player)) {
            return;
        }

        if (!hasShengZhuangEquipped(player)) {
            return;
        }

        if (player.level().dimension() != Level.NETHER) {
            return;
        }

        if (player.getBlockY() < 129) {
            return;
        }

        player.getPersistentData().putBoolean(
                NineHeavensPunishmentItem.EARTH_DISASTER_REVERSED,
                true
        );

        player.removeEffect(ModMobEffects.EARTH_DISASTER);

        player.sendOverlayMessage(
                Component.literal("地灾已反转：立桩镇地，地缚已断。"));
    }

    private boolean hasShengZhuangEquipped(Player player) {

        final boolean[] found = {false};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("calamity").ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {

                    if (stacks.getStackInSlot(slot).is(ModItems.SHENG_ZHUANG.get())) {
                        found[0] = true;
                        return;
                    }
                }
            });
        });

        return found[0];
    }
    //反转枯血
    private void tryReverseWitheredBlood(Player player) {

        if (NineHeavensPunishmentItem.isWitheredBloodReversed(player)) {
            return;
        }

        if (player.getMaxHealth() < WITHERED_BLOOD_REVERSE_MAX_HEALTH) {
            return;
        }

        player.getPersistentData().putBoolean(
                NineHeavensPunishmentItem.WITHERED_BLOOD_REVERSED,
                true
        );

        player.removeEffect(ModMobEffects.WITHERED_BLOOD);

        player.sendOverlayMessage(
                Component.literal("枯血已反转：气血充盈，生机复苏。"));
    }

    private void applySpiritExhaustion(Pre event) {

        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }

        if (!player.hasEffect(ModMobEffects.SPIRIT_EXHAUSTION)) {
            return;
        }

        event.setNewDamage(event.getNewDamage() * 0.6F);    //玩家造成伤害降低至40%
    }
    private void applyHeartDemon(Pre event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD)) {
            return;
        }

        if (NineHeavensPunishmentItem.isHeartDemonReversed(player)) {
            return;
        }

        if (!player.hasEffect(ModMobEffects.HEART_DEMON)) {
            return;
        }

        player.addEffect(new MobEffectInstance(
                MobEffects.NAUSEA,
                10 * 20,
                0,
                false,
                false,
                true
        ));
    }
    private void copyBooleanFlag(CompoundTag oldData, CompoundTag newData, String key) {

        if (oldData.getBooleanOr(key, false)) {
            newData.putBoolean(key, true);
        }
    }
    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {

        Player player = event.getEntity();

        if (!(event.getTarget() instanceof AbstractVillager)) {
            return;
        }

        if (!player.hasEffect(ModMobEffects.KARMA)) {
            return;
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.FAIL);
    }
    @SubscribeEvent
    public void onLivingHeal(LivingHealEvent event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!player.hasEffect(ModMobEffects.WITHERED_BLOOD)) {
            return;
        }

        if (shouldBlockHealing(player, event.getAmount())) {
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!player.hasEffect(ModMobEffects.WITHERED_BLOOD)) {
            return;
        }

        ItemStack stack = event.getItem();

        if (!stack.is(Items.GOLDEN_APPLE) && !stack.is(Items.ENCHANTED_GOLDEN_APPLE)) {
            return;
        }

        player.getPersistentData().putLong(
                NineHeavensPunishmentItem.GOLDEN_APPLE_REGEN_BLOCK_TIME,
                player.level().getGameTime()
        );
    }

    private boolean shouldBlockHealing(Player player, float healAmount) {

        if (isGoldenAppleRegenBlocked(player)) {
            return true;
        }

        if (isNaturalFoodHealing(player, healAmount)) {
            return true;
        }

        return false;
    }
    private boolean isGoldenAppleRegenBlocked(Player player) {

        CompoundTag data = player.getPersistentData();

        if (!data.contains(NineHeavensPunishmentItem.GOLDEN_APPLE_REGEN_BLOCK_TIME)) {
            return false;
        }

        long lastEatTime = data.getLongOr(NineHeavensPunishmentItem.GOLDEN_APPLE_REGEN_BLOCK_TIME, 0L);
        long currentTime = player.level().getGameTime();

        // 30秒内阻止金苹果/附魔金苹果带来的生命恢复
        return currentTime - lastEatTime <= 30 * 20;
    }
    private boolean isNaturalFoodHealing(Player player, float healAmount) {

        if (healAmount > 1.0F) {
            return false;
        }

        return player.getFoodData().getFoodLevel() >= 18;
    }
    private void removeWindEvilIfInactive(Player player) {

        if (!player.hasEffect(ModMobEffects.WIND_EVIL)) {
            removeWindEvilModifiers(player);
        }
    }
    private void removeWindEvilModifiers(Player player) {

        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);
        AttributeInstance armorToughness = player.getAttribute(Attributes.ARMOR_TOUGHNESS);

        if (armor != null && armor.getModifier(NineHeavensPunishmentItem.WIND_EVIL_ARMOR_MODIFIER_ID) != null) {
            armor.removeModifier(NineHeavensPunishmentItem.WIND_EVIL_ARMOR_MODIFIER_ID);
        }

        if (armorToughness != null && armorToughness.getModifier(NineHeavensPunishmentItem.WIND_EVIL_ARMOR_TOUGHNESS_MODIFIER_ID) != null) {
            armorToughness.removeModifier(NineHeavensPunishmentItem.WIND_EVIL_ARMOR_TOUGHNESS_MODIFIER_ID);
        }
    }
    // 夺运
    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {

        Entity deadEntity = event.getEntity();

        if (deadEntity.level().isClientSide()) {
            return;
        }

        boolean hasLuckDeprivationPlayerNearby =
                !deadEntity.level().getEntitiesOfClass(
                        Player.class,
                        deadEntity.getBoundingBox().inflate(LUCK_DEPRIVATION_RANGE),
                        this::isLuckDeprivationActive
                ).isEmpty();

        if (!hasLuckDeprivationPlayerNearby) {
            return;
        }

        event.getDrops().clear();
    }

    private boolean isLuckDeprivationActive(Player player) {
        return player.hasEffect(ModMobEffects.LUCK_DEPRIVATION);
    }


}