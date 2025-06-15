package net.tracystacktrace.stackem.mixins;

import net.minecraft.client.Minecraft;
import net.tracystacktrace.stackem.processor.TextureMerger;
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
        TextureMerger.replaceTextures();
    }
}
