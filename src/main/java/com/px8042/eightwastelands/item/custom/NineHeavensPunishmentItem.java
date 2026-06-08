package com.px8042.eightwastelands.item.custom;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import com.px8042.eightwastelands.effect.ModMobEffects;
import com.px8042.eightwastelands.damage.ModDamageTypes;
import com.px8042.eightwastelands.item.ModItems;
import top.theillusivec4.curios.api.CuriosApi;
import java.util.List;


public class NineHeavensPunishmentItem extends Item implements ICurioItem {

    
    private static final int TRIBULATION_EFFECT_DURATION = 40;   

    private static final String NEXT_LIGHTNING_TIME =
            "eightwastelands_next_lightning_time";


    private static final int MIN_LIGHTNING_DELAY = 35 * 20;
    private static final int MAX_LIGHTNING_DELAY = 45 * 20;
    private static final int HARDCORE_LIGHTNING_DELAY = 20 * 20;


    public static final String GOLDEN_APPLE_REGEN_BLOCK_TIME =
            "eightwastelands_golden_apple_regen_block_time";


    public static final Identifier LIFE_EXHAUSTION_MAX_HEALTH_MODIFIER_ID =
            Identifier.fromNamespaceAndPath(
                    EightWastelands.MODID,
                    "life_exhaustion_max_health"
            );

    private static final double LIFE_EXHAUSTION_MAX_HEALTH_MULTIPLIER = -0.5D;


    public static final Identifier WIND_EVIL_ARMOR_MODIFIER_ID =
            Identifier.fromNamespaceAndPath(
                    EightWastelands.MODID,
                    "wind_evil_armor"
            );

    public static final Identifier WIND_EVIL_ARMOR_TOUGHNESS_MODIFIER_ID =
            Identifier.fromNamespaceAndPath(
                    EightWastelands.MODID,
                    "wind_evil_armor_toughness"
            );

    private static final double WIND_EVIL_ATTRIBUTE_MULTIPLIER = -1.0D;

    private static final float LIGHTNING_DAMAGE_MAX_HEALTH_RATIO = 0.5F;   
    
    public static final String LUCK_DEPRIVATION_REVERSED =
            "eightwastelands_reversed_luck_deprivation";
    public static final String WIND_EVIL_REVERSED =
            "eightwastelands_reversed_wind_evil";
    public static final String LIFE_EXHAUSTION_REVERSED =
            "eightwastelands_reversed_life_exhaustion";
    public static final String HEART_DEMON_REVERSED =
            "eightwastelands_reversed_heart_demon";
    public static final String SPIRIT_EXHAUSTION_REVERSED =
            "eightwastelands_reversed_spirit_exhaustion";
    public static final String WITHERED_BLOOD_REVERSED =
            "eightwastelands_reversed_withered_blood";
    public static final String KARMA_REVERSED =
            "eightwastelands_reversed_karma";
    public static final String EARTH_DISASTER_REVERSED =
            "eightwastelands_reversed_earth_disaster";
    public static final String HEAVENLY_TRIBULATION_REVERSED =
            "eightwastelands_reversed_heavenly_tribulation";
    private static final int THUNDER_SEAL_LIGHTNING_DELAY = 2 * 20;
    private static final float THUNDER_SEAL_LIGHTNING_DAMAGE = 100.0F;  


    

    
    
    public static boolean isLuckDeprivationReversed(Player player) {
        return player.getPersistentData().getBooleanOr(LUCK_DEPRIVATION_REVERSED, false);
    }
    
    public static boolean isWindEvilReversed(Player player) {
        return player.getPersistentData().getBooleanOr(WIND_EVIL_REVERSED, false);
    }
    
    public static boolean isLifeExhaustionReversed(Player player) {
        return player.getPersistentData().getBooleanOr(LIFE_EXHAUSTION_REVERSED, false);
    }
    
    public static boolean isHeartDemonReversed(Player player) {
        return player.getPersistentData().getBooleanOr(HEART_DEMON_REVERSED, false);
    }
    
    public static boolean isSpiritExhaustionReversed(Player player) {
        return player.getPersistentData().getBooleanOr(SPIRIT_EXHAUSTION_REVERSED, false);
    }
    
    public static boolean isWitheredBloodReversed(Player player) {
        return player.getPersistentData().getBooleanOr(WITHERED_BLOOD_REVERSED, false);
    }
    
    public static boolean isKarmaReversed(Player player) {
        return player.getPersistentData().getBooleanOr(KARMA_REVERSED, false);
    }
    
    public static boolean isEarthDisasterReversed(Player player) {
        return player.getPersistentData().getBooleanOr(EARTH_DISASTER_REVERSED, false);
    }
    
    
    public static boolean isHeavenlyTribulationReversed(Player player) {
        return player.getPersistentData().getBooleanOr(HEAVENLY_TRIBULATION_REVERSED, false);
    }


