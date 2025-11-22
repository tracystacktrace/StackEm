package net.tracystacktrace.stackem.sagittarius.swap;

import com.google.gson.JsonObject;
import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.tracystacktrace.stackem.tools.json.JsonExtractionException;
import net.tracystacktrace.stackem.tools.json.ThrowingJson;
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

    public static void obtainArmorAdditionally(
            @NotNull SwapDescriptor swapper,
            @NotNull JsonObject object,
            @NotNull final String sourceName
    ) throws JsonExtractionException {
        if (object.has("armorTexture")) {
            final String content = ThrowingJson.cautiouslyGetString(object, "armorTexture", sourceName);
            swapper.setArmorTexture(content);
        }
        if (object.has("armorEnableColor")) {
            final Boolean content = ThrowingJson.cautiouslyGetBoolean(object, "armorEnableColor", sourceName);
            swapper.setArmorColor(content);
        }
    }
}
