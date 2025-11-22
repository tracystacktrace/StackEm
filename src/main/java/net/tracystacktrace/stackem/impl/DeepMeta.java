package net.tracystacktrace.stackem.impl;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.tracystacktrace.stackem.modifications.entityvariation.EntityVariation;
import net.tracystacktrace.stackem.modifications.moon.CelestialMeta;
import net.tracystacktrace.stackem.modifications.moon.EnumCelestialCycle;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DeepMeta {
    //can be null-ed when flushed
    public CelestialMeta moonData;
    public CelestialMeta sunData;
    public Map<String, List<String>> entityVariations = new HashMap<>();

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

    /* entity variations system */

    public void addEntityVariation(@NotNull EntityVariation.Description description) {
        if (this.entityVariations == null) {
            this.entityVariations = new Object2ObjectArrayMap<>();
        }
        if (!this.entityVariations.containsKey(description.entity())) {
            this.entityVariations.put(description.entity(), new ArrayList<>());
        }
        if (description.forceOnly()) {
            this.entityVariations.get(description.entity()).clear();
        }

        this.entityVariations.get(description.entity()).addAll(Arrays.asList(description.variants()));
    }

    public boolean hasVariationsForEntity(@NotNull String entityName) {
        if (this.entityVariations == null)
            return false;
        return this.entityVariations.containsKey(entityName);
    }

    /* general methods */

    public void flush() {
        this.moonData = null;
        this.sunData = null;
        this.entityVariations = null;
    }
}
