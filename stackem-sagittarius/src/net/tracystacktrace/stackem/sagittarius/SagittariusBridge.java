package net.tracystacktrace.stackem.sagittarius;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.minecraft.common.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class SagittariusBridge {
    private static final Int2ObjectMap<List<ItemIconSwap>> mapTexturepacksSwap = new Int2ObjectOpenHashMap<>();

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static @Nullable Icon getCustomIcon(@NotNull ItemStack stack) {
        final List<ItemIconSwap> candidates = mapTexturepacksSwap.get(stack.getItemID());
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }

        for (int i = 0; i < candidates.size(); i++) {
            final Icon icon = candidates.get(i).getIcon(stack);
            if (icon != null) {
                return icon;
            }
        }
        return null;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public static @Nullable String getCustomArmor(@NotNull ItemStack stack) {
        final List<ItemIconSwap> candidates = mapTexturepacksSwap.get(stack.getItemID());
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }

        for (int i = 0; i < candidates.size(); i++) {
            final String icon = candidates.get(i).getArmorTexture(stack);
            if (icon != null) {
                return icon;
            }
        }
        return null;
    }

    public static void registerAllIcons(@NotNull IconRegister register) {
        for (List<ItemIconSwap> listSwaps : mapTexturepacksSwap.values()) {
            for (ItemIconSwap single : listSwaps) {
                single.registerIcon(register);
            }
        }
    }

    public static boolean containsIconSwapper(int itemID) {
        return mapTexturepacksSwap.containsKey(itemID);
    }

    public static void addIconSwapper(@NotNull ItemIconSwap set) {
        if (!mapTexturepacksSwap.containsKey(set.target())) {
            mapTexturepacksSwap.put(set.target(), new ArrayList<>());
        }
        mapTexturepacksSwap.get(set.target()).add(set);
        System.out.println(String.format(
                "Added custom swap for target [%s] of \"onName\" number %s, \"onMeta\" number %s",
                set.target(),
                set.textureByNames() != null ? set.textureByNames().length : "null",
                set.textureByMetadata() != null ? set.textureByMetadata().length : "null"
        ));
    }

    public static void clearAll() {
        mapTexturepacksSwap.forEach((i, s) -> s.clear());
        mapTexturepacksSwap.clear();
    }
}
