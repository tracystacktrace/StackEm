package net.tracystacktrace.stackem.processor.itemstackicon;

import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.minecraft.common.item.ItemStack;
import net.tracystacktrace.stackem.processor.itemstackicon.swap.TextureByMetadata;
import net.tracystacktrace.stackem.processor.itemstackicon.swap.TextureByName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public record SingleItemSwap(int target, TextureByName[] textureByNames, TextureByMetadata[] textureByMetadata) {

    public boolean anySwapsByName() {
        return this.textureByNames != null && this.textureByNames.length > 0;
    }

    public boolean anySwapsByMeta() {
        return this.textureByMetadata != null && this.textureByMetadata.length > 0;
    }

    public @Nullable Icon getIcon(@NotNull ItemStack stack) {
        if (this.anySwapsByMeta()) {
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < this.textureByMetadata.length; i++) {
                TextureByMetadata meta = this.textureByMetadata[i];
                if (meta.compare(stack.getItemDamage())) {
                    return meta.textureIcon;
                }
            }
        }

        if (this.anySwapsByName()) {
            for (int i = 0; i < this.textureByNames.length; i++) {
                TextureByName name = this.textureByNames[i];
                if (name.compareString(stack.getDisplayName())) {
                    return name.textureIcon;
                }
            }
        }
        return null;
    }

    public void registerIcon(IconRegister register) {
        if (this.anySwapsByMeta()) {
            for (TextureByMetadata meta : this.textureByMetadata) {
                meta.registerIcon(register);
            }
        }
        if (this.anySwapsByName()) {
            for (TextureByName name : this.textureByNames) {
                name.registerIcon(register);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SingleItemSwap desc = (SingleItemSwap) o;
        return target == desc.target && Objects.deepEquals(textureByNames, desc.textureByNames) && Objects.deepEquals(textureByMetadata, desc.textureByMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target, Arrays.hashCode(textureByNames), Arrays.hashCode(textureByMetadata));
    }

    @Override
    public String toString() {
        return "TexturepackSwapSet{" +
                "target=" + target +
                ", textureByNames=" + Arrays.toString(textureByNames) +
                ", textureByMetadata=" + Arrays.toString(textureByMetadata) +
                '}';
    }
}
