package net.tracystacktrace.stackem.processor.itemstackicon;

import com.google.gson.*;
import net.minecraft.common.item.Item;
import net.minecraft.common.item.Items;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.impl.TexturePackStacked;
import net.tracystacktrace.stackem.processor.IJam;
import net.tracystacktrace.stackem.processor.itemstackicon.swap.TextureByMetadata;
import net.tracystacktrace.stackem.processor.itemstackicon.swap.TextureByName;
import net.tracystacktrace.stackem.tools.JsonReadHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;

public class JamItemStackTexture implements IJam {
    @Override
    public @NotNull String getPath() {
        return "/stackem.items.json";
    }

    @Override
    public void process(
            @NotNull TexturePackStacked stacked,
            @NotNull String rawJsonContent
    ) {
        try {
            final JsonObject root = JsonParser.parseString(rawJsonContent).getAsJsonObject();

            if (root.has("data")) {
                final JsonElement dataElement = root.get("data");
                if(dataElement.isJsonArray()) {
                    for (JsonElement element : dataElement.getAsJsonArray()) {
                        if (element.isJsonObject()) {
                            processItem(element.getAsJsonObject());
                        }
                    }
                }
            }

        } catch (JsonParseException e) {
            StackEm.LOGGER.severe("Failed to process stackem.sun.json for top texturepack");
            StackEm.LOGGER.throwing("JamSunTexture", "process", e);
        }
    }

    private void processItem(JsonObject object) {
        int targetItemID = -1;

        if (!object.has("item")) {
            StackEm.LOGGER.severe("No item/id present in stackem.items.json in a texturepack!");
            return;
        }

        if (object.has("item")) {
            final JsonElement itemElement = object.get("item");
            if(itemElement.isJsonPrimitive()) {
                final JsonPrimitive itemPrimitive = itemElement.getAsJsonPrimitive();
                if(itemPrimitive.isNumber()) {
                    final int item_iid = itemPrimitive.getAsInt();
                    if(this.isValidItemID(item_iid)) {
                        targetItemID = item_iid;
                    }
                } else if(itemPrimitive.isString()) {
                    final String item_iid = itemPrimitive.getAsString();
                    targetItemID = this.getItemIDByName(item_iid);
                }
            }
        }

        if (targetItemID == -1) {
            return;
        }

        final TextureByName[] textureByNames = JsonReadHelper.readObjectArray(object, "onName", TextureByName::fromJson, new TextureByName[0]);
        final TextureByMetadata[] textureByMetadata = JsonReadHelper.readObjectArray(object, "onMeta", TextureByMetadata::fromJson, new TextureByMetadata[0]);

        //sort them by priority
        if (textureByNames != null) Arrays.sort(textureByNames, Comparator.comparingInt(TextureByName::getPriority));
        if (textureByMetadata != null) Arrays.sort(textureByMetadata, Comparator.comparingInt(TextureByMetadata::getPriority));

        StackEm.getContainerDeepMeta().addCodex(targetItemID, new SingleItemSwap(targetItemID, textureByNames, textureByMetadata));
    }

    private boolean isValidItemID(int i) {
        if (i < 0 || i >= Items.ITEMS_LIST.length) {
            return false;
        }

        try {
            //noinspection unused
            Item item = Items.ITEMS_LIST[i];
            return item != null;
        } catch (Exception e) {
            return false;
        }
    }

    private int getItemIDByName(@Nullable String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            return -1;
        }

        for (Item item : Items.ITEMS_LIST) {
            if (item == null) continue;

            if (item.getItemName().equals(itemName)) {
                return item.itemID;
            }
        }

        return -1;
    }
}
