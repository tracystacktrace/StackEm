package net.tracystacktrace.stackem.mixins.patches;

import net.minecraft.client.gui.GuiContainerChest;
import net.minecraft.client.renderer.world.RenderEngine;
import net.minecraft.common.entity.inventory.IInventory;
import net.minecraft.common.entity.player.InventoryDimensionalChest;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiContainerChest.class)
public class MixinGuiContainerChest {
    @Shadow @Final private IInventory lowerChestInventory;

    @Redirect(method = "drawGuiContainerBackgroundLayer", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/world/RenderEngine;getTexture(Ljava/lang/String;)I"
    ))
    private int stackem$patchDimensionChestTextureGUI(RenderEngine instance, String arg1) {
        if(this.lowerChestInventory instanceof InventoryDimensionalChest) {
            arg1 = "/textures/gui/container/dimensional_chest.png";
        }

        return instance.getTexture(arg1);
    }
}
