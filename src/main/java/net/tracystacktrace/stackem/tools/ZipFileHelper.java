package net.tracystacktrace.stackem.tools;

import net.tracystacktrace.stackem.StackEm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileHelper {

    public static class CustomZipOperationException extends Exception {
        public CustomZipOperationException(@NotNull String info, @NotNull IOException e) {
            super(info, e);
        }
    }

    public static @Nullable ZipEntry getEntryFor(@NotNull ZipFile file, @Nullable String string) {
        if(string == null || string.isEmpty()) {
            return null;
        }
        return file.getEntry(string);
    }

    public static @NotNull String readTextFile(@NotNull ZipFile file, @NotNull ZipEntry entry) throws CustomZipOperationException {
        try (InputStream inputStream = file.getInputStream(entry);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new CustomZipOperationException("Couldn't read file: " + entry.getName(), e);
        }
    }

}
