package com.px8042.eightwastelands.item.storage;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

public class StorageBagItem extends Item {

    private final StorageBagTier tier;

    public StorageBagItem(StorageBagTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public StorageBagTier getTier() {
        return this.tier;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            ItemStack storageBag = player.getItemInHand(hand);
            StorageBagContainer container = new StorageBagContainer(storageBag, this.tier, player.getInventory());
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (containerId, inventory, ignoredPlayer) -> createMenu(containerId, inventory, container),
                    this.getName(storageBag)
            ));
        }
        return InteractionResult.SUCCESS;
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
                "tooltip.eightwastelands.storage_bag.capacity",
                this.tier.getCapacity()
        ).withStyle(ChatFormatting.GRAY));
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.storage_bag.right_click"
        ).withStyle(ChatFormatting.DARK_AQUA));
        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.storage_bag.auto_store"
        ).withStyle(ChatFormatting.GRAY));
    }

    private ChestMenu createMenu(int containerId, net.minecraft.world.entity.player.Inventory inventory,
                                 StorageBagContainer container) {
        return switch (this.tier.getMenuRows()) {
            case 4 -> new ChestMenu(MenuType.GENERIC_9x4, containerId, inventory, container, 4);
            case 6 -> ChestMenu.sixRows(containerId, inventory, container);
            default -> ChestMenu.threeRows(containerId, inventory, container);
        };
    }
}
