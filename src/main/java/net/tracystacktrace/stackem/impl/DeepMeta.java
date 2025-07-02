package net.tracystacktrace.stackem.impl;

import net.tracystacktrace.stackem.processor.moon.EnumMoonCycle;
import net.tracystacktrace.stackem.processor.moon.CelestialMeta;

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

    public boolean isCustomMoon() {
        return moonData != null;
    }

    public CelestialMeta getMoonData() {
        return this.moonData;
    }

    public void setMoonData(CelestialMeta metadata) {
        this.moonData = metadata;
    }

    /* general methods */

    public void flush() {
        this.moonData = null;
    }
}
