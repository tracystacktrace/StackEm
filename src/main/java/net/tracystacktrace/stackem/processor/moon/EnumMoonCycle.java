package net.tracystacktrace.stackem.processor.moon;

import net.tracystacktrace.stackem.tools.QuickRNG;
import org.jetbrains.annotations.Nullable;

public enum EnumMoonCycle {
    STATIC("static"),
    DEFAULT("default"),
    REVERSE("reverse"),
    RANDOM("random");

    public final String id;

    EnumMoonCycle(String id) {
        this.id = id;
    }

    //total - total amount of moon cycles available
    public static int getMoon(EnumMoonCycle cycle, long timeTicks, int total) {
        switch (cycle) {
            case STATIC -> {
                return 0;
            }
            case DEFAULT -> {
                return ((int) (timeTicks / 24000L)) % total;
            }
            case REVERSE -> {
                final int phase = ((int) (timeTicks / 24000L)) % total;
                return (total - phase) % total;
            }
            case RANDOM -> {
                return QuickRNG.getBetween((int) (timeTicks / 24000L), 0, total);
            }
            default -> {
                return -1;
            }
        }
    }

    public static @Nullable EnumMoonCycle getType(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        for (EnumMoonCycle cycle : values()) {
            if (cycle.id.equalsIgnoreCase(name)) {
                return cycle;
            }
        }
        return null;
    }
}
