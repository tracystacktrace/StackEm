package net.tracystacktrace.stackem.mixins;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.tracystacktrace.stackem.gui.GuiTextureStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu extends GuiScreen {

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void stacken$injectOpenMenuMM(GuiButton button, CallbackInfo ci) {
        if (button.id == 3 || button.id == 8) {
            this.mc.displayGuiScreen(new GuiTextureStack(this));
            ci.cancel();
        }
    }

}
