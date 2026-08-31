package com.px8042.eightwastelands.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.px8042.eightwastelands.EightWastelands;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public final class ModKeyMappings {

    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(
            Identifier.fromNamespaceAndPath(EightWastelands.MODID, "eight_wastelands")
    );

    public static final KeyMapping OPEN_CULTIVATION = new KeyMapping(
            "key.eightwastelands.open_cultivation",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            CATEGORY
    );

    private ModKeyMappings() {
    }

    public static void register(RegisterKeyMappingsEvent event) {
        event.registerCategory(CATEGORY);
        event.register(OPEN_CULTIVATION);
    }
}
