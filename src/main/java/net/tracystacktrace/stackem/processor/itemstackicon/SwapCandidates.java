package net.tracystacktrace.stackem.processor.itemstackicon;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.minecraft.common.item.ItemStack;
import net.tracystacktrace.stackem.processor.itemstackicon.swap.TextureOnMeta;
import net.tracystacktrace.stackem.processor.itemstackicon.swap.TextureOnName;

import java.util.ArrayList;
import java.util.List;

public class SwapCandidates {
    public static final Int2ObjectMap<SwapCandidates> CODEX = new Int2ObjectOpenHashMap<>();

    public static boolean isIncluded(ItemStack stack) {
        return CODEX.containsKey(stack.getItemID());
    }

    public static Icon getCustomIcon(ItemStack stack) {
        List<TextureSwapDesc> cands = CODEX.get(stack.getItemID()).candidates;

        for (TextureSwapDesc swapDesc : cands) {
            if (swapDesc.hasOnMetas()) {
                for (int i = 0; i < swapDesc.textureOnMetas.length; i++) {
                    TextureOnMeta meta = swapDesc.textureOnMetas[i];
                    if (meta.compare(stack.getItemDamage())) {
                        return meta.textureIcon;
                    }
                }
            }

            if (swapDesc.hasOnNames()) {
                for (int i = 0; i < swapDesc.textureOnNames.length; i++) {
                    TextureOnName name = swapDesc.textureOnNames[i];

                    if (name.compareString(stack.getDisplayName())) {
                        return name.textureIcon;
                    }
                }
            }
        }

        return null;
    }

    public final List<TextureSwapDesc> candidates = new ArrayList<>();

    public void registerIcon(IconRegister register) {
        for(TextureSwapDesc desc : this.candidates) {
            desc.registerIcon(register);
        }
    }
}
