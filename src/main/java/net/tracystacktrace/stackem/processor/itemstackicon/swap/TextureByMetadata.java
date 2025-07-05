package net.tracystacktrace.stackem.processor.itemstackicon.swap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import org.jetbrains.annotations.NotNull;

public class TextureByMetadata {
    public static final byte STATIC = 0;
    public static final byte BETWEEN = 1;
    public static final byte BEFORE = 2;
    public static final byte AFTER = 3;
    public static final byte FOLLOWING = 4;

    public final byte compareCode;
    public final int[] compareInts;
    public final String texturePath;

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
            case BEFORE -> {
                return this.compareInts[0] < meta;
            }
            case AFTER -> {
                return this.compareInts[0] > meta;
            }
            case FOLLOWING -> {
                //noinspection ForLoopReplaceableByForEach
                for(int i = 0; i < this.compareInts.length; i++) {
                    if(this.compareInts[i] == meta) {
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

    public static @NotNull TextureByMetadata fromJson(@NotNull JsonObject object) throws IllegalArgumentException {
        byte compareCode = -1;
        int[] compareInts = null;

        if(object.has("static")) {
            compareCode = TextureByMetadata.STATIC;
            compareInts = new int[] { object.get("static").getAsInt() };
        }

        if(object.has("between")) {
            compareCode = TextureByMetadata.BETWEEN;
            compareInts = readArray(object.getAsJsonArray("between"));
        }

        if(object.has("before")) {
            compareCode = TextureByMetadata.BEFORE;
            compareInts = new int[] { object.get("before").getAsInt() };
        }

        if(object.has("after")) {
            compareCode = TextureByMetadata.AFTER;
            compareInts = new int[] { object.get("after").getAsInt() };
        }

        if(object.has("following")) {
            compareCode = TextureByMetadata.FOLLOWING;
            compareInts = readArray(object.getAsJsonArray("following"));
        }

        if(compareCode == -1 || compareInts == null) {
            throw new IllegalArgumentException("Item texture swap builder error! Cannot fetch some crap!");
        }

        final String texture = object.get("texture").getAsString();

        return new TextureByMetadata(compareCode, compareInts, texture);
    }

    private static int[] readArray(JsonArray array) {
        int[] result = new int[array.size()];
        for(int i = 0; i < result.length; i++) {
            result[i] = array.get(i).getAsInt();
        }
        return result;
    }
}
