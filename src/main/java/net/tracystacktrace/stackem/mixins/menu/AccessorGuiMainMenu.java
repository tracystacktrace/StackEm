package net.tracystacktrace.stackem.mixins.menu;

import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiMainMenu.class)
public interface AccessorGuiMainMenu {
    @Accessor("prideful")
    void stackem$setPrideful(boolean b);
}
