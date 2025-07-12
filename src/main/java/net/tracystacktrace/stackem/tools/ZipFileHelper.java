package net.tracystacktrace.stackem.tools;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class ZipFileHelper {

    public static class CustomZipOperationException extends Exception {
        public CustomZipOperationException(@NotNull String info, @NotNull IOException e) {
            super(info, e);
        }
    }

    public static @Nullable ZipEntry getEntryFor(
            @NotNull ZipFile file,
            @Nullable String string
    ) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        if (string.startsWith("/")) {
            string = string.substring(1);
        }

        return file.getEntry(string);
    }

    public static @NotNull String readTextFile(
            @NotNull ZipFile file,
            @NotNull ZipEntry entry
    ) throws CustomZipOperationException {
        try (InputStream inputStream = file.getInputStream(entry);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new CustomZipOperationException("Couldn't read file: " + entry.getName(), e);
        }
    }

    public static @Nullable String readTextFile(
            @NotNull ZipFile file,
            @NotNull String location
    ) {
        if (location.startsWith("/")) {
            location = location.substring(1);
        }

        final ZipEntry entry = file.getEntry(location);
        if (entry == null) {
            return null;
        }
        try {
            return readTextFile(file, entry);
        } catch (CustomZipOperationException e) {
            return null;
        }
    }

    public static <T> @Nullable T readTextFile(
            @NotNull ZipFile file,
            @NotNull String location,
            @NotNull FunctionWithException<BufferedReader, T, IOException> generator
    ) {
        if (location.startsWith("/")) {
            location = location.substring(1);
        }

        final ZipEntry entry = file.getEntry(location);
        if (entry == null) {
            return null;
        }

        try (InputStream inputStream = file.getInputStream(entry);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return generator.apply(reader);
        } catch (IOException e) {
            return null;
        }
    }

    public static @Nullable BufferedImage readImage(
            @NotNull ZipFile file,
            @NotNull String location
    ) {
        if (location.startsWith("/")) {
            location = location.substring(1);
        }

        final ZipEntry entry = file.getEntry(location);
        if (entry == null) {
            return null;
        }

        try (InputStream inputStream = file.getInputStream(entry)) {
            return ImageIO.read(inputStream);
        } catch (IOException ignored) {
            return null;
        }
    }
}
