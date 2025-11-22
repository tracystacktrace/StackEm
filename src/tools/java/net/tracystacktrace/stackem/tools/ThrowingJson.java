package net.tracystacktrace.stackem.tools;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//yeah, they accidentally threw out or something, maybe better to not drink that much
public final class ThrowingJson {

    public static @NotNull JsonObject stringToJsonObject(
            @Nullable String data,
            @NotNull String sourceName
    ) throws JsonExtractionException {
        if (data == null || data.isEmpty())
            throw new JsonExtractionException(JsonExtractionException.INVALID_INPUT_DATA, sourceName);

        try {
            return JsonParser.parseString(data).getAsJsonObject();
        } catch (JsonParseException e) {
            throw new JsonExtractionException(JsonExtractionException.INVALID_INPUT_DATA, sourceName, data);
        }
    }

    /* Checking methods */

    public static boolean isInteger(
            @NotNull JsonObject object,
            @NotNull String target
    ) {
        if (!object.has(target))
            return false;

        final JsonElement element = object.get(target);
        if (!element.isJsonPrimitive())
            return false;

        final JsonPrimitive primitive = element.getAsJsonPrimitive();
        return primitive.isNumber();
    }

    public static boolean isString(
            @NotNull JsonObject object,
            @NotNull String target
    ) {
        if (!object.has(target))
            return false;

        final JsonElement element = object.get(target);
        if (!element.isJsonPrimitive())
            return false;

        final JsonPrimitive primitive = element.getAsJsonPrimitive();
        return primitive.isString();
    }

    /* Extraction methods */

    public static @NotNull JsonArray cautiouslyGetArray(
            @NotNull JsonObject object,
            @NotNull String target,
            @NotNull String sourceName
    ) throws JsonExtractionException {
        if (!object.has(target))
            throw new JsonExtractionException(JsonExtractionException.ELEMENT_DOESNT_EXIST, sourceName, target);

        final JsonElement element = object.get(target);
        if (!element.isJsonArray())
            throw new JsonExtractionException(JsonExtractionException.NOT_A_JSON_ARRAY, sourceName, target);

        return element.getAsJsonArray();
    }

    public static int cautiouslyGetInt(
            @NotNull JsonObject object,
            @NotNull String target,
            @NotNull String sourceName
    ) throws JsonExtractionException {
        if (!object.has(target))
            throw new JsonExtractionException(JsonExtractionException.ELEMENT_DOESNT_EXIST, sourceName, target);

        final JsonElement element = object.get(target);
        if (!element.isJsonPrimitive())
            throw new JsonExtractionException(JsonExtractionException.INVALID_INT, sourceName, element.toString());

        final JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (!primitive.isNumber())
            throw new JsonExtractionException(JsonExtractionException.INVALID_INT, sourceName, element.toString());

        try {
            return primitive.getAsInt();
        } catch (NumberFormatException e) {
            throw new JsonExtractionException(JsonExtractionException.INVALID_INT, sourceName, element.toString(), e);
        }
    }

    public static @NotNull String cautiouslyGetString(
            @NotNull JsonObject object,
            @NotNull String target,
            @NotNull String sourceName
    ) throws JsonExtractionException {
        if (!object.has(target))
            throw new JsonExtractionException(JsonExtractionException.ELEMENT_DOESNT_EXIST, sourceName, target);

        final JsonElement element = object.get(target);
        if (!element.isJsonPrimitive())
            throw new JsonExtractionException(JsonExtractionException.INVALID_STRING, sourceName, element.toString());

        final JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (!primitive.isString())
            throw new JsonExtractionException(JsonExtractionException.INVALID_STRING, sourceName, element.toString());

        return primitive.getAsString();
    }

    public static boolean cautiouslyGetBoolean(
            @NotNull JsonObject object,
            @NotNull String target,
            @NotNull String sourceName
    ) throws JsonExtractionException {
        if (!object.has(target))
            throw new JsonExtractionException(JsonExtractionException.ELEMENT_DOESNT_EXIST, sourceName, target);

        final JsonElement element = object.get(target);
        if (!element.isJsonPrimitive())
            throw new JsonExtractionException(JsonExtractionException.INVALID_BOOLEAN, sourceName, element.toString());

        final JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (!primitive.isBoolean())
            throw new JsonExtractionException(JsonExtractionException.INVALID_BOOLEAN, sourceName, element.toString());

        return primitive.getAsBoolean();
    }

}
