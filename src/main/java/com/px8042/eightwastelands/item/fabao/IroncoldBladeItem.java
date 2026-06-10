package com.px8042.eightwastelands.item.fabao;

import com.px8042.eightwastelands.effect.ModMobEffects;
import com.px8042.eightwastelands.effect.ModNegativeEffectHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class IroncoldBladeItem extends AbstractQuickBladeFabaoItem {

    public IroncoldBladeItem(Properties properties) {
        super(properties, 4.0F, 6 * 20, 8.0D, 5);
    }

    @Override
    public void onAutoAttack(ServerPlayer player, ItemStack stack, Monster target) {
        ModNegativeEffectHelper.addStackingEffect(target, ModMobEffects.COLD_ACCUMULATION);
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
                "tooltip.eightwastelands.ironcold_blade.description_1"
        ).withStyle(style -> style.withColor(0x9AD6FF).withItalic(true)));
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.ironcold_blade.description_2"
        ).withStyle(style -> style.withColor(0x9AD6FF).withItalic(true)));
    }
}
