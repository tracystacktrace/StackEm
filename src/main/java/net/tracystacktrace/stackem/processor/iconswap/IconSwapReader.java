package net.tracystacktrace.stackem.processor.iconswap;

import com.google.gson.*;
import net.minecraft.common.item.Items;
import net.tracystacktrace.stackem.processor.iconswap.swap.ISwapper;
import net.tracystacktrace.stackem.processor.iconswap.swap.TextureByMetadata;
import net.tracystacktrace.stackem.processor.iconswap.swap.TextureByName;
import net.tracystacktrace.stackem.tools.JsonReadHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class IconSwapReader {
    public static List<ItemIconSwap> fromJson(
            @NotNull String sourceName,
            @NotNull String input
    ) throws IconProcessorException {
        try {
            final JsonObject root = JsonParser.parseString(input).getAsJsonObject();
            final List<ItemIconSwap> collector = new ArrayList<>();

            if (root.has("data")) {
                final JsonElement dataElement = root.get("data");
                if (dataElement.isJsonArray()) {
                    for (JsonElement element : dataElement.getAsJsonArray()) {
                        if (element.isJsonObject()) {
                            try {
                                final ItemIconSwap candidate = IconSwapReader.processItem(element.getAsJsonObject());
                                collector.add(candidate);
                            } catch (IconProcessorException e) {
                                throw new IconProcessorException(IconProcessorException.INVALID_DATA_ELEMENT, element.toString(), e);
                            }
                        }
                    }
                } else {
                    throw new IconProcessorException(IconProcessorException.INVALID_DATA_OBJECT, dataElement.toString());
                }
            }

            return collector;
        } catch (JsonParseException e) {
            throw new IconProcessorException(IconProcessorException.FAILED_JSON_PARSE, sourceName, e);
        }
    }

    private static @NotNull ItemIconSwap processItem(@NotNull JsonObject object) throws IconProcessorException {
        int targetItemID = -1;

        if (!object.has("item")) {
            throw new IconProcessorException(IconProcessorException.NOT_FOUND_ITEM_ID);
        }

        //item processor
        final JsonElement itemElement = object.get("item");
        if (itemElement.isJsonPrimitive()) {
            final JsonPrimitive itemPrimitive = itemElement.getAsJsonPrimitive();
            if (itemPrimitive.isNumber()) {
                final int item_iid = itemPrimitive.getAsInt();
                if (isValidItemID(item_iid)) {
                    targetItemID = item_iid;
                }
            } else if (itemPrimitive.isString()) {
                final String item_iid = itemPrimitive.getAsString();
                targetItemID = getItemIDByName(item_iid);
            }
        }

        if (targetItemID == -1) {
            throw new IconProcessorException(IconProcessorException.INVALID_ITEM_ID, String.valueOf(targetItemID));
        }

        //swapper processors
        TextureByName[] textureByNames = null;
        TextureByMetadata[] textureByMetadata = null;

        if (object.has("onName")) {
            final JsonElement onNameElement = object.get("onName");
            if (onNameElement.isJsonArray()) {
                try {
                    textureByNames = JsonReadHelper.transformArray(
                            onNameElement.getAsJsonArray(),
                            TextureByName::fromJson,
                            TextureByName[]::new
                    );
                } catch (IconProcessorException e) {
                    throw new IconProcessorException(IconProcessorException.ON_NAME_PROCESS_FAILED, onNameElement.toString(), e);
                }
            }
        }

        if (object.has("onMeta")) {
            final JsonElement onMetaElement = object.get("onMeta");
            if (onMetaElement.isJsonArray()) {
                try {
                    textureByMetadata = JsonReadHelper.transformArray(
                            onMetaElement.getAsJsonArray(),
                            TextureByMetadata::fromJson,
                            TextureByMetadata[]::new
                    );
                } catch (IconProcessorException e) {
                    throw new IconProcessorException(IconProcessorException.ON_META_PROCESS_FAILED, onMetaElement.toString(), e);
                }
            }
        }

        if (textureByNames != null) {
            Arrays.sort(textureByNames, Comparator.comparingInt(TextureByName::getPriority));
        }

        if (textureByMetadata != null) {
            Arrays.sort(textureByMetadata, Comparator.comparingInt(TextureByMetadata::getPriority));
        }

        return new ItemIconSwap(targetItemID, textureByNames, textureByMetadata);
    }

    public static @NotNull String obtainTexture(@NotNull JsonObject object) throws IconProcessorException {
        String texture = null;
        if (object.has("texture")) {
            final JsonElement textureElement = object.get("texture");
            if (textureElement.isJsonPrimitive() && textureElement.getAsJsonPrimitive().isString()) {
                texture = textureElement.getAsString();
            } else {
                throw new IconProcessorException(IconProcessorException.INVALID_TEXTURE, textureElement.toString());
            }
        }

        if (texture == null) {
            throw new IconProcessorException(IconProcessorException.NOT_FOUND_TEXTURE);
        }

        return texture;
    }

    public static @Nullable Integer obtainPriority(@NotNull JsonObject object) throws IconProcessorException {
        if (object.has("priority")) {
            final JsonElement element = object.get("priority");
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                return element.getAsInt();
            } else {
                throw new IconProcessorException(IconProcessorException.INVALID_PRIORITY, element.toString());
            }
        }
        return null;
    }

    public static void obtainArmorIfPossible(@NotNull ISwapper swapper, @NotNull JsonObject object) throws IconProcessorException {
        if (object.has("armorTexture")) {
            final JsonElement element = object.get("armorTexture");
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                swapper.setArmorTexture(element.getAsString());
            } else {
                throw new IconProcessorException(IconProcessorException.INVALID_ARMOR_TEXTURE, element.toString());
            }
        }
        if (object.has("armorEnableColor")) {
            final JsonElement element = object.get("armorEnableCoor");
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
                swapper.setEnableColor(element.getAsBoolean());
            } else {
                throw new IconProcessorException(IconProcessorException.INVALID_ENABLE_COLOR, element.toString());
            }
        }
    }

    private static boolean isValidItemID(int i) {
        if (i < 0 || i >= Items.ITEMS_LIST.length) {
            return false;
        }

        try {
            return Items.ITEMS_LIST[i] != null;
        } catch (Exception e) {
            return false;
        }
    }

    private static int getItemIDByName(@Nullable String candidate) {
        if (candidate == null || candidate.isEmpty()) {
            return -1;
        }

        return Arrays.stream(Items.ITEMS_LIST)
                .filter(Objects::nonNull)
                .filter(i -> i.getItemName().equals(candidate))
                .map(i -> i.itemID)
                .findFirst()
                .orElse(-1);
    }
}
