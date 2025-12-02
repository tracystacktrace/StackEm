package net.tracystacktrace.stackem.modifications.imageglue;

import net.minecraft.client.renderer.block.TexturePackDefault;
import net.minecraft.client.renderer.world.RenderEngine;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.hacks.SmartHacks;
import net.tracystacktrace.stackem.impl.TexturePackStacked;
import net.tracystacktrace.stackem.modifications.imageglue.segment.SegmentedTexture;
import net.tracystacktrace.stackem.modifications.imageglue.segment.SegmentsProvider;
import net.tracystacktrace.stackem.tools.ImageHelper;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

public final class ImageGlueBridge {
    public static void processTexturesSegments(@NotNull RenderEngine renderEngine) {
        //first step - segmented textures
        for (SegmentedTexture value : SegmentsProvider.TEXTURES) {
            //process texture layering, get a layered texture
            BufferedImage image = processLayering(value);

            if (SmartHacks.getTextureMap(renderEngine).containsKey(value.texture())) {
                //hack solution - simply overwrite the texture with the in-built code
                final int id = SmartHacks.getTextureMap(renderEngine).getInt(value.texture());
                renderEngine.setupTexture(image, id);
            } else {
                //no id present - we will add it then
                final int loc = renderEngine.allocateAndSetupTexture(image);
                SmartHacks.getTextureMap(renderEngine).put(value.texture(), loc);
            }
        }
    }

    private static @NotNull BufferedImage processLayering(@NotNull SegmentedTexture name) {
        final TexturePackDefault defaultPack = (TexturePackDefault) SmartHacks.getDefaultTexturePack();
        final TexturePackStacked stacked = StackEm.getContainerInstance();

        final ImageGlueContainer original = new ImageGlueContainer(ImageHelper.readImage(defaultPack::getResourceAsStream, name.texture()));
        final List<BufferedImage> images = new ArrayList<>();

        //fetching texturepacks that can into gluing
        List<ZipFile> zipFiles = stacked.getZipFiles();
        for (int i = zipFiles.size() - 1; i >= 0; i--) {
            final ZipFile zipFile = zipFiles.get(i);
            final BufferedImage image = ImageHelper.readImage(zipFile, name.texture());
            if (ImageHelper.isValidSquareTexture(image)) {
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
