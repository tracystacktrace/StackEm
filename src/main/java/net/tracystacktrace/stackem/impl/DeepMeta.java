package net.tracystacktrace.stackem.impl;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.minecraft.common.item.ItemStack;
import net.tracystacktrace.stackem.processor.itemstackicon.TexturepackModify1;
import net.tracystacktrace.stackem.processor.itemstackicon.TexturepackSwapSet;
import net.tracystacktrace.stackem.processor.moon.CelestialMeta;
import net.tracystacktrace.stackem.processor.moon.EnumCelestialCycle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeepMeta {

    public CelestialMeta moonData;
    public CelestialMeta sunData;
    protected final Int2ObjectMap<TexturepackModify1> map1 = new Int2ObjectOpenHashMap<>();

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
        return map1.containsKey(itemID);
    }

    public @Nullable Icon getCustomIcon(@NotNull ItemStack stack) {
        return map1.get(stack.getItemID()).getIcon(stack);
    }

    public void registerAllIcons(IconRegister register) {
        for (TexturepackModify1 desc : map1.values()) {
            desc.registerIcon(register);
        }
    }

    public void addCodex(int itemID, @NotNull TexturepackSwapSet set) {
        if (!map1.containsKey(itemID)) {
            map1.put(itemID, new TexturepackModify1());
        }
        map1.get(itemID).add(set);
    }

    /* general methods */

    public void flush() {
        this.moonData = null;
        this.sunData = null;
        this.map1.clear();
    }
}
