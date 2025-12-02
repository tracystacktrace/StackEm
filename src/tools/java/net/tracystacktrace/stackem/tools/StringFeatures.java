package net.tracystacktrace.stackem.tools;

import org.jetbrains.annotations.Nullable;

public final class StringFeatures {
    public static @Nullable String limitString(@Nullable String line, final int length, final boolean endDots) {
        if (line == null || line.length() < length) {
            return line;
        }

        int maxLength = length;
        int colorCodeCount = (int) line.chars().limit(maxLength - 1).filter(c -> c == '§').count();

        maxLength += colorCodeCount * 2;

        String result = line.length() > maxLength ? line.substring(0, maxLength) : line;
        if (endDots) {
            result += "...";
        }

        return result;
    }
}
