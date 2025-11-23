package net.tracystacktrace.stackem.mixins.processor.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.common.entity.other.EntityMinecart;
import net.tracystacktrace.stackem.hacks.ITextureHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderMinecart.class)
public abstract class MixinRenderMinecart extends EntityRenderer<EntityMinecart> {

    @Inject(method = "doRender(Lnet/minecraft/common/entity/other/EntityMinecart;DDDFF)V", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/RenderMinecart;loadTexture(Ljava/lang/String;)V",
            shift = At.Shift.AFTER,
            ordinal = 1
    ))
    private void stackem$entityVariation(EntityMinecart entity, double x, double y, double z, float yaw, float deltaTicks, CallbackInfo ci) {
        this.loadTexture(((ITextureHolder)entity).stackem$getCachedTexture());
    }
}
