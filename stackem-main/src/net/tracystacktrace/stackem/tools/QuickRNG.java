package net.tracystacktrace.stackem.tools;

/**
 * This is a set of quick RNGs (right now there's only one afaik)
 * <br>
 * Use it if you really want to get a determined values, such as entity skin values, etc
 */
public final class QuickRNG {
    /**
     * A very simple, deterministic, yet effective RNG based on input value.
     * <br>
     * Basically hashes the value integer and outputs an answer based on it.
     *
     * @param value A value to base on random
     * @param min   Minimum range value (inclusive)
     * @param max   Maximum range value (exclusive)
     * @return A random number between corresponding to [min;max) range
     */
    public static int getBetween(final int value, final int min, final int max) {
        if (max <= min) {
            throw new IllegalArgumentException("The value (max) must be bigger than (min)!");
        }
        int hash = value;
        hash ^= (hash << 13);
        hash ^= (hash >>> 17);
        hash ^= (hash << 5);
        hash = hash & Integer.MAX_VALUE;
        return min + (hash % (max - min));
    }
}
