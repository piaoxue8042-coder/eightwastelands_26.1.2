package com.px8042.eightwastelands.item.fabao;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class ZaohuaPishanAxeItem extends AbstractBattleAxeFabaoItem {

    public ZaohuaPishanAxeItem(Properties properties) {
        super(properties, 25.0F, 18 * 20, 10.0D);
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
                "tooltip.eightwastelands.fuckmountain_axe.description"
        ).withStyle(style -> style.withColor(0xD8B45A).withItalic(true)));
    }
}
