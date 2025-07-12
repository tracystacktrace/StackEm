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

    public static int[] readIntArray(@NotNull JsonArray array) {
        final int[] result = new int[array.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = array.get(i).getAsInt();
        }
        return result;
    }

    public static <T, E extends Exception> T @Nullable [] readObjectArray(
            @NotNull JsonObject root,
            @NotNull String arrayName,
            @NotNull FunctionWithException<@NotNull JsonObject, @NotNull T, E> generator,
            T @NotNull [] empty
    ) throws E {
        if (!root.has(arrayName)) {
            return null;
        }

        final JsonElement element = root.get(arrayName);
        if (!element.isJsonArray()) {
            throw new IllegalStateException(String.format("A json array named %s is either corrupt or not an array!", arrayName));
        }

        final Set<T> collector = new HashSet<>();
        for (JsonElement candidate : element.getAsJsonArray()) {
            if (!candidate.isJsonObject()) {
                continue;
            }
            try {
                final T value = generator.apply(candidate.getAsJsonObject());
                collector.add(value);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException(String.format("An error occured while processing array %s", arrayName), e);
            }
        }

        return collector.isEmpty() ? null : collector.toArray(empty);
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

    public static @Nullable String extractString(@NotNull JsonObject object, @NotNull String name) {
        if (object.has(name)) {
            final JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                return element.getAsString();
            }
        }
        return null;
    }

}
