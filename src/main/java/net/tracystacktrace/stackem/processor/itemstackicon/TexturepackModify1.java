package net.tracystacktrace.stackem.processor.itemstackicon;

import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.minecraft.common.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TexturepackModify1 {
    protected final List<TexturepackSwapSet> candidates = new ArrayList<>();

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

    public void add(@NotNull TexturepackSwapSet set) {
        this.candidates.add(set);
    }
}
