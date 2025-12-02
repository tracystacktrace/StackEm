package net.tracystacktrace.stackem.tools;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Properties;
import java.util.regex.Pattern;

public class NeptuneProperties {
    protected static final Pattern PATTERN_SINGLE_WORD = Pattern.compile("^\\w+$");
    protected final Properties properties = new Properties();

    public NeptuneProperties() {
    }

    public void open(@NotNull InputStream stream) throws IOException {
        properties.load(stream);
    }

    public void save(@NotNull OutputStream stream, @Nullable String commentary) throws IOException {
        properties.store(stream, String.format("NeptuneProperties by tracystacktrace, a quick and dirty properties expansion\n%s", commentary != null ? commentary : "-"));
    }

    public void open(@NotNull File file) throws IOException {
        final FileInputStream stream = new FileInputStream(file);
        this.open(stream);
        stream.close();
    }

    public void save(@NotNull File file, @Nullable String commentary) throws IOException {
        final FileOutputStream stream = new FileOutputStream(file);
        this.save(stream, commentary);
        stream.close();
    }

    public void setString(@NotNull String key, @Nullable String value) {
        this.properties.setProperty(key, value);
    }

    public @Nullable String getString(@NotNull String key) {
        final String candidate = this.properties.getProperty(key);
        if (candidate == null || "null".equals(candidate)) {
            return null;
        }
        return candidate;
    }

    public void setStringArray(@NotNull String key, @Nullable String @Nullable [] value) {
        if (value == null) {
            this.properties.setProperty(key, null);
            return;
        }

        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length; i++) {
            final String candidate = value[i];

            if (candidate == null || "null".equals(candidate)) {
                builder.append("null");
            } else if (this.isSingleWord(candidate)) {
                builder.append(candidate.trim());
            } else {
                builder.append("\"").append(candidate).append("\"");
            }

            if (i + 1 < value.length) builder.append(';');
        }

        this.properties.setProperty(key, builder.toString());
    }

    public @Nullable String @Nullable [] getStringArray(@NotNull String key) {
        final String parseString = this.properties.getProperty(key);
        if (parseString == null || "null".equals(parseString)) {
            return null;
        }

        final String[] candidates = parseString.split(";");
        final String[] resultData = new String[candidates.length];

        for (int i = 0; i < candidates.length; i++) {
            final String c0 = candidates[i];
            if (c0 == null || "null".equals(c0)) {
                resultData[i] = null;
                continue;
            }

            //trim or remove brackets
            if (c0.startsWith("\"") && c0.endsWith("\"")) {
                resultData[i] = c0.substring(1, c0.length() - 1);
            } else {
                resultData[i] = c0.trim();
            }
        }

        return resultData;
    }

    private boolean isSingleWord(@NotNull String s) {
        return PATTERN_SINGLE_WORD.matcher(s).matches();
    }
}
