package net.tracystacktrace.stackem.processor.iconswap.swap;

import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SwapDescriptor {
    public final String texturePath;
    public final int priority;

    public Icon textureIcon;
    protected String armorTexture = null;
    protected boolean armorEnableColor = true;

    public SwapDescriptor(@NotNull String texturePath, int priority) {
        this.texturePath = texturePath;
        this.priority = priority;
    }

    public void registerIcon(@NotNull IconRegister register) {
        this.textureIcon = register.registerIcon(this.texturePath);
    }

    public int getPriority() {
        return this.priority;
    }

    public boolean hasArmorTexture() {
        return this.armorTexture != null;
    }

    public boolean hasArmorColor() {
        return this.armorEnableColor;
    }

    public void setArmorColor(boolean b) {
        this.armorEnableColor = b;
    }

    public @Nullable String getArmorTexture() {
        return this.armorTexture;
    }

    public void setArmorTexture(@NotNull String s) {
        this.armorTexture = s;
    }
}
