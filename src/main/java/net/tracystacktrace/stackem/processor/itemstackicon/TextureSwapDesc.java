package net.tracystacktrace.stackem.processor.itemstackicon;

import net.minecraft.common.block.icon.IconRegister;
import net.tracystacktrace.stackem.processor.itemstackicon.swap.TextureOnMeta;
import net.tracystacktrace.stackem.processor.itemstackicon.swap.TextureOnName;

import java.util.Arrays;
import java.util.Objects;

public class TextureSwapDesc {
    public final int target;
    public final TextureOnName[] textureOnNames;
    public final TextureOnMeta[] textureOnMetas;

    public TextureSwapDesc(int target, TextureOnName[] textureOnNames, TextureOnMeta[] textureOnMetas) {
        this.target = target;
        this.textureOnNames = textureOnNames;
        this.textureOnMetas = textureOnMetas;
    }

    public boolean hasOnNames() {
        return this.textureOnNames != null && this.textureOnNames.length > 0;
    }

    public boolean hasOnMetas() {
        return this.textureOnMetas != null && this.textureOnMetas.length > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TextureSwapDesc desc = (TextureSwapDesc) o;
        return target == desc.target && Objects.deepEquals(textureOnNames, desc.textureOnNames) && Objects.deepEquals(textureOnMetas, desc.textureOnMetas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target, Arrays.hashCode(textureOnNames), Arrays.hashCode(textureOnMetas));
    }

    public void registerIcon(IconRegister register) {
        if(this.hasOnMetas()) {
            for(TextureOnMeta meta : this.textureOnMetas) {
                meta.registerIcon(register);
            }
        }
        if(this.hasOnNames()) {
            for(TextureOnName name : this.textureOnNames) {
                name.registerIcon(register);
            }
        }
    }
}
