package net.tracystacktrace.stackem.mixins.core;

import net.minecraft.client.Minecraft;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.processor.StackEmModifications;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(method = "startGame", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/FontRenderer;setBidiFlag(Z)V"))
    private void stackem$injectPreloadTextures(CallbackInfo ci) {
        StackEmModifications.fetchTextureModifications();
    }

    @Inject(method = "run", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;screenshotListener()V"))
    private void stackem$injectF10Toggle(CallbackInfo ci) {
        if (Keyboard.isKeyDown(StackEm.DEBUG_KEYBIND_FALLBACK.keyCode)) {
            StackEm.toggleDefaultTextures();
        }
    }
}
