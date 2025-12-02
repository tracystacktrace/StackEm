package net.tracystacktrace.stackem.modifications.moon;

import net.tracystacktrace.stackem.tools.QuickRNG;
import org.jetbrains.annotations.Nullable;

public enum EnumCelestialCycle {
    STATIC("static"),
    DEFAULT("default"),
    REVERSE("reverse"),
    RANDOM("random");

    public final String id;

    EnumCelestialCycle(String id) {
        this.id = id;
    }

    //total - total amount of moon cycles available
    public static int getMoon(EnumCelestialCycle cycle, long timeTicks, int total) {
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

    public static @Nullable EnumCelestialCycle getType(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        for (EnumCelestialCycle cycle : values()) {
            if (cycle.id.equalsIgnoreCase(name)) {
                return cycle;
            }
        }
        return null;
    }
}
