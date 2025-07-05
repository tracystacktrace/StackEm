package net.tracystacktrace.stackem.processor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.world.RenderEngine;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.hack.SmartHacks;
import net.tracystacktrace.stackem.impl.TexturePackStacked;
import net.tracystacktrace.stackem.processor.imageglue.GlueImages;
import net.tracystacktrace.stackem.processor.imageglue.segment.SegmentedTexture;
import net.tracystacktrace.stackem.processor.imageglue.segment.SegmentsProvider;
import net.tracystacktrace.stackem.processor.itemstackicon.JamItemStackTexture;
import net.tracystacktrace.stackem.processor.moon.JamMoonTexture;
import net.tracystacktrace.stackem.processor.moon.JamSunTexture;
import net.tracystacktrace.stackem.tools.ZipFileHelper;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A main entrypoint for most texture/sound modifications handled by this mod
 */
public final class StackEmModifications {

    public static final IJam[] JAMS = new IJam[]{
            new JamMoonTexture(), new JamSunTexture()
    };

    private static final IJam JAM_IS_SWAP = new JamItemStackTexture();

    /**
     * This is the actual code you should call upon refreshing textures
     * <br>
     * This one takes {@link RenderEngine} instance from {@link Minecraft#renderEngine} field
     */
    public static void fetchTextureModifications() {
        StackEmModifications.fetchTextureModifications(Minecraft.getInstance().renderEngine);
    }

    public static void fetchIconModifications() {
        StackEmModifications.fetchIconModifications(Minecraft.getInstance().renderEngine);
    }

    /**
     * This is the actual code you should call upon refreshing textures
     */
    public static void fetchTextureModifications(RenderEngine renderEngine) {
        if (renderEngine == null || StackEm.DEBUG_DISABLE) {
            return;
        }

        final TexturePackStacked stacked = StackEm.getContainerInstance();

        //no textures - no changes
        if (stacked.isEmpty()) {
            return;
        }

        //first step - segmented textures
        for (SegmentedTexture value : SegmentsProvider.TEXTURES) {
            //process texture layering, get a layered texture
            BufferedImage image = GlueImages.processLayering(value);

            if (SmartHacks.getTextureMap(renderEngine).containsKey(value.texture)) {
                //hack solution - simply overwrite the texture with the in-built code
                final int id = SmartHacks.getTextureMap(renderEngine).getInt(value.texture);
                renderEngine.setupTexture(image, id);
            } else {
                //no id present - we will add it then
                final int loc = renderEngine.allocateAndSetupTexture(image);
                SmartHacks.getTextureMap(renderEngine).put(value.texture, loc);
            }
        }

        //read through different ONLY-TOP jams
        for (IJam jam : JAMS) {

            if (!stacked.checkIfFileExists(jam.getPath())) {
                continue;
            }

            String collectedJsonString;
            try (InputStream inputStream = stacked.getResourceAsStream(jam.getPath());
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                collectedJsonString = reader.lines().collect(Collectors.joining());
            } catch (IOException e) {
                StackEm.LOGGER.severe("Couldn't read file: " + jam.getPath());
                StackEm.LOGGER.throwing("StackEmModifications", "fetchTextureModifications", e);
                continue;
            }

            if (!collectedJsonString.isEmpty()) {
                jam.process(stacked, collectedJsonString);
                StackEm.LOGGER.info("Loaded JAM: " + jam.getPath());
            }
        }
    }

    public static void fetchIconModifications(RenderEngine render) {
        if (render == null || StackEm.DEBUG_DISABLE) {
            return;
        }
        final TexturePackStacked stacked = StackEm.getContainerInstance();

        //no textures - no changes
        if (stacked.isEmpty()) {
            return;
        }

        //process EVERY-STACK jams
        for (ZipFile file : stacked.getZipFiles()) {
            final ZipEntry entry = ZipFileHelper.getEntryFor(file, JAM_IS_SWAP.getPath());
            if (entry == null) continue;

            try {
                final String data = ZipFileHelper.readTextFile(file, entry);
                JAM_IS_SWAP.process(stacked, data);
            } catch (ZipFileHelper.CustomZipOperationException e) {
                StackEm.LOGGER.severe("Failed during JAM fetch: " + JAM_IS_SWAP.getPath());
                StackEm.LOGGER.throwing("StackEmModifications", "fetchIconModifications", e);
            }
        }
    }
}
