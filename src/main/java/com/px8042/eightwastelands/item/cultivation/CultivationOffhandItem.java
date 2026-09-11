package com.px8042.eightwastelands.item.cultivation;

import com.px8042.eightwastelands.cultivation.CultivationRealm;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class CultivationOffhandItem extends Item implements ICultivationGear {

    private final CultivationRealm requiredRealm;
    private final CultivationGearGrade gearGrade;

    public CultivationOffhandItem(
            CultivationRealm requiredRealm,
            CultivationGearGrade gearGrade,
            Properties properties
    ) {
        super(properties);
        this.requiredRealm = requiredRealm;
        this.gearGrade = gearGrade;
    }

    @Override
    public CultivationRealm getRequiredRealm() {
        return this.requiredRealm;
    }

    @Override
    public CultivationGearGrade getGearGrade() {
        return this.gearGrade;
    }

    @Override
    public CultivationGearType getGearType() {
        return CultivationGearType.OFFHAND;
    }

    @Override
    public CultivationGearSlot getGearSlot() {
        return CultivationGearSlot.OFFHAND;
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
        appendCultivationGearTooltip(stack, tooltipComponents);
    }
}
