package com.px8042.eightwastelands.item.material;

import com.px8042.eightwastelands.alchemy.AlchemyFormula;
import com.px8042.eightwastelands.alchemy.AlchemyFormulas;
import com.px8042.eightwastelands.item.ModItems;
import com.px8042.eightwastelands.item.pill.PillGrade;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class ModMaterialItem extends Item {

    public ModMaterialItem(Properties properties) {
        super(properties);
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

        appendMaterialUsageTooltip(stack, tooltipComponents);

        Optional<AlchemyFormula> formula = AlchemyFormulas.find(stack);
        if (formula.isEmpty()) {
            return;
        }

        appendAlchemyFormulaTooltip(formula.get(), tooltipComponents);
    }

    private static void appendAlchemyFormulaTooltip(
            AlchemyFormula formula,
            Consumer<Component> tooltipComponents
    ) {
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.alchemy_pill_base.type",
                formula.type().getDisplayName()
        ).withStyle(ChatFormatting.GRAY));
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.alchemy_pill_base.realm",
                formula.realm().getDisplayName()
        ).withStyle(ChatFormatting.GRAY));
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.alchemy_pill_base.required_furnace",
                formula.realm().getDisplayName()
        ).withStyle(ChatFormatting.GRAY));
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.alchemy_pill_base.cooking_time",
                formatSeconds(formula.cookingTime())
        ).withStyle(ChatFormatting.GRAY));
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.alchemy_pill_base.outputs"
        ).withStyle(ChatFormatting.DARK_AQUA));

        for (PillGrade grade : PillGrade.gradedValues()) {
            ItemStack result = formula.createResult(grade);
            if (result.isEmpty()) {
                continue;
            }

            tooltipComponents.accept(Component.translatable(
                    "tooltip.eightwastelands.alchemy_pill_base.output_entry",
                    grade.getDisplayName(),
                    result.getHoverName()
            ).withStyle(ChatFormatting.AQUA));
        }
    }

    private static String formatSeconds(int ticks) {
        double seconds = ticks / 20.0D;
        return seconds == (int) seconds
                ? Integer.toString((int) seconds)
                : String.format(java.util.Locale.ROOT, "%.1f", seconds);
    }

    private static void appendMaterialUsageTooltip(
            ItemStack stack,
            Consumer<Component> tooltipComponents
    ) {
        String key = null;
        if (stack.is(ModItems.QI_REFINING_MONSTER_CORE.get())) {
            key = "tooltip.eightwastelands.material.qi_refining_monster_core";
        } else if (stack.is(ModItems.QI_REFINING_SPIRIT_HERB.get())) {
            key = "tooltip.eightwastelands.material.qi_refining_spirit_herb";
        } else if (stack.is(ModItems.FOUNDATION_BUILDING_MONSTER_CORE.get())) {
            key = "tooltip.eightwastelands.material.foundation_building_monster_core";
        } else if (stack.is(ModItems.FOUNDATION_BUILDING_SPIRIT_HERB.get())) {
            key = "tooltip.eightwastelands.material.foundation_building_spirit_herb";
        } else if (stack.is(ModItems.WUDAO_SAND.get())) {
            key = "tooltip.eightwastelands.material.wudao_sand";
        }

        if (key != null) {
            tooltipComponents.accept(Component.translatable(key).withStyle(ChatFormatting.GRAY));
        }
    }
}
