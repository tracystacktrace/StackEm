package net.tracystacktrace.stackem.sagittarius;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IconProcessorException extends Exception {
    public static final byte NOT_FOUND_ITEM_ID = 2;
    public static final byte INVALID_ITEM_ID = 3;
    public static final byte INVALID_COMPARABLE_CODE = 4;
    public static final byte ON_NAME_PROCESS_FAILED = 5;
    public static final byte ON_META_PROCESS_FAILED = 6;

    public static @NotNull String getErrorCode(byte code, @Nullable String optional) {
        return switch (code) {
            case NOT_FOUND_ITEM_ID -> "Expected an item identifier, but wasn't found!";
            case INVALID_ITEM_ID -> String.format("Invalid \"item\" value: %s", optional);
            case INVALID_COMPARABLE_CODE ->
                    optional != null ? String.format("Invalid comparable code: %s", optional) : "Invalid comparable code!";
            case ON_NAME_PROCESS_FAILED -> String.format("Failed to process \"onName\" array: %s", optional);
            case ON_META_PROCESS_FAILED -> String.format("Failed to process \"onMeta\" array: %s", optional);

            default -> "Unknown error!";
        };
    }

    public IconProcessorException(byte code, @Nullable String optional, @Nullable Exception e) {
        super(getErrorCode(code, optional), e);
    }

    public IconProcessorException(byte code, @Nullable String optional) {
        this(code, optional, null);
    }

    public IconProcessorException(byte code) {
        this(code, null, null);
    }
}
