package net.tracystacktrace.stackem.modifications.imageglue;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.TexturePackDefault;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.hacks.SmartHacks;
import net.tracystacktrace.stackem.impl.TexturePackStacked;
import net.tracystacktrace.stackem.modifications.imageglue.segment.SegmentedTexture;
import net.tracystacktrace.stackem.tools.ImageHelper;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

public final class GlueImages {
    public static class ImageGlueContainer {
        public BufferedImage original;
        public BufferedImage canvas;

        private final int original_width;

        public ImageGlueContainer(@NotNull BufferedImage image) {
            this.original = image;
            //basically copy the image into an independent object
            this.canvas = ImageHelper.fullCopy(image);

            this.original_width = original.getWidth();
        }

        private boolean rescaleContainer(int width, int height) {
            //refer to scaling the result
            if (original.getWidth() < width || original.getHeight() < height) {
                this.original = ImageHelper.scaleImage(original, width, height);
                this.canvas = ImageHelper.scaleImage(canvas, width, height);
                return true;
            }
            return false; //signal to scale not the original, but modified version
        }

        public int makeChanges(@NotNull BufferedImage attempt, @NotNull SegmentedTexture holder) {
            if (attempt.getWidth() != this.original.getWidth()) {
                if (!rescaleContainer(attempt.getWidth(), attempt.getHeight())) {
                    attempt = ImageHelper.scaleImage(attempt, original.getWidth(), original.getHeight());
                }
            }

            final int width = this.original.getWidth();
            final int height = this.original.getHeight();
            final int scale = width / original_width;

            final boolean[] modParts = holder.generateOverwrite();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int origPixel = this.original.getRGB(x, y);
                    int modPixel = attempt.getRGB(x, y);

                    if (origPixel != modPixel) {
                        int test1 = holder.isInWhatSegment(x, y, scale);
                        if (test1 > -1) {
                            modParts[test1] = true;
                        }
                    }
                }
            }

            int amount = 0;
            for (int i = 0; i < modParts.length; i++) {
                if (modParts[i]) {
                    final int x = holder.segments()[i][0] * scale;
                    final int y = holder.segments()[i][1] * scale;
                    final int endX = (x + holder.segments()[i][2]) * scale;
                    final int endY = (y + holder.segments()[i][3]) * scale;

                    for (int movY = y; movY < endY; movY++) {
                        for (int movX = x; movX < endX; movX++) {
                            this.canvas.setRGB(movX, movY, attempt.getRGB(movX, movY));
                        }
                    }
                    amount++;
                }
            }

            return amount;
        }

        public void flush() {
            this.original.flush();
        }

        public void debugSave(@NotNull String name) {
            try {
                ImageIO.write(canvas, "png", new File(Minecraft.getInstance().getMinecraftDir(), name + ".png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static @NotNull BufferedImage processLayering(@NotNull SegmentedTexture name) {
        final TexturePackDefault defaultPack = (TexturePackDefault) SmartHacks.getDefaultTexturePack();
        final TexturePackStacked stacked = StackEm.getContainerInstance();

        final ImageGlueContainer original = new ImageGlueContainer(ImageHelper.readImage(defaultPack, name.texture()));
        final List<BufferedImage> images = new ArrayList<>();

        //fetching texturepacks that can into gluing
        List<ZipFile> zipFiles = stacked.getZipFiles();
        for (int i = zipFiles.size() - 1; i >= 0; i--) {
            final ZipFile zipFile = zipFiles.get(i);
            final BufferedImage image = ImageHelper.readImage(zipFile, name.texture());
            if (ImageHelper.isValidTexture(image)) {
                images.add(image);
            } else {
                if (image != null) {
                    image.flush();
                }
            }
        }

        //actual gluing process
        int changesNum = 0;

        for (final BufferedImage attack : images) {
            changesNum += original.makeChanges(attack, name);
        }

        if (changesNum != 0) {
            StackEm.LOGGER.info(String.format("Overwrote %s image segments for: %s", changesNum, name.texture()));
        } else {
            StackEm.LOGGER.info(String.format("No image segments gluing candidates were found: %s", name.texture()));
        }

        //clean-up process
        for (BufferedImage image : images) {
            image.flush();
        }
        images.clear();

        original.flush();
        return original.canvas;
    }
}
