package net.tracystacktrace.stackem.sagittarius;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.tracystacktrace.stackem.tools.IOReadHelper;
import net.tracystacktrace.stackem.tools.JsonReadHelper;
import org.jetbrains.annotations.NotNull;

public final class IconSwapReader {
    public static @NotNull ItemIconSwap @NotNull [] fromJson(
            @NotNull String sourceName,
            @NotNull String input
    ) throws IconProcessorException {
        JsonObject object;

        try {
            object = IOReadHelper.processJson(input);
        } catch (IOReadHelper.CustomIOException e) {
            throw new IconProcessorException(IconProcessorException.FAILED_JSON_PARSE, sourceName, e);
        }

        if (object.has("data")) {
            final JsonElement dataElement = object.get("data");
            if (dataElement.isJsonArray()) {
                return JsonReadHelper.transformArray(dataElement.getAsJsonArray(), a -> {
                    try {
                        return ItemIconSwap.fromJson(a);
                    } catch (IconProcessorException e) {
                        throw new IconProcessorException(IconProcessorException.INVALID_DATA_ELEMENT, a.toString(), e);
                    }
                }, ItemIconSwap[]::new);
            }
        }

        throw new IconProcessorException(IconProcessorException.INVALID_DATA_OBJECT, object.toString());
    }
}
