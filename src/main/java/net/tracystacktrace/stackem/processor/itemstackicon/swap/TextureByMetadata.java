package net.tracystacktrace.stackem.processor.itemstackicon.swap;

import com.google.gson.JsonObject;
import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.tracystacktrace.stackem.tools.JsonReadHelper;
import org.jetbrains.annotations.NotNull;

public class TextureByMetadata {
    public static final byte STATIC = 0;
    public static final byte BETWEEN = 1;
    public static final byte BELOW = 2;
    public static final byte AFTER = 3;
    public static final byte FOLLOWING = 4;

    public final byte compareCode;
    public final int[] compareInts;
    public final String texturePath;

    protected int priority = 0;
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

    public int getPriority() {
        return this.priority;
    }

    public static @NotNull TextureByMetadata fromJson(@NotNull JsonObject object) throws IllegalArgumentException {
        byte compareCode = -1;
        int[] compareInts = null;

        if (object.has("static")) {
            compareCode = TextureByMetadata.STATIC;
            compareInts = new int[]{object.get("static").getAsInt()};
        }

        if (object.has("between")) {
            compareCode = TextureByMetadata.BETWEEN;
            compareInts = JsonReadHelper.readIntArray(object.getAsJsonArray("between"));
        }

        if (object.has("below")) {
            compareCode = TextureByMetadata.BELOW;
            compareInts = new int[]{object.get("below").getAsInt()};
        }

        if (object.has("after")) {
            compareCode = TextureByMetadata.AFTER;
            compareInts = new int[]{object.get("after").getAsInt()};
        }

        if (object.has("following")) {
            compareCode = TextureByMetadata.FOLLOWING;
            compareInts = JsonReadHelper.readIntArray(object.getAsJsonArray("following"));
        }

        if (compareCode == -1 || compareInts == null) {
            throw new IllegalArgumentException("Item texture swap builder error! Cannot fetch some crap!");
        }

        final String texture = object.get("texture").getAsString();

        final TextureByMetadata compiled = new TextureByMetadata(compareCode, compareInts, texture);

        if(object.has("priority")) {
            compiled.priority = object.get("priority").getAsInt();
        }

        return compiled;
    }
}
