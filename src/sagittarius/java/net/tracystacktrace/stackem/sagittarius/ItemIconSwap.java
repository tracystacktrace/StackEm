package net.tracystacktrace.stackem.sagittarius;

import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.minecraft.common.item.ItemStack;
import net.tracystacktrace.stackem.sagittarius.swap.TextureByMetadata;
import net.tracystacktrace.stackem.sagittarius.swap.TextureByName;
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
}
