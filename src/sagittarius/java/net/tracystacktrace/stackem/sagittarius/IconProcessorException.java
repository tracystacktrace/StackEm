package net.tracystacktrace.stackem.sagittarius;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IconProcessorException extends Exception {
    public static final byte INVALID_DATA_OBJECT = 0;
    public static final byte FAILED_JSON_PARSE = 1;
    public static final byte NOT_FOUND_ITEM_ID = 2;
    public static final byte INVALID_ITEM_ID = 3;
    public static final byte INVALID_COMPARABLE_CODE = 4;
    public static final byte ON_NAME_PROCESS_FAILED = 5;
    public static final byte ON_META_PROCESS_FAILED = 6;
    public static final byte INVALID_TEXTURE = 7;
    public static final byte NOT_FOUND_TEXTURE = 8;
    public static final byte INVALID_PRIORITY = 9;
    public static final byte INVALID_DATA_ELEMENT = 10;
    public static final byte INVALID_ARMOR_TEXTURE = 11;
    public static final byte INVALID_ENABLE_COLOR = 12;


    public static @NotNull String getErrorCode(byte code, @Nullable String optional) {
        return switch (code) {
            case INVALID_DATA_OBJECT -> String.format("Invalid \"data\" field in \"stackem.items.json\": %s", optional);
            case FAILED_JSON_PARSE -> String.format("Failed to process JSON raw data: %s", optional);
            case NOT_FOUND_ITEM_ID -> "Expected an item identifier, but wasn't found!";
            case INVALID_ITEM_ID -> String.format("Invalid \"item\" value: %s", optional);
            case INVALID_COMPARABLE_CODE ->
                    optional != null ? String.format("Invalid comparable code: %s", optional) : "Invalid comparable code!";
            case ON_NAME_PROCESS_FAILED -> String.format("Failed to process \"onName\" array: %s", optional);
            case ON_META_PROCESS_FAILED -> String.format("Failed to process \"onMeta\" array: %s", optional);
            case INVALID_TEXTURE -> String.format("Invalid texture path: %s", optional);
            case NOT_FOUND_TEXTURE -> "Expected a texture, but wasn't found!";
            case INVALID_PRIORITY -> String.format("Invalid \"priority\" value: %s", optional);
            case INVALID_DATA_ELEMENT -> String.format("Invalid \"data\" element: %s", optional);
            case INVALID_ARMOR_TEXTURE -> String.format("Invalid \"armorTexture\" value: %s", optional);
            case INVALID_ENABLE_COLOR -> String.format("Invalid \"armorEnableColor\" value: %s", optional);

            default -> "Unknown error!";
        };
    }

    public IconProcessorException(byte code, @Nullable String optional, @Nullable Exception e) {
        super(getErrorCode(code, optional), e);
    }

    public IconProcessorException(byte code, @Nullable String optional) {
        this(code, optional, null);
    }

    public IconProcessorException(byte code, @Nullable Exception e) {
        this(code, null, e);
    }

    public IconProcessorException(byte code) {
        this(code, null, null);
    }
}
