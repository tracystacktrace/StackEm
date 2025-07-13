package net.tracystacktrace.stackem.processor.iconswap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.tracystacktrace.stackem.tools.JsonReadHelper;
import org.jetbrains.annotations.NotNull;

public final class IconSwapReader {
    public static ItemIconSwap @NotNull [] fromJson(
            @NotNull String sourceName,
            @NotNull String input
    ) throws IconProcessorException {
        try {
            final JsonObject root = JsonParser.parseString(input).getAsJsonObject();

            if (root.has("data")) {
                final JsonElement dataElement = root.get("data");
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

            throw new IconProcessorException(IconProcessorException.INVALID_DATA_OBJECT, root.toString());
        } catch (JsonParseException e) {
            throw new IconProcessorException(IconProcessorException.FAILED_JSON_PARSE, sourceName, e);
        }
    }
}
