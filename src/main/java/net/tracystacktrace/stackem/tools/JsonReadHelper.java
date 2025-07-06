package net.tracystacktrace.stackem.tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.tracystacktrace.stackem.StackEm;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public final class JsonReadHelper {

    public static int[] readIntArray(@NotNull JsonArray array) {
        final int[] result = new int[array.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = array.get(i).getAsInt();
        }
        return result;
    }

    public static <T> T[] readObjectArray(JsonObject root, String arrayName, Function<JsonObject, T> generator, T[] empty) {
        if (arrayName == null || arrayName.isEmpty() || !root.has(arrayName)) {
            return null;
        }
        final Set<T> collector = new HashSet<>();

        for (JsonElement element : root.get(arrayName).getAsJsonArray()) {
            if (!element.isJsonObject()) {
                continue;
            }
            try {
                final T value = generator.apply(element.getAsJsonObject());
                collector.add(value);
            } catch (IllegalArgumentException e) {
                StackEm.LOGGER.severe("Failed to process item [" + arrayName + "] swap data!");
                e.printStackTrace();
                StackEm.LOGGER.throwing("ItemStackCooker", "read", e);
            }
        }

        return collector.isEmpty() ? null : collector.toArray(empty);
    }

}
