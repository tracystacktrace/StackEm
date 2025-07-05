package net.tracystacktrace.stackem.processor.itemstackicon;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.minecraft.common.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GlobalSwapCandidates {

    /* static work methods */

    public static final Int2ObjectMap<GlobalSwapCandidates> CODEX = new Int2ObjectOpenHashMap<>();

    /**
     * Checks whenever the swap manager contains the following item id
     */
    public static boolean contains(int itemID) {
        return CODEX.containsKey(itemID);
    }

    /**
     * Returns a custom icon based on ItemStack features
     */
    public static @Nullable Icon getCustomIcon(@NotNull ItemStack stack) {
        return CODEX.get(stack.getItemID()).getIcon(stack);
    }

    public static void flushEverything() {
        CODEX.forEach((integer, globalSwapCandidates) -> globalSwapCandidates.flush());
        CODEX.clear();
    }

    /* local instance fields/methods */

    public final List<TexturepackSwapSet> candidates = new ArrayList<>();

    public @Nullable Icon getIcon(@NotNull ItemStack stack) {
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < candidates.size(); i++) {
            Icon value = candidates.get(i).getIcon(stack);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public void registerIcon(@NotNull IconRegister register) {
        for (TexturepackSwapSet desc : this.candidates) {
            desc.registerIcon(register);
        }
    }

    public void flush() {
        this.candidates.clear();
    }
}
