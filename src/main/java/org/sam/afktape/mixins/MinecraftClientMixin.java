package org.sam.afktape.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.sam.afktape.client.AFKTapeClient;
import org.sam.afktape.client.KeybindHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    public Screen currentScreen;

    @Shadow
    public ClientPlayerEntity player;

    @Shadow @Final
    public GameOptions options;

    @Shadow @Final
    public Mouse mouse;

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void modifyTick(CallbackInfo info) {
        if (KeybindHandler.INSTANCE.isRunningIgnorePause()) {
            if (player == null || !player.isAlive()) {
                KeybindHandler.INSTANCE.disable();
            }
            if (currentScreen == null) {
                if (AFKTapeClient.unlockMouseKey.isPressed()) {
                    mouse.lockCursor();
                } else {
                    mouse.unlockCursor();
                }
            }
        }

        if (KeybindHandler.INSTANCE.isRunningIgnorePause()) {
            if (currentScreen == null) {
                if (KeybindHandler.INSTANCE.isPaused()) {
                    KeybindHandler.INSTANCE.unpause();
                }
            } else {
                if (currentScreen instanceof GameMenuScreen) {
                    options.pauseOnLostFocus = false;
                    MinecraftClient.getInstance().setScreen(null);
                    MinecraftClient.getInstance().mouse.lockCursor();
                    System.out.println("Trigger!!!!");
                }

                if (!KeybindHandler.INSTANCE.isPaused()) {
                    KeybindHandler.INSTANCE.pause();
                }
            }
        }

    }

    @Inject(at = @At("HEAD"), method = "openGameMenu(Z)V", cancellable = true) // "openPauseMenu(Z)V"
    private void modifyOpenPauseMenu(CallbackInfo info) {
        if (KeybindHandler.INSTANCE.isRunning()) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "handleInputEvents()V")
    private void modifyHandleInputEvents(CallbackInfo info) {
        if (AFKTapeClient.toggleAfkKey.wasPressed()) {
            Set<KeyBinding> pressedKeybinds = new HashSet<>();
            for (KeyBinding keyBinding : options.allKeys) {
                if (!keyBinding.isPressed()) continue;

                if (keyBinding != AFKTapeClient.toggleAfkKey) {
                    pressedKeybinds.add(keyBinding);
                }

            }
            if (!pressedKeybinds.isEmpty()) {
                KeybindHandler.INSTANCE.enable(pressedKeybinds);
            }
        }

        if (KeybindHandler.INSTANCE.wasPaused) {
            KeybindHandler.INSTANCE.enabledKeys.forEach(key -> KeyBinding.onKeyPressed(((KeyBindingAccessor) key).getBoundKey()));
            KeybindHandler.INSTANCE.wasPaused = false;
        } else {
            KeybindHandler.INSTANCE.enabledKeys.forEach(key -> key.setPressed(true));
        }
    }
}
