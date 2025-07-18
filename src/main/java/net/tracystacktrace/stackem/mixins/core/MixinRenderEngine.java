package net.tracystacktrace.stackem.mixins.core;

import net.minecraft.client.renderer.world.RenderEngine;
import net.tracystacktrace.stackem.modifications.StackEmModifications;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderEngine.class)
public class MixinRenderEngine {
    @Inject(method = "refreshTextures", at = @At("TAIL"))
    private void stackem$injectRefreshTextures(CallbackInfo ci) {
        StackEmModifications.fetchTextureModifications(RenderEngine.class.cast(this));
    }
}
