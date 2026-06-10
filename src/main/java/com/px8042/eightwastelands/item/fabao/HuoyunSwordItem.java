package com.px8042.eightwastelands.item.fabao;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class HuoyunSwordItem extends AbstractLongSwordFabaoItem {

    public HuoyunSwordItem(Properties properties) {
        super(properties, 10.0F, 10 * 20, 12.0D);
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
                "tooltip.eightwastelands.huoyun_sword.description"
        ).withStyle(style -> style.withColor(0xBFC9D8).withItalic(true)));
    }
}
