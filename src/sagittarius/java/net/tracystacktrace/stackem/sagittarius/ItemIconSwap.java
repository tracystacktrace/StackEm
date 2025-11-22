package net.tracystacktrace.stackem.sagittarius;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.minecraft.common.item.ItemStack;
import net.minecraft.common.item.Items;
import net.tracystacktrace.stackem.sagittarius.swap.TextureByMetadata;
import net.tracystacktrace.stackem.sagittarius.swap.TextureByName;
import net.tracystacktrace.stackem.tools.JsonExtractionException;
import net.tracystacktrace.stackem.tools.JsonMapper;
import net.tracystacktrace.stackem.tools.ThrowingJson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public record ItemIconSwap(
        int target,
        @NotNull TextureByName @Nullable [] textureByNames,
        @NotNull TextureByMetadata @Nullable [] textureByMetadata
) {
    @SuppressWarnings("ForLoopReplaceableByForEach")
    public @Nullable Icon getIcon(@NotNull ItemStack stack) {
        if (this.hasAnyMetaSwaps()) {
            for (int i = 0; i < this.textureByMetadata.length; i++) {
                TextureByMetadata meta = this.textureByMetadata[i];
                if (meta.compare(stack.getItemDamage())) {
                    return meta.textureIcon;
                }
            }
        }

        if (this.hasAnyNameSwaps()) {
            for (int i = 0; i < this.textureByNames.length; i++) {
                final TextureByName name = this.textureByNames[i];
                if (name.compareString(stack.getDisplayName())) {
                    return name.textureIcon;
                }
            }
        }

        return null;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public @Nullable String getArmorTexture(@NotNull ItemStack stack) {
        if (this.hasAnyMetaSwaps()) {
            for (int i = 0; i < this.textureByMetadata.length; i++) {
                TextureByMetadata meta = this.textureByMetadata[i];
                if (!meta.hasArmorTexture()) {
                    continue;
                }

                if (meta.compare(stack.getItemDamage())) {
                    return meta.getArmorTexture();
                }
            }
        }

        if (this.hasAnyNameSwaps()) {
            for (int i = 0; i < this.textureByNames.length; i++) {
                final TextureByName name = this.textureByNames[i];
                if (!name.hasArmorTexture()) {
                    continue;
                }

                if (name.compareString(stack.getDisplayName())) {
                    return name.getArmorTexture();
                }
            }
        }

        return null;
    }

    public void registerIcon(@NotNull IconRegister register) {
        if (this.hasAnyMetaSwaps()) {
            for (TextureByMetadata meta : this.textureByMetadata) {
                meta.registerIcon(register);
            }
        }
        if (this.hasAnyNameSwaps()) {
            for (TextureByName name : this.textureByNames) {
                name.registerIcon(register);
            }
        }
    }

    public boolean hasAnyNameSwaps() {
        return this.textureByNames != null && this.textureByNames.length > 0;
    }

    public boolean hasAnyMetaSwaps() {
        return this.textureByMetadata != null && this.textureByMetadata.length > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemIconSwap that = (ItemIconSwap) o;
        return target == that.target &&
                Objects.deepEquals(textureByNames, that.textureByNames) &&
                Objects.deepEquals(textureByMetadata, that.textureByMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target, Arrays.hashCode(textureByNames), Arrays.hashCode(textureByMetadata));
    }

    public static @NotNull ItemIconSwap fromJson(
            @NotNull final JsonObject object,
            @NotNull final String sourceName
    ) throws IconProcessorException, JsonExtractionException {
        int targetItemID = -1;

        if (!object.has("item"))
            throw new IconProcessorException(IconProcessorException.NOT_FOUND_ITEM_ID);


        //item processor
        if (ThrowingJson.isInteger(object, "item")) {
            final int item_iid = object.get("item").getAsInt();
            if (isValidItemID(item_iid)) {
                targetItemID = item_iid;
            }
        } else if (ThrowingJson.isString(object, "item")) {
            targetItemID = getItemIDByName(object.get("item").getAsString());
        }

        if (targetItemID == -1)
            throw new IconProcessorException(IconProcessorException.INVALID_ITEM_ID, String.valueOf(targetItemID));


        //swapper processors
        TextureByName[] textureByNames = null;
        TextureByMetadata[] textureByMetadata = null;

        if (object.has("onName")) {
            final JsonArray onNameArray = ThrowingJson.cautiouslyGetArray(object, "onName", sourceName);

            try {
                textureByNames = JsonMapper.mapJsonArrayWithException(onNameArray, o -> TextureByName.fromJson(o, sourceName), TextureByName[]::new);
            } catch (IconProcessorException | JsonExtractionException e) {
                throw new IconProcessorException(IconProcessorException.ON_NAME_PROCESS_FAILED, onNameArray.toString(), e);
            }
        }

        if (object.has("onMeta")) {
            final JsonArray onMetaArray = ThrowingJson.cautiouslyGetArray(object, "onMeta", sourceName);

            try {
                textureByMetadata = JsonMapper.mapJsonArrayWithException(onMetaArray, o -> TextureByMetadata.fromJson(o, sourceName), TextureByMetadata[]::new);
            } catch (IconProcessorException | JsonExtractionException e) {
                throw new IconProcessorException(IconProcessorException.ON_META_PROCESS_FAILED, onMetaArray.toString(), e);
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
