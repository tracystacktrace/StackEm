package net.tracystacktrace.stackem.processor.iconswap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.tracystacktrace.stackem.processor.iconswap.swap.SwapDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class IconSwapReader {
    public static List<ItemIconSwap> fromJson(
            @NotNull String sourceName,
            @NotNull String input
    ) throws IconProcessorException {
        try {
            final JsonObject root = JsonParser.parseString(input).getAsJsonObject();
            final List<ItemIconSwap> collector = new ArrayList<>();

            if (root.has("data")) {
                final JsonElement dataElement = root.get("data");
                if (dataElement.isJsonArray()) {
                    for (JsonElement element : dataElement.getAsJsonArray()) {
                        if (element.isJsonObject()) {
                            try {
                                final ItemIconSwap candidate = ItemIconSwap.fromJson(element.getAsJsonObject());
                                collector.add(candidate);
                            } catch (IconProcessorException e) {
                                throw new IconProcessorException(IconProcessorException.INVALID_DATA_ELEMENT, element.toString(), e);
                            }
                        }
                    }
                } else {
                    throw new IconProcessorException(IconProcessorException.INVALID_DATA_OBJECT, dataElement.toString());
                }
            }

            return collector;
        } catch (JsonParseException e) {
            throw new IconProcessorException(IconProcessorException.FAILED_JSON_PARSE, sourceName, e);
        }
    }

    public static @NotNull String obtainTexture(@NotNull JsonObject object) throws IconProcessorException {
        String texture = null;
        if (object.has("texture")) {
            final JsonElement textureElement = object.get("texture");
            if (textureElement.isJsonPrimitive() && textureElement.getAsJsonPrimitive().isString()) {
                texture = textureElement.getAsString();
            } else {
                throw new IconProcessorException(IconProcessorException.INVALID_TEXTURE, textureElement.toString());
            }
        }

        if (texture == null) {
            throw new IconProcessorException(IconProcessorException.NOT_FOUND_TEXTURE);
        }

        return texture;
    }

    public static @Nullable Integer obtainPriority(@NotNull JsonObject object) throws IconProcessorException {
        if (object.has("priority")) {
            final JsonElement element = object.get("priority");
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                return element.getAsInt();
            } else {
                throw new IconProcessorException(IconProcessorException.INVALID_PRIORITY, element.toString());
            }
        }
        return null;
    }

    public static void obtainArmorIfPossible(@NotNull SwapDescriptor swapper, @NotNull JsonObject object) throws IconProcessorException {
        if (object.has("armorTexture")) {
            final JsonElement element = object.get("armorTexture");
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                swapper.setArmorTexture(element.getAsString());
            } else {
                throw new IconProcessorException(IconProcessorException.INVALID_ARMOR_TEXTURE, element.toString());
            }
        }
        if (object.has("armorEnableColor")) {
            final JsonElement element = object.get("armorEnableCoor");
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
                swapper.setArmorColor(element.getAsBoolean());
            } else {
                throw new IconProcessorException(IconProcessorException.INVALID_ENABLE_COLOR, element.toString());
            }
        }
    }
}
