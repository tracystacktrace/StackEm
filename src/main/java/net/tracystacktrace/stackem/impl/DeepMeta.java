package net.tracystacktrace.stackem.impl;

import net.tracystacktrace.stackem.processor.moon.CelestialMeta;
import net.tracystacktrace.stackem.processor.moon.EnumMoonCycle;

public class DeepMeta {

    protected CelestialMeta moonData;

    /* moon data related stuff */

    public int getMoonCycle(long ticks) {
        return EnumMoonCycle.getMoon(moonData.cycle, ticks, moonData.total);
    }

    public float getMoonScale() {
        return 20.0F * moonData.scale;
    }

    public String getMoonTexture() {
        return this.moonData.path;
    }

    public int getMoonHorizontals() {
        return this.moonData.number_x;
    }

    public int getMoonVerticals() {
        return this.moonData.number_y;
    }

    public void setMoonData(CelestialMeta metadata) {
        this.moonData = metadata;
    }

    /* general methods */

    public void flush() {
        this.moonData = null;
    }
}
