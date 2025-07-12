package net.tracystacktrace.stackem.mixins.processor.iconswap;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.common.entity.EntityLiving;
import net.minecraft.common.item.ItemStack;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.processor.StackEmModifications;
import net.tracystacktrace.stackem.processor.iconswap.ItemIconSwap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderBiped.class)
public abstract class MixinRenderBiped extends EntityRenderer {
    @Inject(method = "renderArmorSlot", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/RenderBiped;loadTexture(Ljava/lang/String;)V",
            shift = At.Shift.AFTER
    ))
    private void stackem$injectReplaceArmorTexture(EntityLiving entity, int pass, float deltaTicks, CallbackInfo ci) {
        final String replacement = StackEm.getContainerDeepMeta().getCustomArmor(entity.getCurrentArmor(3 - pass));
        if(replacement != null) {
            loadTexture(replacement);
        }
    }
}
