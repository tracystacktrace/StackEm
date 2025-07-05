package net.tracystacktrace.stackem.processor.itemstackicon;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.minecraft.common.item.ItemStack;
import net.tracystacktrace.stackem.processor.itemstackicon.swap.TextureByMetadata;
import net.tracystacktrace.stackem.processor.itemstackicon.swap.TextureByName;

import java.util.ArrayList;
import java.util.List;

public class GlobalSwapCandidates {
    public static final Int2ObjectMap<GlobalSwapCandidates> CODEX = new Int2ObjectOpenHashMap<>();

    public static boolean isIncluded(ItemStack stack) {
        return CODEX.containsKey(stack.getItemID());
    }

    public static Icon getCustomIcon(ItemStack stack) {
        List<TexturepackSwapSet> cands = CODEX.get(stack.getItemID()).candidates;

        for (TexturepackSwapSet swapDesc : cands) {
            if (swapDesc.hasOnMetas()) {
                for (int i = 0; i < swapDesc.textureByMetadata.length; i++) {
                    TextureByMetadata meta = swapDesc.textureByMetadata[i];
                    if (meta.compare(stack.getItemDamage())) {
                        return meta.textureIcon;
                    }
                }
            }

            if (swapDesc.hasOnNames()) {
                for (int i = 0; i < swapDesc.textureByNames.length; i++) {
                    TextureByName name = swapDesc.textureByNames[i];

                    if (name.compareString(stack.getDisplayName())) {
                        return name.textureIcon;
                    }
                }
            }
        }

        return null;
    }

    public static void flushEverything() {
        CODEX.forEach((integer, globalSwapCandidates) -> globalSwapCandidates.flush());
        CODEX.clear();
    }

    public final List<TexturepackSwapSet> candidates = new ArrayList<>();

    public void registerIcon(IconRegister register) {
        for(TexturepackSwapSet desc : this.candidates) {
            desc.registerIcon(register);
        }
    }

    public void flush() {
        this.candidates.clear();
    }
}
