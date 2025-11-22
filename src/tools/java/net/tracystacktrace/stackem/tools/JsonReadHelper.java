package net.tracystacktrace.stackem.tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public final class JsonReadHelper {
    public static <T, E extends Exception> T @NotNull [] transformArray(
            @NotNull JsonArray array,
            @NotNull FunctionWithException<@NotNull JsonObject, @NotNull T, E> transformer,
            @NotNull Function<Integer, T @NotNull []> emptyArray
    ) throws E {
        final Set<T> collector = new HashSet<>();
        for (JsonElement candidate : array) {
            if (candidate.isJsonObject()) {
                final T value = transformer.apply(candidate.getAsJsonObject());
                collector.add(value);
            }
        }
        return collector.toArray(emptyArray.apply(0));
    }

    public static <T> T @NotNull [] transformArraySafe(
            @NotNull JsonArray array,
            @NotNull Function<@NotNull JsonElement, @Nullable T> transformer,
            @NotNull Function<Integer, T @NotNull []> emptyArray
    ) {
        final Set<T> collector = new HashSet<>();
        for (JsonElement candidate : array) {
            final T value = transformer.apply(candidate);
            if (value != null) {
                collector.add(value);
            }
        }
        return collector.toArray(emptyArray.apply(0));
    }

    public static @Nullable String readString(@NotNull JsonElement element) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return element.getAsString();
        }
        return null;
    }
}
