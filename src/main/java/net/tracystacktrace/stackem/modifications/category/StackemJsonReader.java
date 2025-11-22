package net.tracystacktrace.stackem.modifications.category;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.tracystacktrace.stackem.impl.TagTexturePack;
import net.tracystacktrace.stackem.tools.JsonMapper;
import net.tracystacktrace.stackem.tools.JsonReadHelper;
import org.jetbrains.annotations.NotNull;

public final class StackemJsonReader {
    public static void pushJson(
            @NotNull JsonObject object,
            @NotNull TagTexturePack texturePack
    ) {
        if (object.has("author")) {
            texturePack.setAuthor(JsonReadHelper.readString(object.get("author")));
        }

        if (object.has("website")) {
            texturePack.setWebsite(JsonReadHelper.readString(object.get("website")));
        }

        if (object.has("category")) {
            final JsonElement elementCategory = object.get("category");

            if (elementCategory.isJsonObject()) {
                final JsonObject category = elementCategory.getAsJsonObject();

                //in-built
                if (category.has("id")) {
                    final JsonElement elementId = category.get("id");
                    if (elementId.isJsonArray()) {
                        final JsonArray idArray = elementId.getAsJsonArray();
                        final EnumCategory[] collected = JsonMapper.mapJsonArraySafe(
                                idArray, element -> EnumCategory.define(JsonReadHelper.readString(element)),
                                EnumCategory[]::new
                        );
                        if (collected.length > 0) {
                            texturePack.setCategories(collected);
                        }
                    }
                }

                //custom
                if (category.has("custom")) {
                    final JsonElement elementCustom = category.get("custom");

                    if (elementCustom.isJsonArray()) {
                        final JsonArray arrayCustom = elementCustom.getAsJsonArray();
                        final String[] collected = JsonMapper.mapJsonArraySafe(
                                arrayCustom, element -> {
                                    final String collected1 = JsonReadHelper.readString(element);
                                    return (collected1 != null && collected1.isEmpty()) ? null : collected1;
                                },
                                String[]::new
                        );
                        if (collected.length > 0) {
                            texturePack.setCustomCategories(collected);
                        }
                    }
                }
            }
        }
    }
}
