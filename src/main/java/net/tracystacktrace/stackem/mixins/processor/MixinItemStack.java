package net.tracystacktrace.stackem.mixins.processor;

import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.item.ItemStack;
import net.tracystacktrace.stackem.StackEm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @Shadow
    public abstract int getItemID();

    @Shadow
    public abstract String getDisplayName();

    @Inject(method = "getIconIndex", at = @At("HEAD"), cancellable = true)
    private void stackem$injectReplaceItemStackIcon(CallbackInfoReturnable<Icon> cir) {
        if (StackEm.getContainerDeepMeta().containsCustomSwapFor(this.getItemID())) {
            Icon icon = StackEm.getContainerDeepMeta().getCustomIcon(ItemStack.class.cast(this));
            if (icon != null) {
                cir.setReturnValue(icon);
                cir.cancel();
            }
        }
    }

    @Inject(method = "getDisplayNameTilda", at = @At("HEAD"), cancellable = true)
    private void stackem$injectHideTilda(CallbackInfoReturnable<String> cir) {
        if (StackEm.CONFIG.hideTilda) {
            cir.setReturnValue(this.getDisplayName());
        }
    }
}
