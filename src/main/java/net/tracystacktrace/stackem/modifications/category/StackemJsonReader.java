package net.tracystacktrace.stackem.modifications.category;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.tracystacktrace.stackem.impl.TagTexturePack;
import net.tracystacktrace.stackem.tools.JsonExtractionException;
import net.tracystacktrace.stackem.tools.JsonMapper;
import net.tracystacktrace.stackem.tools.ThrowingJson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StackemJsonReader {
    public static void pushJson(
            @NotNull JsonObject object,
            @NotNull TagTexturePack texturePack
    ) {
        if (object.has("author")) {
            try {
                texturePack.setAuthor(ThrowingJson.cautiouslyGetString(object, "author", texturePack.name));
            }catch (JsonExtractionException e) {
                e.printStackTrace();
            }
        }

        if (object.has("website")) {
            try {
                texturePack.setWebsite(ThrowingJson.cautiouslyGetString(object, "website", texturePack.name));
            }catch (JsonExtractionException e) {
                e.printStackTrace();
            }
        }

        if (object.has("category")) {
            try {
                final JsonObject category = ThrowingJson.cautiouslyGetObject(object, "category", texturePack.name);

                //in-built
                if (category.has("id")) {
                    final JsonArray idArray = ThrowingJson.cautiouslyGetArray(object, "id", texturePack.name);
                    final EnumCategory[] collected = JsonMapper.mapJsonArray(
                            idArray, element -> EnumCategory.define(readString(element)),
                            EnumCategory[]::new
                    );
                    if (collected.length > 0) {
                        texturePack.setCategories(collected);
                    }
                }

                //custom
                if (category.has("custom")) {
                    final JsonArray arrayCustom = ThrowingJson.cautiouslyGetArray(object, "custom", texturePack.name);
                    final String[] collected = JsonMapper.mapJsonArray(
                            arrayCustom, element -> {
                                final String collected1 = readString(element);
                                return (collected1 != null && collected1.isEmpty()) ? null : collected1;
                            },
                            String[]::new
                    );
                    if (collected.length > 0) {
                        texturePack.setCustomCategories(collected);
                    }
                }
            } catch (JsonExtractionException e) {
                e.printStackTrace();
            }
        }
    }

    private static @Nullable String readString(@NotNull JsonElement element) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return element.getAsString();
        }
        return null;
    }
}
