package net.tracystacktrace.stackem.processor;

@SuppressWarnings("ClassCanBeRecord")
public class SegmentedTexture {
    public final String texture;
    public final int[][] segments;

    public SegmentedTexture(String texture, int[][] segments) {
        this.texture = texture;
        this.segments = segments;
    }

    public boolean[] generateOverwrite() {
        return new boolean[this.segments.length];
    }

    public int isInWhatSegment(int pixelX, int pixelY, int scale) {
        for (int i = 0; i < this.segments.length; i++) {
            int[] segment = this.segments[i];
            if (pixelX > (segment[0] * scale) && pixelX < ((segment[0] + segment[2]) * scale) &&
                    pixelY > (segment[1] * scale) && pixelY < ((segment[1] + segment[3]) * scale)) {
                return i;
            }
        }
        return -1;
    }
}
