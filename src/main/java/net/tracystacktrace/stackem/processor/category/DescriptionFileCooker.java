package net.tracystacktrace.stackem.processor.category;

import com.google.gson.*;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.impl.TagTexturePack;
import net.tracystacktrace.stackem.tools.JsonReadHelper;

public final class DescriptionFileCooker {

    public static void read(TagTexturePack texturePack, String content) {
        try {
            final JsonObject root = JsonParser.parseString(content).getAsJsonObject();

            if (root.has("author")) {
                texturePack.setAuthor(JsonReadHelper.readString(root.get("author")));
            }

            if (root.has("website")) {
                texturePack.setWebsite(JsonReadHelper.readString(root.get("website")));
            }

            if (root.has("category")) {
                final JsonElement elementCategory = root.get("category");

                if (elementCategory.isJsonObject()) {
                    final JsonObject category = elementCategory.getAsJsonObject();

                    //in-built
                    if (category.has("id")) {
                        final JsonElement elementId = category.get("id");
                        if (elementId.isJsonArray()) {
                            final JsonArray idArray = elementId.getAsJsonArray();
                            final EnumCategory[] collected = JsonReadHelper.transformArraySafe(
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
                            final String[] collected = JsonReadHelper.transformArraySafe(
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

        } catch (JsonParseException e) {
            StackEm.LOGGER.severe("Failed to process stackem.json for " + texturePack.name);
            StackEm.LOGGER.throwing("DescriptionFileCooker", "read", e);
        }
    }

}
