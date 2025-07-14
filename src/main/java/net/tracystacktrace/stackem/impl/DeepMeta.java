package net.tracystacktrace.stackem.impl;

import net.tracystacktrace.stackem.modifications.moon.CelestialMeta;
import net.tracystacktrace.stackem.modifications.moon.EnumCelestialCycle;

public class DeepMeta {
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

    /* general methods */

    public void flush() {
        this.moonData = null;
        this.sunData = null;
    }
}
