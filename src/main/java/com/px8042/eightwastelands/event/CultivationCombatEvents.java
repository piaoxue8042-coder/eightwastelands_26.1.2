package com.px8042.eightwastelands.event;

import com.px8042.eightwastelands.cultivation.CultivationCombatManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class CultivationCombatEvents {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLivingDamage(LivingDamageEvent.Pre event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) {
            return;
        }

        Entity sourceEntity = event.getSource().getEntity();
        if (!(sourceEntity instanceof LivingEntity attacker)) {
            return;
        }

        if (attacker == target) {
            return;
        }

        float damage = event.getNewDamage();
        if (damage <= 0.0F) {
            return;
        }

        double multiplier = CultivationCombatManager.getDamageMultiplier(attacker, target);
        if (multiplier == 1.0D) {
            return;
        }

        event.setNewDamage((float) Math.max(0.0D, damage * multiplier));
    }
}
