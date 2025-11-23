package net.tracystacktrace.stackem.tools;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public final class IOReadHelper {
    public static class CustomIOException extends Exception {
        public static final byte ERROR_DURING_IS_FETCH = 0;
        public static final byte ERROR_DURING_IO_OPERATION = 1;

        public static @NotNull String getErrorCode(byte code, @Nullable String optional) {
            return switch (code) {
                case ERROR_DURING_IS_FETCH ->
                        String.format("Error during accessing an InputStream instance of %s", optional);
                case ERROR_DURING_IO_OPERATION -> String.format("Error during reading the file of %s", optional);

                default -> "Unknown error!";
            };
        }

        public CustomIOException(byte code, @Nullable String optional, @Nullable Exception e) {
            super(getErrorCode(code, optional), e);
        }
    }

    public static @NotNull String readTextFile(
            @NotNull String path,
            @NotNull FunctionWithException<@NotNull String, @NotNull InputStream, IOException> access
    ) throws CustomIOException {
        final InputStream inputStream;

        try {
            inputStream = access.apply(path);
        } catch (IOException e) {
            throw new CustomIOException(CustomIOException.ERROR_DURING_IS_FETCH, path, e);
        }

        String collected;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            collected = reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new CustomIOException(CustomIOException.ERROR_DURING_IO_OPERATION, path, e);
        }

        try {
            inputStream.close();
        } catch (IOException ignored) {
        }

        return collected;
    }
}
