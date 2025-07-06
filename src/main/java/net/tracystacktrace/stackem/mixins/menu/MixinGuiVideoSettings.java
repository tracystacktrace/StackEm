package net.tracystacktrace.stackem.mixins.menu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import net.tracystacktrace.stackem.gui.GuiTextureStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiVideoSettings.class)
public class MixinGuiVideoSettings extends GuiScreen {
    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void stackem$injectOpenMenu(GuiButton button, CallbackInfo ci) {
        if (button.enabled && button.id == 102) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiTextureStack(this));
            ci.cancel();
        }
    }
}
