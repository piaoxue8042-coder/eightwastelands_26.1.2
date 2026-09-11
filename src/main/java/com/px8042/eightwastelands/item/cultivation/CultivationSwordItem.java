package com.px8042.eightwastelands.item.cultivation;

import com.px8042.eightwastelands.cultivation.CultivationRealm;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class CultivationSwordItem extends Item implements ICultivationGear {

    private final CultivationRealm requiredRealm;
    private final CultivationGearGrade gearGrade;

    public CultivationSwordItem(
            ToolMaterial material,
            float attackDamage,
            float attackSpeed,
            CultivationRealm requiredRealm,
            CultivationGearGrade gearGrade,
            Item.Properties properties
    ) {
        super(properties.sword(material, attackDamage, attackSpeed));
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
        return CultivationGearType.WEAPON;
    }

    @Override
    public CultivationGearSlot getGearSlot() {
        return CultivationGearSlot.WEAPON;
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
