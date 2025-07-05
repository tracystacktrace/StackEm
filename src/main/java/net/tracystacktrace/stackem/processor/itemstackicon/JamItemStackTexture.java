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
                JsonArray array = root.getAsJsonArray("data");
                for (JsonElement element : array) {
                    if (element.isJsonObject()) {
                        processItem(element.getAsJsonObject());
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

        if (!object.has("item") && !object.has("id")) {
            StackEm.LOGGER.severe("No item/id present in stackem.items.json in a texturepack!");
            return;
        }

        if (object.has("item")) {
            targetItemID = this.getIntFor(object.get("item").getAsString());
        }

        if (object.has("id")) {
            final int value = object.get("id").getAsInt();
            if (this.checkItemId(value)) {
                targetItemID = value;
            }
        }

        if (targetItemID == -1) {
            return;
        }

        final TextureByName[] textureByNames = JsonReadHelper.readObjectArray(object, "onName", TextureByName::fromJson, new TextureByName[0]);
        final TextureByMetadata[] textureByMetadata = JsonReadHelper.readObjectArray(object, "onMeta", TextureByMetadata::fromJson, new TextureByMetadata[0]);

        // dumb way to "prevent" excess swaps per single item id
        if (!GlobalSwapCandidates.CODEX.containsKey(targetItemID)) {
            GlobalSwapCandidates.CODEX.put(targetItemID, new GlobalSwapCandidates());
        }

        TexturepackSwapSet swapSet = new TexturepackSwapSet(targetItemID, textureByNames, textureByMetadata);
        GlobalSwapCandidates.CODEX.get(targetItemID).candidates.add(swapSet);
    }

    private boolean checkItemId(int i) {
        if (i < 0 || i >= Items.ITEMS_LIST.length) {
            return false;
        }

        try {
            Item item = Items.ITEMS_LIST[i];
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private int getIntFor(String itemName) {
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
