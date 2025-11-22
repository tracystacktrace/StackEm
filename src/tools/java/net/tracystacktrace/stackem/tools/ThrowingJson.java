package net.tracystacktrace.stackem.tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;

//yeah, they accidentally threw out or something, maybe better to not drink that much
public final class ThrowingJson {

    public static void assertElementAsArray(
            @NotNull JsonObject object,
            @NotNull String target,
            @NotNull String sourceName
    ) throws JsonExtractionException {
        if(!object.has(target)) throw new JsonExtractionException(JsonExtractionException.ELEMENT_DOESNT_EXIST, sourceName, target);
        if(!object.get(target).isJsonArray()) throw new JsonExtractionException(JsonExtractionException.NOT_A_JSON_ARRAY, sourceName, target);
    }

    public static int cautiouslyGetInt(
            @NotNull JsonObject object,
            @NotNull String target,
            @NotNull String sourceName
    ) throws JsonExtractionException {
        if(!object.has(target)) throw new JsonExtractionException(JsonExtractionException.ELEMENT_DOESNT_EXIST, sourceName, target);
        
        final JsonElement element = object.get(target);
        if(!element.isJsonPrimitive()) throw new JsonExtractionException(JsonExtractionException.INVALID_INT, sourceName, element.toString());
        
        final JsonPrimitive primitive = element.getAsJsonPrimitive();
        if(!primitive.isNumber()) throw new JsonExtractionException(JsonExtractionException.INVALID_INT, sourceName, element.toString());
        
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
        if(!object.has(target)) throw new JsonExtractionException(JsonExtractionException.ELEMENT_DOESNT_EXIST, sourceName, target);

        final JsonElement element = object.get(target);
        if(!element.isJsonPrimitive()) throw new JsonExtractionException(JsonExtractionException.INVALID_STRING, sourceName, element.toString());

        final JsonPrimitive primitive = element.getAsJsonPrimitive();
        if(!primitive.isString()) throw new JsonExtractionException(JsonExtractionException.INVALID_STRING, sourceName, element.toString());

        return primitive.getAsString();
    }

    public static boolean cautiouslyGetBoolean(
            @NotNull JsonObject object,
            @NotNull String target,
            @NotNull String sourceName
    ) throws JsonExtractionException {
        if(!object.has(target)) throw new JsonExtractionException(JsonExtractionException.ELEMENT_DOESNT_EXIST, sourceName, target);

        final JsonElement element = object.get(target);
        if(!element.isJsonPrimitive()) throw new JsonExtractionException(JsonExtractionException.INVALID_BOOLEAN, sourceName, element.toString());

        final JsonPrimitive primitive = element.getAsJsonPrimitive();
        if(!primitive.isBoolean()) throw new JsonExtractionException(JsonExtractionException.INVALID_BOOLEAN, sourceName, element.toString());

        return primitive.getAsBoolean();
    }

}
