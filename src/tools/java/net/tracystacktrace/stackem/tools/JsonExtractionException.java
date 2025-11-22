package net.tracystacktrace.stackem.tools;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JsonExtractionException extends Exception {
    public static final byte ELEMENT_DOESNT_EXIST = -128;
    public static final byte NOT_A_JSON_ARRAY = -127;

    public static final byte INVALID_INPUT_DATA = -126;
    public static final byte INVALID_INT = -125;
    public static final byte INVALID_STRING = -124;
    public static final byte INVALID_BOOLEAN = -123;

    public static @NotNull String factoryMessage(byte reason, @Nullable String optional, @NotNull String source) {
        return switch (reason) {
            //evaluation/assertion error messages
            case ELEMENT_DOESNT_EXIST -> String.format("Fatal! Source %s, element \"%s\" is not found, but required!", source, optional);
            case NOT_A_JSON_ARRAY -> String.format("Fatal! Source %s, element \"%s\" is not a JSON array!", source, optional);

            //parsing error messages
            case INVALID_INPUT_DATA -> String.format("Source %s, invalid JSON Object at: %s", source, optional == null ? "input empty or null" : optional);
            case INVALID_INT -> String.format("Source %s, invalid INT value at: %s", source, optional);
            case INVALID_STRING -> String.format("Source %s, invalid STRING value at: %s", source, optional);
            case INVALID_BOOLEAN -> String.format("Source %s, invalid BOOLEAN value at: %s", source, optional);

            default -> String.format("Unknown error code: %d", reason);
        };
    }

    public JsonExtractionException(byte reason, @NotNull String source, @Nullable String optional) {
        super(factoryMessage(reason, optional, source));
    }

    public JsonExtractionException(byte reason, @NotNull String source) {
        super(factoryMessage(reason, null, source));
    }

    public JsonExtractionException(byte reason, @NotNull String source, @NotNull Exception exception) {
        super(factoryMessage(reason, null, source), exception);
    }

    public JsonExtractionException(byte reason, @NotNull String source, @Nullable String optional, @NotNull Exception exception) {
        super(factoryMessage(reason, optional, source), exception);
    }
}