    public NineHeavensPunishmentItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.identifier().equals("calamity");
    }
    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {

        if (!(slotContext.entity() instanceof Player player)) {
            return false;
        }

        return player.isCreative() || player.isSpectator();
    }
    
    private void applyTribulationMobEffects(Player player) {

        if (player.level().isClientSide()) {
            return;
        }

        if (!isHeavenlyTribulationReversed(player)) {
            player.addEffect(new MobEffectInstance(
                    ModMobEffects.HEAVENLY_TRIBULATION,
                    TRIBULATION_EFFECT_DURATION,
                    0,
                    false,
                    false,
                    true
            ));
        } else {
            player.removeEffect(ModMobEffects.HEAVENLY_TRIBULATION);
        }

        if (!isEarthDisasterReversed(player)) {
            player.addEffect(new MobEffectInstance(
                    ModMobEffects.EARTH_DISASTER,
                    TRIBULATION_EFFECT_DURATION,
                    0,
                    false,
                    false,
                    true
            ));
        } else {
            player.removeEffect(ModMobEffects.EARTH_DISASTER);
        }

        if (!isKarmaReversed(player)) {
            player.addEffect(new MobEffectInstance(
                    ModMobEffects.KARMA,
                    TRIBULATION_EFFECT_DURATION,
                    0,
                    false,
                    false,
                    true
            ));
        } else {
            player.removeEffect(ModMobEffects.KARMA);
        }

        if (!isHeartDemonReversed(player)) {
            player.addEffect(new MobEffectInstance(
                    ModMobEffects.HEART_DEMON,
                    TRIBULATION_EFFECT_DURATION,
                    0,
                    false,
                    false,
                    true
            ));
        } else {
            player.removeEffect(ModMobEffects.HEART_DEMON);
        }

        if (!isSpiritExhaustionReversed(player)) {
            player.addEffect(new MobEffectInstance(
                    ModMobEffects.SPIRIT_EXHAUSTION,
                    TRIBULATION_EFFECT_DURATION,
                    0,
                    false,
                    false,
                    true
            ));
        } else {
            player.removeEffect(ModMobEffects.SPIRIT_EXHAUSTION);
        }

        if (!isWitheredBloodReversed(player)) {
            player.addEffect(new MobEffectInstance(
                    ModMobEffects.WITHERED_BLOOD,
                    TRIBULATION_EFFECT_DURATION,
                    0,
                    false,
                    false,
                    true
            ));
        } else {
            player.removeEffect(ModMobEffects.WITHERED_BLOOD);
        }

        if (!isLuckDeprivationReversed(player)) {
            player.addEffect(new MobEffectInstance(
                    ModMobEffects.LUCK_DEPRIVATION,
                    TRIBULATION_EFFECT_DURATION,
                    0,
                    false,
                    false,
                    true
            ));
        } else {
            player.removeEffect(ModMobEffects.LUCK_DEPRIVATION);
        }

        if (!isWindEvilReversed(player)) {
            player.addEffect(new MobEffectInstance(
                    ModMobEffects.WIND_EVIL,
                    TRIBULATION_EFFECT_DURATION,
                    0,
                    false,
                    false,
                    true
            ));
        } else {
            player.removeEffect(ModMobEffects.WIND_EVIL);
        }

        if (!isLifeExhaustionReversed(player)) {
            player.addEffect(new MobEffectInstance(
                    ModMobEffects.LIFE_EXHAUSTION,
                    TRIBULATION_EFFECT_DURATION,
                    0,
                    false,
                    false,
                    true
            ));
        } else {
            player.removeEffect(ModMobEffects.LIFE_EXHAUSTION);
        }
    }


    

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {

        if (!slotContext.identifier().equals("calamity")) {
            return;
        }

        if (!(slotContext.entity() instanceof Player player)) {
            return;
        }
        applyTribulationMobEffects(player);

        


        

        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        if (player.hasEffect(ModMobEffects.HEAVENLY_TRIBULATION)) {
            tickHeavenlyTribulation(player, level);
        }


        if (player.hasEffect(ModMobEffects.LIFE_EXHAUSTION)) {
            tickLifeExhaustion(player);
        }

        if (player.hasEffect(ModMobEffects.WIND_EVIL)) {
            tickWindEvil(player);
        }
    }
    
    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            net.minecraft.world.item.component.TooltipDisplay tooltipDisplay,
            java.util.function.Consumer<Component> tooltipComponents,
            TooltipFlag tooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipComponents, tooltipFlag);
        tooltipComponents.accept(Component.translatable("tooltip.eightwastelands.nine_heavens_punishment.title")
                .withStyle(style -> style.withColor(0x550000).withBold(true)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.description"
        ).withStyle(style -> style.withColor(0x7A0000).withItalic(true)));

        tooltipComponents.accept(Component.empty());

        tooltipComponents.accept(Component.translatable("tooltip.eightwastelands.nine_heavens_punishment.flavor")
                .withStyle(style -> style.withColor(0x550000).withItalic(true)));

        tooltipComponents.accept(Component.empty());

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.heavenly_tribulation"
        ).withStyle(style -> style.withColor(0x33CCFF)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.earth_disaster"
        ).withStyle(style -> style.withColor(0x8B7355)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.karma"
        ).withStyle(style -> style.withColor(0x8A2BE2)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.heart_demon"
        ).withStyle(style -> style.withColor(0xC71585)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.spirit_exhaustion"
        ).withStyle(style -> style.withColor(0x3F7F5F)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.withered_blood"
        ).withStyle(style -> style.withColor(0xAA0000)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.luck_deprivation"
        ).withStyle(style -> style.withColor(0xB8860B)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.wind_evil"
        ).withStyle(style -> style.withColor(0xB0C4DE)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.life_exhaustion"
        ).withStyle(style -> style.withColor(0xC0C0C0)));
    }


    private void summonLightningOnPlayer(Player player, ServerLevel level) {

        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED);

        if (lightning == null) {
            return;
        }

        lightning.setPos(
                player.getX(),
                player.getY(),
                player.getZ()
        );

        
        lightning.setVisualOnly(true);

        level.addFreshEntity(lightning);

        damagePlayerByHeavenlyTribulation(player, level);
    }
    private void damagePlayerByHeavenlyTribulation(Player player, ServerLevel level) {

        float damage;

        if (hasHeavenlyThunderSealEquipped(player)
                && !isHeavenlyTribulationReversed(player)) {
            damage = THUNDER_SEAL_LIGHTNING_DAMAGE;
        } else {
            damage = player.getMaxHealth() * LIGHTNING_DAMAGE_MAX_HEALTH_RATIO;
        }

        player.hurt(
                level.damageSources().source(ModDamageTypes.HEAVENLY_TRIBULATION_LIGHTNING),
                damage
        );
    }

    private int getNextLightningDelay(ServerLevel level, Player player) {

        if (hasHeavenlyThunderSealEquipped(player)
                && !isHeavenlyTribulationReversed(player)) {
            return THUNDER_SEAL_LIGHTNING_DELAY;
        }

        if (isHardcore(level)) {
            return HARDCORE_LIGHTNING_DELAY;
        }

        return MIN_LIGHTNING_DELAY
                + level.getRandom().nextInt(MAX_LIGHTNING_DELAY - MIN_LIGHTNING_DELAY + 1);
    }

    private boolean isHardcore(ServerLevel level) {
        return level.getServer().isHardcore();
    }

    


    private void tickLifeExhaustion(Player player) {

        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);

        if (maxHealth == null) {
            return;
        }

        if (maxHealth.getModifier(LIFE_EXHAUSTION_MAX_HEALTH_MODIFIER_ID) == null) {
            maxHealth.addTransientModifier(new AttributeModifier(
                    LIFE_EXHAUSTION_MAX_HEALTH_MODIFIER_ID,
                    LIFE_EXHAUSTION_MAX_HEALTH_MULTIPLIER,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ));
        }

        float currentMaxHealth = player.getMaxHealth();

        if (player.getHealth() > currentMaxHealth) {
            player.setHealth(currentMaxHealth);
        }
    }
    private void tickHeavenlyTribulation(Player player, ServerLevel level) {

        CompoundTag data = player.getPersistentData();
        long gameTime = level.getGameTime();

        int nextDelay = getNextLightningDelay(level, player);

        if (!data.contains(NEXT_LIGHTNING_TIME)) {
            data.putLong(NEXT_LIGHTNING_TIME, gameTime + nextDelay);
            return;
        }

        long nextLightningTime = data.getLongOr(NEXT_LIGHTNING_TIME, 0L);

        
        if (hasHeavenlyThunderSealEquipped(player)
                && !isHeavenlyTribulationReversed(player)
                && nextLightningTime - gameTime > THUNDER_SEAL_LIGHTNING_DELAY) {

            data.putLong(NEXT_LIGHTNING_TIME, gameTime + THUNDER_SEAL_LIGHTNING_DELAY);
            return;
        }

        if (gameTime < nextLightningTime) {
            return;
        }

        if (level.canSeeSky(player.blockPosition())) {
            summonLightningOnPlayer(player, level);
        }

        data.putLong(NEXT_LIGHTNING_TIME, gameTime + getNextLightningDelay(level, player));
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
    
    private void tickWindEvil(Player player) {

        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);
        AttributeInstance armorToughness = player.getAttribute(Attributes.ARMOR_TOUGHNESS);

        if (armor != null && armor.getModifier(WIND_EVIL_ARMOR_MODIFIER_ID) == null) {
            armor.addTransientModifier(new AttributeModifier(
                    WIND_EVIL_ARMOR_MODIFIER_ID,
                    WIND_EVIL_ATTRIBUTE_MULTIPLIER,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ));
        }

        if (armorToughness != null && armorToughness.getModifier(WIND_EVIL_ARMOR_TOUGHNESS_MODIFIER_ID) == null) {
            armorToughness.addTransientModifier(new AttributeModifier(
                    WIND_EVIL_ARMOR_TOUGHNESS_MODIFIER_ID,
                    WIND_EVIL_ATTRIBUTE_MULTIPLIER,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ));
        }
    }
}
