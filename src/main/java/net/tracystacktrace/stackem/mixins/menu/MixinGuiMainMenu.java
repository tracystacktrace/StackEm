package net.tracystacktrace.stackem.mixins.menu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.gui.GuiTextureStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu extends GuiScreen {
    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void stackem$injectOpenMenuMM(GuiButton button, CallbackInfo ci) {
        if (button.id == 3 || button.id == 8) {
            this.mc.displayGuiScreen(new GuiTextureStack(this));
            ci.cancel();
        }
    }

    @Redirect(method = "checkForHoliday", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/GuiMainMenu;prideful:Z",
            opcode = Opcodes.PUTFIELD
    ))
    private void stackem$checkForPride(GuiMainMenu instance, boolean value) {
        ((AccessorGuiMainMenu)instance).stackem$setPrideful(value | StackEm.CONFIG.eternalPride);
    }
}
