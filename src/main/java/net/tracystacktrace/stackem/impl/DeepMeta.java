package net.tracystacktrace.stackem.impl;

import net.tracystacktrace.stackem.processor.moon.MoonCycleCooker;

public class DeepMeta {

    protected MoonCycleCooker.MoonMetadata moonData;

    public void setMoonData(MoonCycleCooker.MoonMetadata metadata) {
        this.moonData = metadata;
    }

    public boolean isMoonDifferent() {
        return moonData != null;
    }

    public MoonCycleCooker.MoonMetadata getMoonData() {
        return this.moonData;
    }

    public void flush() {
        this.moonData = null;
    }
}
