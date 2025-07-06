package net.tracystacktrace.stackem.impl;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.minecraft.common.item.ItemStack;
import net.tracystacktrace.stackem.processor.itemstackicon.SingleItemSwap;
import net.tracystacktrace.stackem.processor.moon.CelestialMeta;
import net.tracystacktrace.stackem.processor.moon.EnumCelestialCycle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DeepMeta {

    protected final Int2ObjectMap<List<SingleItemSwap>> mapTexturepacksSwap = new Int2ObjectOpenHashMap<>();
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
    public boolean containsCodex(int itemID) {
        return mapTexturepacksSwap.containsKey(itemID);
    }

    public @Nullable Icon getCustomIcon(@NotNull ItemStack stack) {
        final List<SingleItemSwap> candidates = mapTexturepacksSwap.get(stack.getItemID());
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < candidates.size(); i++) {
            Icon value = candidates.get(i).getIcon(stack);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public void registerAllIcons(@NotNull IconRegister register) {
        for (List<SingleItemSwap> listSwaps : mapTexturepacksSwap.values()) {
            for (SingleItemSwap single : listSwaps) {
                single.registerIcon(register);
            }
        }
    }

    public void addCodex(int itemID, @NotNull SingleItemSwap set) {
        if (!mapTexturepacksSwap.containsKey(itemID)) {
            mapTexturepacksSwap.put(itemID, new ArrayList<>());
        }
        mapTexturepacksSwap.get(itemID).add(set);
    }

    /* general methods */

    public void flush() {
        this.moonData = null;
        this.sunData = null;
        this.mapTexturepacksSwap.forEach((i, s) -> s.clear());
        this.mapTexturepacksSwap.clear();
    }
}
