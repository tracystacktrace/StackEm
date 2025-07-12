package net.tracystacktrace.stackem.processor.iconswap.swap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.tracystacktrace.stackem.processor.iconswap.IconProcessorException;
import net.tracystacktrace.stackem.processor.iconswap.IconSwapReader;
import net.tracystacktrace.stackem.tools.JsonReadHelper;
import org.jetbrains.annotations.NotNull;

public class TextureByMetadata implements ISwapper {
    public static final byte STATIC = 0;
    public static final byte BETWEEN = 1;
    public static final byte BELOW = 2;
    public static final byte AFTER = 3;
    public static final byte FOLLOWING = 4;

    public final byte compareCode;
    public final int[] compareInts;
    public final String texturePath;

    protected int priority = 0;
    protected String armorTexture = null;
    protected boolean armorEnableColor = false;
    public Icon textureIcon;

    public TextureByMetadata(byte compareCode, int @NotNull [] compareInts, @NotNull String texturePath) {
        this.compareCode = compareCode;
        this.compareInts = compareInts;
        this.texturePath = texturePath;
    }

    public boolean compare(int meta) {
        switch (this.compareCode) {
            case STATIC -> {
                return this.compareInts[0] == meta;
            }
            case BETWEEN -> {
                return this.compareInts[0] <= meta && this.compareInts[1] >= meta;
            }
            case BELOW -> {
                return this.compareInts[0] < meta;
            }
            case AFTER -> {
                return this.compareInts[0] > meta;
            }
            case FOLLOWING -> {
                //noinspection ForLoopReplaceableByForEach
                for (int i = 0; i < this.compareInts.length; i++) {
                    if (this.compareInts[i] == meta) {
                        return true;
                    }
                }
                return false;
            }
        }

        return false;
    }

    public void registerIcon(IconRegister register) {
        this.textureIcon = register.registerIcon(this.texturePath);
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public boolean hasArmorTexture() {
        return this.armorTexture != null;
    }

    @Override
    public String getArmorTexture() {
        return this.armorTexture;
    }

    @Override
    public void setArmorTexture(@NotNull String s) {
        this.armorTexture = s;
    }

    @Override
    public void setEnableColor(boolean b) {
        this.armorEnableColor = b;
    }

    @Override
    public boolean hasColor() {
        return this.armorEnableColor;
    }

    public static @NotNull TextureByMetadata fromJson(@NotNull JsonObject object) throws IconProcessorException {
        byte compareCode = -1;
        int[] compareInts = null;

        if (object.has("static")) {
            final JsonElement inputElement = object.get("static");
            if (inputElement.isJsonPrimitive() && inputElement.getAsJsonPrimitive().isNumber()) {
                compareCode = TextureByMetadata.STATIC;
                compareInts = new int[]{inputElement.getAsInt()};
            }
        }

        if (object.has("between")) {
            final JsonElement inputElement = object.get("between");
            if (inputElement.isJsonArray()) {
                compareCode = TextureByMetadata.BETWEEN;
                compareInts = JsonReadHelper.readIntArray(inputElement.getAsJsonArray());
            }
        }

        if (object.has("below")) {
            final JsonElement inputElement = object.get("below");
            if (inputElement.isJsonPrimitive() && inputElement.getAsJsonPrimitive().isNumber()) {
                compareCode = TextureByMetadata.BELOW;
                compareInts = new int[]{inputElement.getAsInt()};
            }
        }

        if (object.has("after")) {
            final JsonElement inputElement = object.get("after");
            if (inputElement.isJsonPrimitive() && inputElement.getAsJsonPrimitive().isNumber()) {
                compareCode = TextureByMetadata.AFTER;
                compareInts = new int[]{inputElement.getAsInt()};
            }
        }

        if (object.has("following")) {
            final JsonElement inputElement = object.get("following");
            if (inputElement.isJsonArray()) {
                compareCode = TextureByMetadata.FOLLOWING;
                compareInts = JsonReadHelper.readIntArray(inputElement.getAsJsonArray());
            }
        }

        if (compareCode == -1) {
            throw new IconProcessorException(IconProcessorException.INVALID_COMPARABLE_CODE);
        }

        final String texture = IconSwapReader.obtainTexture(object);
        final Integer priority = IconSwapReader.obtainPriority(object);

        final TextureByMetadata compiled = new TextureByMetadata(compareCode, compareInts, texture);

        if (priority != null) {
            compiled.priority = priority;
        }

        IconSwapReader.obtainArmorIfPossible(compiled, object);

        return compiled;
    }
}
