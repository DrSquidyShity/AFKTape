package org.sam.afktape.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class AFKTapeClient implements ClientModInitializer {

    public static KeyBinding toggleAfkKey;
    public static KeyBinding unlockMouseKey;

    @Override
    public void onInitializeClient() {
        // Register keybind (Default: K)
        toggleAfkKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.afktape.toggle",
                GLFW.GLFW_KEY_K,
                "category.afktape"
        ));

        // Register keybind (Default: Left Alt)
        unlockMouseKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.afktape.mouse",
                GLFW.GLFW_KEY_LEFT_ALT,
                "category.afktape"
        ));
    }
}
