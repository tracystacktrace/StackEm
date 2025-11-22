package net.tracystacktrace.stackem.sagittarius;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.tracystacktrace.stackem.tools.JsonExtractionException;
import net.tracystacktrace.stackem.tools.JsonReadHelper;
import net.tracystacktrace.stackem.tools.ThrowingJson;
import org.jetbrains.annotations.NotNull;

public final class IconSwapReader {
    public static @NotNull ItemIconSwap @NotNull [] fromJson(
            @NotNull final String sourceName,
            @NotNull final String input
    ) throws IconProcessorException, JsonExtractionException {
        final JsonObject object = ThrowingJson.stringToJsonObject(input, sourceName);
        final JsonArray arrayData = ThrowingJson.cautiouslyGetArray(object, "data", sourceName);

        return JsonReadHelper.transformArray(arrayData, a -> {
            try {
                return ItemIconSwap.fromJson(a, sourceName);
            } catch (IconProcessorException | JsonExtractionException e) {
                throw new IconProcessorException(IconProcessorException.INVALID_DATA_ELEMENT, a.toString(), e);
            }
        }, ItemIconSwap[]::new);
    }
}
