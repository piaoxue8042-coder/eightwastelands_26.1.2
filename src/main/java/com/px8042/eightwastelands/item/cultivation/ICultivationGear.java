package com.px8042.eightwastelands.item.cultivation;

import com.px8042.eightwastelands.cultivation.CultivationManager;
import com.px8042.eightwastelands.cultivation.CultivationRealm;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public interface ICultivationGear {

    CultivationRealm getRequiredRealm();

    CultivationGearGrade getGearGrade();

    CultivationGearType getGearType();

    CultivationGearSlot getGearSlot();

    default boolean canBeUsedBy(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return true;
        }

        return CultivationManager.getStage(serverPlayer)
                .getRealm()
                .map(realm -> realm.isAtLeast(getRequiredRealm()))
                .orElse(false);
    }

    default void appendCultivationGearTooltip(ItemStack stack, Consumer<Component> tooltipComponents) {
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.cultivation_gear.type",
                getGearType().getDisplayName()
        ).withStyle(ChatFormatting.GRAY));
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.cultivation_gear.realm",
                getRequiredRealm().getDisplayName()
        ).withStyle(ChatFormatting.GRAY));
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.cultivation_gear.grade",
                GearInstanceData.getGrade(stack, getGearGrade()).getDisplayName()
        ).withStyle(ChatFormatting.GRAY));
        GearInstanceData.appendTooltip(stack, tooltipComponents);
    }
}
