package net.tracystacktrace.stackem.mixins;

import net.minecraft.client.renderer.block.TextureMap;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.processor.StackEmModifications;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureMap.class)
public class MixinTextureMap {
    @Inject(method = "refreshTextures", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/TextureManager;createStitcher(Ljava/lang/String;)Lnet/minecraft/client/renderer/block/icon/Stitcher;", shift = At.Shift.AFTER))
    private void stackem$registerCustomIcons(CallbackInfo ci) {
        StackEmModifications.fetchIconModifications();
        StackEm.getContainerDeepMeta().registerAllIcons(TextureMap.class.cast(this));
    }
}
