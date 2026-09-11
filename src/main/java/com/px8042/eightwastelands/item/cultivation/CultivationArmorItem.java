package com.px8042.eightwastelands.item.cultivation;

import com.px8042.eightwastelands.cultivation.CultivationRealm;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.function.Consumer;

public class CultivationArmorItem extends Item implements ICultivationGear {

    private final CultivationRealm requiredRealm;
    private final CultivationGearGrade gearGrade;
    private final CultivationGearSlot gearSlot;

    public CultivationArmorItem(
            ArmorMaterial material,
            ArmorType armorType,
            CultivationRealm requiredRealm,
            CultivationGearGrade gearGrade,
            CultivationGearSlot gearSlot,
            Properties properties
    ) {
        super(properties.humanoidArmor(material, armorType));
        this.requiredRealm = requiredRealm;
        this.gearGrade = gearGrade;
        this.gearSlot = gearSlot;
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
        return CultivationGearType.ARMOR;
    }

    @Override
    public CultivationGearSlot getGearSlot() {
        return this.gearSlot;
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
