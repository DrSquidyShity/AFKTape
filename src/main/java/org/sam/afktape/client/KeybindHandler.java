package org.sam.afktape.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Formatting;

import java.util.HashSet;
import java.util.Set;

import static net.minecraft.util.Formatting.*;

public class KeybindHandler {

    public static final KeybindHandler INSTANCE = new KeybindHandler();

    private final SoundManager soundManager = new SoundManager();

    private boolean running = false;
    private boolean paused = false;
    public boolean wasPaused = false;

    public Set<KeyBinding> enabledKeys = new HashSet<>();

    public boolean isRunning() {
        return running && !paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isRunningIgnorePause() {
        return running;
    }

    public void enable(Set<KeyBinding> keys) {
        running = true;
        enabledKeys.addAll(keys);
        soundManager.mute();
        MinecraftClient.getInstance().mouse.unlockCursor();
    }

    public String[] getMessage() {

        String[] msg = new String[4];

        if (MinecraftClient.getInstance().player != null) {
            StringBuilder str = new StringBuilder();
            str.append(WHITE).append("Taped down ");

            for (int i = 0; i < enabledKeys.size(); i++) {
                KeyBinding keyBinding = enabledKeys.toArray(new KeyBinding[0])[i];
                String keyName = keyBinding.getBoundKeyLocalizedText().getString().toUpperCase();
                int size = enabledKeys.size();

                if (size == 1 || i == size - 2) {
                    str.append(AQUA).append(keyName);
                } else if (i == size - 1) {
                    str.append(WHITE).append(" and ").append(AQUA).append(keyName);
                } else {
                    str.append(AQUA).append(keyName).append(WHITE).append(", ");
                }
            }

            msg[0] = str.toString();
            msg[1] = (WHITE + "Volume (" + soundManager.getOldVolume() +"%) is " + GRAY + "MUTED" + WHITE);
            msg[2] = "";
            msg[3] = (WHITE + "Press " + RED + "ESCAPE" + WHITE + " to exit");
            return msg;
        }
        return new String[0];
    }

    public void disable() {
        enabledKeys.forEach(key -> key.setPressed(false));
        enabledKeys.clear();
        soundManager.unmute();
        running = false;
        unpause();
        wasPaused = false;
        if (MinecraftClient.getInstance().currentScreen == null) MinecraftClient.getInstance().mouse.lockCursor();
    }

    public void pause() {
        paused = true;
        wasPaused = true;
    }

    public void unpause() {
        paused = false;
    }
}