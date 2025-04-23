package org.sam.afktape.mixins;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.sam.afktape.client.KeybindHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    public abstract TextRenderer getTextRenderer();

    // Render HUD when running
    @Inject(at = @At("TAIL"), method = "render")
    private void modifyRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {

        if (KeybindHandler.INSTANCE.isRunning()) {
            TextRenderer renderer = getTextRenderer();
            String[] lines = KeybindHandler.INSTANCE.getMessage();

            int scaledWidth = context.getScaledWindowWidth();
            int scaledHeight = context.getScaledWindowHeight();

            int additive = 0;
            for (int i = lines.length - 1; i >= 0; i--) {
                int width = renderer.getWidth(lines[i]);

                context.drawText(
                        renderer,
                        lines[i],
                        (scaledWidth / 2) - (width / 2),
                        ((scaledHeight / 2) - 20) + additive,
                        0xFFFFFF, // white text
                        true // with shadow
                );
                additive -= 10;
            }
        }
    }
}
