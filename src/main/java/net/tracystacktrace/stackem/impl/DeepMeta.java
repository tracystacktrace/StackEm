package net.tracystacktrace.stackem.impl;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.minecraft.common.item.ItemStack;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.sagittarius.ItemIconSwap;
import net.tracystacktrace.stackem.processor.moon.CelestialMeta;
import net.tracystacktrace.stackem.processor.moon.EnumCelestialCycle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DeepMeta {
    protected final Int2ObjectMap<List<ItemIconSwap>> mapTexturepacksSwap = new Int2ObjectOpenHashMap<>();

    //can be null-ed when flushed
    public CelestialMeta moonData;
    public CelestialMeta sunData;

    /* moon data related stuff */

    public int getMoonCycle(long ticks) {
        return EnumCelestialCycle.getMoon(moonData.cycle, ticks, moonData.total);
    }

    public float getMoonScale() {
        return 20.0F * moonData.scale;
    }

    public void setMoonData(CelestialMeta metadata) {
        this.moonData = metadata;
    }

    /* sun data related stuff */

    public int getSunCycle(long ticks) {
        return EnumCelestialCycle.getMoon(sunData.cycle, ticks, sunData.total);
    }

    public float getSunScale() {
        return 30.0F * sunData.scale;
    }

    public void setSunData(CelestialMeta metadata) {
        this.sunData = metadata;
    }

    /* icon methods */

    public boolean containsCustomSwapFor(int itemID) {
        return mapTexturepacksSwap.containsKey(itemID);
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public @Nullable Icon getCustomIcon(@NotNull ItemStack stack) {
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
    public @Nullable String getCustomArmor(@NotNull ItemStack stack) {
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

    public void registerAllIcons(@NotNull IconRegister register) {
        for (List<ItemIconSwap> listSwaps : mapTexturepacksSwap.values()) {
            for (ItemIconSwap single : listSwaps) {
                single.registerIcon(register);
            }
        }
    }

    public void addIconSwapper(@NotNull ItemIconSwap set) {
        if (!mapTexturepacksSwap.containsKey(set.target())) {
            mapTexturepacksSwap.put(set.target(), new ArrayList<>());
        }
        mapTexturepacksSwap.get(set.target()).add(set);
        StackEm.LOGGER.info(String.format(
                "Added custom swap for target [%s] of \"onName\" number %s, \"onMeta\" number %s",
                set.target(),
                set.textureByNames() != null ? set.textureByNames().length : "null",
                set.textureByMetadata() != null ? set.textureByMetadata().length : "null"
        ));
    }

    /* general methods */

    public void flush() {
        this.moonData = null;
        this.sunData = null;
        this.mapTexturepacksSwap.forEach((i, s) -> s.clear());
        this.mapTexturepacksSwap.clear();
    }
}
