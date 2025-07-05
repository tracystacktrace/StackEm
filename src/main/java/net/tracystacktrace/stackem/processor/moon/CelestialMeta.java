package net.tracystacktrace.stackem.processor.moon;

public class CelestialMeta {
    public final String path;
    public final EnumCelestialCycle cycle;
    public final float scale;
    public final int number_x;
    public final int number_y;
    public final int total;

    public CelestialMeta(String path, EnumCelestialCycle cycle, float scale, int numberX, int numberY) {
        this.path = path;
        this.cycle = cycle;
        this.scale = scale;
        this.number_x = numberX;
        this.number_y = numberY;
        this.total = numberX * numberY;
    }
}
