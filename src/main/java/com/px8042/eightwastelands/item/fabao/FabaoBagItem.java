package com.px8042.eightwastelands.item.fabao;

import com.px8042.eightwastelands.menu.FabaoBagMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.function.Consumer;

public class FabaoBagItem extends Item implements ICurioItem {

    public static final String SLOT_ID = "fabao_bag";
    public static final int SLOT_COUNT = 6;

    public FabaoBagItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.identifier().equals(SLOT_ID);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack bagStack = player.getItemInHand(hand);

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(
                    new SimpleMenuProvider(
                            (containerId, inventory, menuPlayer) -> new FabaoBagMenu(containerId, inventory, bagStack),
                            Component.translatable("container.eightwastelands.fabao_bag")
                    )
            );
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
                "tooltip.eightwastelands.fabao_bag.description"
        ).withStyle(style -> style.withColor(0xD7B96B).withItalic(true)));

        tooltipComponents.accept(Component.translatable(
                "tooltip.eightwastelands.fabao_bag.slots",
                getFilledSlotCount(stack),
                SLOT_COUNT
        ).withStyle(ChatFormatting.GRAY));
    }

    public static boolean isValidFabaoStack(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof IFabaoItem;
    }

    public static NonNullList<ItemStack> getContents(ItemStack bagStack) {
        NonNullList<ItemStack> contents = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
        bagStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(contents);
        return contents;
    }

    public static int getFilledSlotCount(ItemStack bagStack) {
        int count = 0;
        for (ItemStack stack : getContents(bagStack)) {
            if (!stack.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    public static String cooldownKey(int slot) {
        return "slot_" + slot + "_cooldown_end";
    }

    public static long getCooldownEnd(ItemStack bagStack, int slot) {
        if (!isValidSlot(slot)) {
            return 0L;
        }

        CustomData customData = bagStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return customData.copyTag().getLongOr(cooldownKey(slot), 0L);
    }

    public static boolean isSlotReady(ItemStack bagStack, int slot, long gameTime) {
        return gameTime >= getCooldownEnd(bagStack, slot);
    }

    public static void setCooldownEnd(ItemStack bagStack, int slot, long cooldownEnd) {
        if (bagStack.isEmpty() || !isValidSlot(slot)) {
            return;
        }

        CustomData.update(DataComponents.CUSTOM_DATA, bagStack, tag -> updateCooldownTag(tag, slot, cooldownEnd));
    }

    private static void updateCooldownTag(CompoundTag tag, int slot, long cooldownEnd) {
        String key = cooldownKey(slot);
        if (cooldownEnd <= 0L) {
            tag.remove(key);
        } else {
            tag.putLong(key, cooldownEnd);
        }
    }

    private static boolean isValidSlot(int slot) {
        return slot >= 0 && slot < SLOT_COUNT;
    }
}
