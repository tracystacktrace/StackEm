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
    public static int @NotNull [] readIntArray(@NotNull JsonArray array) throws IllegalStateException {
        final int[] result = new int[array.size()];
        for (int i = 0; i < result.length; i++) {
            final JsonElement element = array.get(i);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                result[i] = element.getAsInt();
            } else {
                throw new IllegalStateException(String.format("A presumably pure int[] json array is not pure! Failed at index: %s", i));
            }
        }
        return result;
    }

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

    public static @Nullable Integer readInteger(@NotNull JsonElement element) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsInt();
        }
        return null;
    }

    public static @Nullable Float readFloat(@NotNull JsonElement element) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsFloat();
        }
        return null;
    }

    public static @Nullable Boolean readBoolean(@NotNull JsonElement element) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
            return element.getAsBoolean();
        }
        return null;
    }
}
