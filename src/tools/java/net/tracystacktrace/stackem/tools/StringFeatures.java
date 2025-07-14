package net.tracystacktrace.stackem.tools;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class StringFeatures {
    public static String limitString(String line, final int length, final boolean endDots) {
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

    public static String[] provideCategoryCombinations(List<String> input, int maxLength) {
        final List<String> strings = new ArrayList<>(input);
        strings.sort(Comparator.comparingInt(String::length));

        List<String> result = new ArrayList<>();
        List<String> currentGroup = new ArrayList<>();
        int currentGroupLength = 0;

        for (String str : strings) {
            if (str.length() >= maxLength) {
                if (!currentGroup.isEmpty()) {
                    result.add(String.join(" §r§7|§r ", currentGroup));
                    currentGroup.clear();
                    currentGroupLength = 0;
                }
                result.add(truncateString(str, maxLength));
                continue;
            }

            int potentialLength = currentGroupLength + str.length();
            if (!currentGroup.isEmpty()) {
                potentialLength += 3 * currentGroup.size();
            }

            if (potentialLength > maxLength) {
                if (!currentGroup.isEmpty()) {
                    result.add(String.join(" §r§7|§r ", currentGroup));
                }
                currentGroup.clear();
                currentGroup.add(str);
                currentGroupLength = str.length();
            } else {
                currentGroup.add(str);
                currentGroupLength += str.length();
            }
        }

        if (!currentGroup.isEmpty()) {
            result.add(String.join(" §r§7|§r ", currentGroup));
        }

        return result.toArray(new String[0]);
    }

    private static @NotNull String truncateString(@NotNull String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        if (maxLength <= 3) {
            return "...".substring(0, maxLength);
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}
