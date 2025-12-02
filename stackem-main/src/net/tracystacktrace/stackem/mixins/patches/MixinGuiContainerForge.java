package net.tracystacktrace.stackem.mixins.patches;

import net.minecraft.client.gui.GuiContainerForge;
import net.minecraft.client.renderer.world.RenderEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiContainerForge.class)
public class MixinGuiContainerForge {
    @Redirect(method = "drawGuiContainerBackgroundLayer", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/world/RenderEngine;getTexture(Ljava/lang/String;)I"))
    private int stackem$patchForgeTextureGUI(RenderEngine instance, String arg1) {
        return instance.getTexture("/textures/gui/container/forge.png");
    }
}
