package com.px8042.eightwastelands.event;

import com.px8042.eightwastelands.cultivation.CultivationMonsterManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

public class CultivationMonsterEvents {

    @SubscribeEvent
    public void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Monster monster) {
            CultivationMonsterManager.assignOnSpawn(monster);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onLivingDrops(LivingDropsEvent event) {
        if (!(event.getSource().getEntity() instanceof Player)) {
            return;
        }

        CultivationMonsterManager.dropMaterialsFor(event.getEntity());
    }
}
