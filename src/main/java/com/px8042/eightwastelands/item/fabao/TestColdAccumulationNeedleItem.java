package com.px8042.eightwastelands.item.fabao;

import com.px8042.eightwastelands.effect.ModMobEffects;
import com.px8042.eightwastelands.effect.ModNegativeEffectHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;

public class TestColdAccumulationNeedleItem extends AbstractControlNeedleFabaoItem {

    public TestColdAccumulationNeedleItem(Properties properties) {
        super(properties, 1.0F, 16.0D);
    }

    @Override
    public void onAutoAttack(ServerPlayer player, ItemStack stack, Monster target) {
        ModNegativeEffectHelper.addStackingEffect(target, ModMobEffects.COLD_ACCUMULATION);
    }
}
