package net.tracystacktrace.stackem.processor.imageglue.segment;

import org.jetbrains.annotations.NotNull;

public record SegmentedTexture(@NotNull String texture, int @NotNull [] @NotNull [] segments) {
    /**
     * Provides an empty array with the length equivalent to number of segments
     */
    public boolean[] generateOverwrite() {
        return new boolean[this.segments.length];
    }

    /**
     * Returns the id of the segment where the point (X/Y coords) is located
     *
     * @param pixelX X location of the pixel
     * @param pixelY Y location of the pixel
     * @param scale  scale of the reference image, usually (1) for x1 scale
     * @return id of the segment, otherwise (-1) if not found
     */
    public int isInWhatSegment(int pixelX, int pixelY, int scale) {
        for (int i = 0; i < this.segments.length; i++) {
            final int[] segment = this.segments[i];
            if (pixelX > (segment[0] * scale) && pixelX < ((segment[0] + segment[2]) * scale) &&
                    pixelY > (segment[1] * scale) && pixelY < ((segment[1] + segment[3]) * scale)) {
                return i;
            }
        }
        return -1;
    }
}
