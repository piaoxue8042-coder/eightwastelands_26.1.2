package com.px8042.eightwastelands.compat.damagenexus;

import com.px8042.eightwastelands.EightWastelands;
import io.github.naimjeg.damagenexus.api.event.DamageNexusRegisterEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class DamageNexusBridge {

    @SubscribeEvent
    public void onDamageNexusRegister(DamageNexusRegisterEvent event) {
        event.registerPhaseProcessor(new AttackFormResistanceProcessor());
        EightWastelands.LOGGER.info("DamageNexus bridge initialized for Eight Wastelands.");
    }
}
