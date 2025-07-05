package net.tracystacktrace.stackem.processor.category;

import com.google.gson.*;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.impl.TagTexturePack;

import java.util.Objects;
import java.util.stream.StreamSupport;

public final class DescriptionFileCooker {

    public static void read(TagTexturePack texturePack, String content) {
        try {
            final JsonObject root = JsonParser.parseString(content).getAsJsonObject();

            if (root.has("author")) {
                texturePack.setAuthor(root.get("author").getAsString());
            }

            if (root.has("website")) {
                texturePack.setWebsite(root.get("website").getAsString());
            }

            if (root.has("category")) {
                final JsonObject category = root.get("category").getAsJsonObject();

                //in-built
                if (category.has("id")) {
                    final JsonArray idArray = category.getAsJsonArray("id");
                    if (!idArray.isEmpty()) {
                        EnumCategory[] collected = StreamSupport.stream(idArray.spliterator(), false)
                                .map(JsonElement::getAsString)
                                .map(EnumCategory::define)
                                .filter(Objects::nonNull)
                                .toArray(EnumCategory[]::new);

                        if (collected.length > 0) {
                            texturePack.setCategories(collected);
                        }
                    }
                }

                //custom
                if (category.has("custom")) {
                    final JsonArray customArray = category.getAsJsonArray("custom");
                    if (!customArray.isEmpty()) {
                        String[] collected = StreamSupport.stream(customArray.spliterator(), false)
                                .map(JsonElement::getAsString)
                                .filter(Objects::nonNull)
                                .filter(e -> !e.isEmpty())
                                .toArray(String[]::new);

                        if (collected.length > 0) {
                            texturePack.setCustomCategories(collected);
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
