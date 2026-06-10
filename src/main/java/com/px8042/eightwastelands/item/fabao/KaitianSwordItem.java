package com.px8042.eightwastelands.item.fabao;

import com.px8042.eightwastelands.damage.ModDamageTypes;
import com.px8042.eightwastelands.effect.ModMobEffects;
import com.px8042.eightwastelands.effect.ModNegativeEffectHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class KaitianSwordItem extends AbstractHeavySwordFabaoItem {

    public KaitianSwordItem(Properties properties) {
        super(properties, 25.0F, 18 * 20, 16.0D);
    }

    @Override
    public DamageSource createDamageSource(ServerLevel level, ServerPlayer player, ItemStack stack, Monster target) {
        return level.damageSources().source(ModDamageTypes.FABAO_ARMOR_PIERCING, player);
    }

    @Override
    public void onAutoAttack(ServerPlayer player, ItemStack stack, Monster target) {
        ModNegativeEffectHelper.addStackingEffect(target, ModMobEffects.FLAME_BAN);
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            TooltipDisplay tooltipDisplay,
            Consumer<Component> tooltipComponents,
            TooltipFlag tooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipComponents, tooltipFlag);

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.fucksky_sword.description"
        ).withStyle(style -> style.withColor(0xE0B06A).withItalic(true)));
    }
}
