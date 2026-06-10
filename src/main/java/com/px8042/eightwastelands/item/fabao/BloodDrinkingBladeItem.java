package com.px8042.eightwastelands.item.fabao;

import com.px8042.eightwastelands.effect.ModNegativeEffectHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class BloodDrinkingBladeItem extends AbstractBloodBladeFabaoItem {

    public BloodDrinkingBladeItem(Properties properties) {
        super(properties, 3.0F, 12 * 20, 6.0D);
    }

    @Override
    public void onAutoAttack(ServerPlayer player, ItemStack stack, Monster target) {
        ModNegativeEffectHelper.addBleeding(target);
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
                "tooltip.eightwastelands.blood_drinking_blade.description"
        ).withStyle(style -> style.withColor(0xB33A3A).withItalic(true)));
    }
}
