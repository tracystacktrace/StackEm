package net.tracystacktrace.stackem.tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public final class JsonMapper {
    @FunctionalInterface
    public interface Transformer<T, R, E extends Exception> {
        R transform(T t) throws E, JsonExtractionException;
    }

    public static <T, E extends Exception> T @NotNull [] mapJsonArray(
            @NotNull JsonArray array,
            @NotNull JsonMapper.Transformer<@NotNull JsonObject, T, E> transformer,
            @NotNull Function<Integer, T @NotNull []> emptyArray
    ) throws E, JsonExtractionException {
        final Set<T> collector = new HashSet<>();
        for (JsonElement candidate : array) {
            if (candidate.isJsonObject()) {
                final T value = transformer.transform(candidate.getAsJsonObject());
                collector.add(value);
            }
        }
        return collector.toArray(emptyArray.apply(0));
    }

    public static <T, E extends Exception> T @NotNull [] mapJsonArraySafe(
            @NotNull JsonArray array,
            @NotNull Function<@NotNull JsonElement, T> transformer,
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
}
