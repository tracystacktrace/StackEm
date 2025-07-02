package net.tracystacktrace.stackem.mixins;

import com.indigo3d.util.RenderSystem;
import net.minecraft.client.renderer.world.RenderGlobal;
import net.minecraft.client.renderer.world.Tessellator;
import net.minecraft.common.world.World;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.impl.DeepMeta;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @Shadow
    private World worldObj;

    @Unique
    boolean stackem$moonTrigger1;

    @Redirect(method = "renderSky", at = @At(
            value = "INVOKE",
            target = "Lcom/indigo3d/util/RenderSystem;bindTexture2D(Ljava/lang/String;)V"))
    private void stackem$putCustomMoonTexture(String textureLocation) {
        if (StackEm.getContainerInstance().isCustomMoon()) {
            if (textureLocation.equals("/textures/environment/moon.png"))
                textureLocation = StackEm.getContainerInstance().getDeepMeta().getMoonTexture();
        }
        RenderSystem.bindTexture2D(textureLocation);
    }

    @Redirect(method = "renderSky", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/world/Tessellator;addVertexWithUV(DDDDD)V"))
    private void stackem$hijackMoonVertexes(Tessellator instance, double x, double y, double z, double u, double v) {
        if (StackEm.getContainerInstance().isCustomMoon()) {
            if ((y == -100) && !this.stackem$moonTrigger1) {
                this.stackem$moonTrigger1 = true;

                final DeepMeta deepMeta = StackEm.getContainerInstance().getDeepMeta();

                final int moonPhase = deepMeta.getMoonCycle(this.worldObj.getWorldTime());
                final int amX = deepMeta.getMoonHorizontals();
                final int amY = deepMeta.getMoonVerticals();
                final float moonSize = deepMeta.getMoonScale();

                final int phaseX = moonPhase % amX;
                final int phaseY = (moonPhase / amX) % amY;

                final float u1 = (float) phaseX / ((float) amX);
                final float v1 = (float) phaseY / ((float) amY);
                final float u2 = (float) (phaseX + 1) / ((float) amX);
                final float v2 = (float) (phaseY + 1) / ((float) amY);

                instance.addVertexWithUV(-moonSize, -100.0D, moonSize, u2, v2);
                instance.addVertexWithUV(moonSize, -100.0D, moonSize, u1, v2);
                instance.addVertexWithUV(moonSize, -100.0D, -moonSize, u1, v1);
                instance.addVertexWithUV(-moonSize, -100.0D, -moonSize, u2, v1);
                return;
            }

            if (y == -100) {
                return;
            }
        }

        instance.addVertexWithUV(x, y, z, u, v);
    }

    @Inject(method = "renderSky", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/world/Tessellator;draw()V"))
    private void stackem$hijackMoonRevert(float deltaTicks, CallbackInfo ci) {
        if (this.stackem$moonTrigger1) this.stackem$moonTrigger1 = false;
    }

}
