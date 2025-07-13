package net.tracystacktrace.stackem.processor.iconswap.swap;

import com.google.gson.JsonObject;
import net.tracystacktrace.stackem.processor.iconswap.IconProcessorException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextureByName extends SwapDescriptor {
    public static final byte EQUALS = 0;
    public static final byte EQUALS_IGNORE_CASE = 1;
    public static final byte CONTAINS = 2;
    public static final byte REGEX = 3; //todo: add regex support
    public static final byte STARTS_WITH = 4;
    public static final byte ENDS_WITH = 5;

    public final byte compareCode;
    public final String targetString;

    public TextureByName(
            byte compareCode,
            @NotNull String targetString,
            @NotNull String texturePath,
            int priority
    ) {
        super(texturePath, priority);
        this.compareCode = compareCode;
        this.targetString = targetString;
    }

    public boolean compareString(@Nullable String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            return false;
        }

        switch (this.compareCode) {
            case EQUALS -> {
                return itemName.equals(this.targetString);
            }
            case EQUALS_IGNORE_CASE -> {
                return itemName.equalsIgnoreCase(this.targetString);
            }
            case CONTAINS -> {
                return itemName.contains(this.targetString);
            }
            case STARTS_WITH -> {
                return itemName.startsWith(this.targetString);
            }
            case ENDS_WITH -> {
                return itemName.endsWith(this.targetString);
            }
        }

        return false;
    }

    public static @NotNull TextureByName fromJson(@NotNull JsonObject object) throws IconProcessorException {
        byte compareCode = -1;
        String targetString = null;

        if (object.has("equals")) {
            targetString = object.get("equals").getAsString();
            compareCode = TextureByName.EQUALS;
        }

        if (object.has("equalsIgnoreCase")) {
            targetString = object.get("equalsIgnoreCase").getAsString();
            compareCode = TextureByName.EQUALS_IGNORE_CASE;
        }

        if (object.has("contains")) {
            targetString = object.get("contains").getAsString();
            compareCode = TextureByName.CONTAINS;
        }

        if (object.has("startsWith")) {
            targetString = object.get("startsWith").getAsString();
            compareCode = TextureByName.STARTS_WITH;
        }

        if (object.has("endsWith")) {
            targetString = object.get("endsWith").getAsString();
            compareCode = TextureByName.ENDS_WITH;
        }

        if (compareCode == -1 || targetString == null) {
            throw new IconProcessorException(IconProcessorException.INVALID_COMPARABLE_CODE);
        }

        //obtain texture path
        final String texture = SwapDescriptor.obtainTexture(object);
        final int priority = SwapDescriptor.obtainPriority(object);

        final TextureByName compiled = new TextureByName(compareCode, targetString, texture, priority);

        SwapDescriptor.obtainArmorAdditionally(compiled, object);

        return compiled;
    }
}
