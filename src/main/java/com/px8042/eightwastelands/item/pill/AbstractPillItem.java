package com.px8042.eightwastelands.item.pill;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public abstract class AbstractPillItem extends Item {

    private static final int DEFAULT_USE_DURATION = 32;

    private final int useDuration;

    protected AbstractPillItem(Properties properties) {
        this(properties, DEFAULT_USE_DURATION);
    }

    protected AbstractPillItem(Properties properties, int useDuration) {
        super(properties);
        this.useDuration = useDuration;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && !canUse(player)) {
            player.sendOverlayMessage(getCannotUseMessage());
            return InteractionResult.FAIL;
        }

        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide() && livingEntity instanceof ServerPlayer player && canUse(player)) {
            applyPillEffect(player);
            player.playSound(SoundEvents.GENERIC_EAT.value(), 1.0F, 1.0F);

            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }

        return stack;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return this.useDuration;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            net.minecraft.world.item.component.TooltipDisplay tooltipDisplay,
            java.util.function.Consumer<Component> tooltipComponents,
            TooltipFlag tooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipComponents, tooltipFlag);
        appendPillTooltip(tooltipComponents);
    }

    protected abstract boolean canUse(Player player);

    protected abstract void applyPillEffect(ServerPlayer player);

    protected abstract Component getCannotUseMessage();

    protected abstract void appendPillTooltip(java.util.function.Consumer<Component> tooltipComponents);
}
