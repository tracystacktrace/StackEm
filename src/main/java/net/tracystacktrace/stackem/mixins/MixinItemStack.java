package net.tracystacktrace.stackem.mixins;

import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.item.ItemStack;
import net.tracystacktrace.stackem.processor.itemstackicon.SwapCandidates;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class MixinItemStack {

    @Inject(method = "getIconIndex", at = @At("HEAD"), cancellable = true)
    private void stackem$injectReplaceItemStackIcon(CallbackInfoReturnable<Icon> cir) {
        if(SwapCandidates.isIncluded(ItemStack.class.cast(this))) {
            Icon icon = SwapCandidates.getCustomIcon(ItemStack.class.cast(this));
            if(icon != null) {
                cir.setReturnValue(icon);
                cir.cancel();
            }
        }
    }

}
