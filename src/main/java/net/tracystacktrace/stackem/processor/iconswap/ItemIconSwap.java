package net.tracystacktrace.stackem.processor.iconswap;

import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.minecraft.common.item.ItemStack;
import net.tracystacktrace.stackem.processor.iconswap.swap.TextureByMetadata;
import net.tracystacktrace.stackem.processor.iconswap.swap.TextureByName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public record ItemIconSwap(
        int target,
        @NotNull TextureByName @Nullable [] textureByNames,
        @NotNull TextureByMetadata @Nullable [] textureByMetadata
) {
    @SuppressWarnings("ForLoopReplaceableByForEach")
    public @Nullable Icon getIcon(@NotNull ItemStack stack) {
        if (this.anySwapsByMeta()) {
            for (int i = 0; i < this.textureByMetadata.length; i++) {
                TextureByMetadata meta = this.textureByMetadata[i];
                if (meta.compare(stack.getItemDamage())) {
                    return meta.textureIcon;
                }
            }
        }

        if (this.anySwapsByName()) {
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
        if (this.anySwapsByMeta()) {
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

        if (this.anySwapsByName()) {
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

    public boolean anySwapsByName() {
        return this.textureByNames != null && this.textureByNames.length > 0;
    }

    public boolean anySwapsByMeta() {
        return this.textureByMetadata != null && this.textureByMetadata.length > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemIconSwap desc = (ItemIconSwap) o;
        return target == desc.target && Objects.deepEquals(textureByNames, desc.textureByNames) && Objects.deepEquals(textureByMetadata, desc.textureByMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target, Arrays.hashCode(textureByNames), Arrays.hashCode(textureByMetadata));
    }

    @Override
    public @NotNull String toString() {
        return "SingleItemSwap{" +
                "target=" + target +
                ", textureByNames=" + Arrays.toString(textureByNames) +
                ", textureByMetadata=" + Arrays.toString(textureByMetadata) +
                '}';
    }
}
