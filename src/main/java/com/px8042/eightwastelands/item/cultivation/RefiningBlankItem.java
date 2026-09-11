package com.px8042.eightwastelands.item.cultivation;

import com.px8042.eightwastelands.cultivation.CultivationRealm;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class RefiningBlankItem extends Item {

    private final CultivationRealm realm;

    public RefiningBlankItem(CultivationRealm realm, Properties properties) {
        super(properties);
        this.realm = realm;
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
                "tooltip.eightwastelands.refining_blank.realm",
                this.realm.getDisplayName()
        ).withStyle(ChatFormatting.GRAY));
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.refining_blank.refine_rule"
        ).withStyle(ChatFormatting.DARK_AQUA));
    }
}
