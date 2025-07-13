package net.tracystacktrace.stackem.processor.iconswap.swap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.tracystacktrace.stackem.processor.iconswap.IconProcessorException;
import net.tracystacktrace.stackem.tools.JsonReadHelper;
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

    public static @NotNull String obtainTexture(@NotNull JsonObject object) throws IconProcessorException {
        if (object.has("texture")) {
            final JsonElement element = object.get("texture");
            final String content = JsonReadHelper.readString(element);
            if (content != null) {
                return content;
            }
            throw new IconProcessorException(IconProcessorException.INVALID_TEXTURE, element.toString());
        }
        throw new IconProcessorException(IconProcessorException.NOT_FOUND_TEXTURE);
    }

    public static int obtainPriority(@NotNull JsonObject object) throws IconProcessorException {
        if (object.has("priority")) {
            final JsonElement element = object.get("priority");
            final Integer content = JsonReadHelper.readInteger(element);
            if (content != null) {
                return content;
            }
            throw new IconProcessorException(IconProcessorException.INVALID_PRIORITY, element.toString());
        }
        return 0;
    }

    public static void obtainArmorAdditionally(@NotNull SwapDescriptor swapper, @NotNull JsonObject object) throws IconProcessorException {
        if (object.has("armorTexture")) {
            final JsonElement element = object.get("armorTexture");
            final String content = JsonReadHelper.readString(element);

            if (content != null) {
                swapper.setArmorTexture(content);
            } else {
                throw new IconProcessorException(IconProcessorException.INVALID_ARMOR_TEXTURE, element.toString());
            }
        }
        if (object.has("armorEnableColor")) {
            final JsonElement element = object.get("armorEnableColor");
            final Boolean content = JsonReadHelper.readBoolean(element);

            if (content != null) {
                swapper.setArmorColor(content);
            } else {
                throw new IconProcessorException(IconProcessorException.INVALID_ENABLE_COLOR, element.toString());
            }
        }
    }
}
