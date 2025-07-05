package net.tracystacktrace.stackem.processor.itemstackicon;

import com.google.gson.*;
import net.minecraft.common.item.Item;
import net.minecraft.common.item.Items;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.impl.TexturePackStacked;
import net.tracystacktrace.stackem.processor.IJam;
import net.tracystacktrace.stackem.processor.itemstackicon.swap.TextureOnMeta;
import net.tracystacktrace.stackem.processor.itemstackicon.swap.TextureOnName;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

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

            if(root.has("data")) {
                JsonArray array = root.getAsJsonArray("data");
                for(JsonElement element : array) {
                    if(element.isJsonObject()) {
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
            final int value = this.getIntFor(object.get("item").getAsString());
            if(value != -1) {
                targetItemID = value;
            }
        }

        if (object.has("id")) {
            final int value = object.get("id").getAsInt();
            if (this.checkItemId(value)) {
                targetItemID = value;
            }
        }

        if(targetItemID == -1) {
            return;
        }

        final TextureOnName[] textureOnNames = this.readArray(object, "onname", TextureOnName::fromJson, new TextureOnName[0]);
        final TextureOnMeta[] textureOnMetas = this.readArray(object, "onmeta", TextureOnMeta::fromJson, new TextureOnMeta[0]);

        // dumb way to "prevent" excess swaps per single item id
        if(!SwapCandidates.CODEX.containsKey(targetItemID)) {
            SwapCandidates.CODEX.put(targetItemID, new SwapCandidates());
        }

        SwapCandidates.CODEX.get(targetItemID).candidates.add(new TextureSwapDesc(targetItemID, textureOnNames, textureOnMetas));
    }

    private <T> T[] readArray(JsonObject object, String name, Function<JsonObject, T> generator, T[] empty) {
        if(name == null || name.isEmpty() || !object.has(name)) {
            return null;
        }
        final Set<T> collector = new HashSet<>();

        for(JsonElement element : object.get(name).getAsJsonArray()) {
            if(!element.isJsonObject()) {
                continue;
            }
            try {
                final T value = generator.apply(element.getAsJsonObject());
                collector.add(value);
            } catch (IllegalArgumentException e) {
                StackEm.LOGGER.severe("Failed to process item [" + name + "] swap data!");
                StackEm.LOGGER.throwing("ItemStackCooker", "read", e);
            }
        }

        return collector.isEmpty() ? null : collector.toArray(empty);
    }


    private boolean checkItemId(int i) {
        if (i < 0 || i >= Items.ITEMS_LIST.length) {
            return false;
        }

        try {
            Item item = Items.ITEMS_LIST[i];
            System.out.println("Test: " + item.itemID);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private int getIntFor(String itemName) {
        if(itemName == null || itemName.isEmpty()) {
            return -1;
        }

        for(Item item : Items.ITEMS_LIST) {
            if(item == null) continue;

            if(item.getItemName().equals(itemName)) {
                return item.itemID;
            }
        }

        return -1;
    }
}
