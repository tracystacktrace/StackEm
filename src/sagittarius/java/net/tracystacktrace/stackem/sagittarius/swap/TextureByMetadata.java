package net.tracystacktrace.stackem.sagittarius.swap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.tracystacktrace.stackem.sagittarius.IconProcessorException;
import net.tracystacktrace.stackem.tools.JsonExtractionException;
import net.tracystacktrace.stackem.tools.JsonReadHelper;
import net.tracystacktrace.stackem.tools.ThrowingJson;
import org.jetbrains.annotations.NotNull;

public class TextureByMetadata extends SwapDescriptor {
    public static final byte STATIC = 0;
    public static final byte BETWEEN = 1;
    public static final byte BELOW = 2;
    public static final byte AFTER = 3;
    public static final byte FOLLOWING = 4;

    public final byte compareCode;
    public final int[] compareInts;

    public TextureByMetadata(
            byte compareCode,
            int @NotNull [] compareInts,
            @NotNull String texturePath,
            int priority
    ) {
        super(texturePath, priority);
        this.compareCode = compareCode;
        this.compareInts = compareInts;
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

    public static @NotNull TextureByMetadata fromJson(
            @NotNull final JsonObject object,
            @NotNull final String sourceName
    ) throws IconProcessorException, JsonExtractionException {
        byte compareCode = -1;
        int[] compareInts = null;

        if (object.has("static")) {
            compareInts = new int[]{ThrowingJson.cautiouslyGetInt(object, "static", sourceName)};
            compareCode = TextureByMetadata.STATIC;
        }

        if (object.has("between")) {
            compareInts = ThrowingJson.cautiouslyGetIntArray(object, "between", sourceName);
            compareCode = TextureByMetadata.BETWEEN;
        }

        if (object.has("below")) {
            compareInts = new int[]{ThrowingJson.cautiouslyGetInt(object, "below", sourceName)};
            compareCode = TextureByMetadata.BELOW;
        }

        if (object.has("after")) {
            compareInts = new int[]{ThrowingJson.cautiouslyGetInt(object, "after", sourceName)};
            compareCode = TextureByMetadata.AFTER;
        }

        if (object.has("following")) {
            compareInts = ThrowingJson.cautiouslyGetIntArray(object, "following", sourceName);
            compareCode = TextureByMetadata.FOLLOWING;
        }

        if (compareCode == -1)
            throw new IconProcessorException(IconProcessorException.INVALID_COMPARABLE_CODE);


        final String texture = SwapDescriptor.obtainTexture(object);
        final int priority = SwapDescriptor.obtainPriority(object);

        final TextureByMetadata compiled = new TextureByMetadata(compareCode, compareInts, texture, priority);

        SwapDescriptor.obtainArmorAdditionally(compiled, object);

        return compiled;
    }
}
