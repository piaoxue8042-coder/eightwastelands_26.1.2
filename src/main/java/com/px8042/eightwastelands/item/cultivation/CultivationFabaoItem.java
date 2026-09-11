package com.px8042.eightwastelands.item.cultivation;

import com.px8042.eightwastelands.cultivation.CultivationRealm;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.function.Consumer;

public class CultivationFabaoItem extends Item implements ICurioItem, ICultivationGear {

    private final CultivationRealm requiredRealm;
    private final CultivationGearGrade gearGrade;

    public CultivationFabaoItem(
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
        return CultivationGearType.FABAO;
    }

    @Override
    public CultivationGearSlot getGearSlot() {
        return CultivationGearSlot.FABAO;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.entity() instanceof Player player && canBeUsedBy(player);
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
