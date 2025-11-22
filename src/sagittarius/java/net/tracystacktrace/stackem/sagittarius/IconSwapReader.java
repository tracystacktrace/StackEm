package net.tracystacktrace.stackem.sagittarius;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.tracystacktrace.stackem.tools.json.JsonExtractionException;
import net.tracystacktrace.stackem.tools.json.JsonMapper;
import net.tracystacktrace.stackem.tools.json.ThrowingJson;
import org.jetbrains.annotations.NotNull;

public final class IconSwapReader {
    public static @NotNull ItemIconSwap @NotNull [] fromJson(
            @NotNull final String sourceName,
            @NotNull final String input
    ) throws IconProcessorException, JsonExtractionException {
        final JsonObject object = ThrowingJson.stringToJsonObject(input, sourceName);
        final JsonArray arrayData = ThrowingJson.cautiouslyGetArray(object, "data", sourceName);
        return JsonMapper.mapJsonArrayWithException(arrayData, a -> ItemIconSwap.fromJson(a, sourceName), ItemIconSwap[]::new);
    }
}
