package com.px8042.eightwastelands.item.custom;

import com.px8042.eightwastelands.cultivation.CultivationRealm;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

public class AlchemyFurnaceBlockItem extends BlockItem {

    private final CultivationRealm maxRealm;

    public AlchemyFurnaceBlockItem(Block block, CultivationRealm maxRealm, Properties properties) {
        super(block, properties);
        this.maxRealm = maxRealm;
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
                "tooltip.eightwastelands.alchemy_furnace.max_realm",
                this.maxRealm.getDisplayName()
        ).withStyle(ChatFormatting.GRAY));
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.alchemy_furnace.refine_rule"
        ).withStyle(ChatFormatting.DARK_AQUA));
    }
}
