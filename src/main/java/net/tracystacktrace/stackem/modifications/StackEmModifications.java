package net.tracystacktrace.stackem.modifications;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.world.RenderEngine;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.hacks.SmartHacks;
import net.tracystacktrace.stackem.impl.TexturePackStacked;
import net.tracystacktrace.stackem.modifications.imageglue.GlueImages;
import net.tracystacktrace.stackem.modifications.imageglue.segment.SegmentedTexture;
import net.tracystacktrace.stackem.modifications.imageglue.segment.SegmentsProvider;
import net.tracystacktrace.stackem.modifications.moon.CelestialMeta;
import net.tracystacktrace.stackem.modifications.moon.MoonReader;
import net.tracystacktrace.stackem.sagittarius.IconProcessorException;
import net.tracystacktrace.stackem.sagittarius.IconSwapReader;
import net.tracystacktrace.stackem.sagittarius.ItemIconSwap;
import net.tracystacktrace.stackem.sagittarius.SagittariusBridge;
import net.tracystacktrace.stackem.tools.IOReadHelper;
import net.tracystacktrace.stackem.tools.JsonExtractionException;
import net.tracystacktrace.stackem.tools.ZipFileHelper;

import java.awt.image.BufferedImage;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A main entrypoint for most texture/sound modifications handled by this mod
 */
public final class StackEmModifications {
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
        if (renderEngine == null || StackEm.DEBUG_FORCE_FALLBACK) {
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

        //moon and sun
        if (stacked.checkIfFileExists("/stackem.moon.json")) {
            try {
                final String content = IOReadHelper.readTextFile("/stackem.moon.json", stacked::getResourceAsStream);
                final JsonObject object = IOReadHelper.processJson(content);
                final CelestialMeta compiled = MoonReader.fromJson(object);
                stacked.getDeepMeta().setMoonData(compiled);
            } catch (IOReadHelper.CustomIOException e) {
                StackEm.LOGGER.severe("Failed fetching and compiling contents of stackem.moon.json");
                StackEm.LOGGER.throwing("StackEmModifications", "fetchTextureModifications", e);
                e.printStackTrace();
            }
        }

        if (stacked.checkIfFileExists("/stackem.sun.json")) {
            try {
                final String content = IOReadHelper.readTextFile("/stackem.sun.json", stacked::getResourceAsStream);
                final JsonObject object = IOReadHelper.processJson(content);
                final CelestialMeta compiled = MoonReader.fromJson(object);
                stacked.getDeepMeta().setMoonData(compiled);
            } catch (IOReadHelper.CustomIOException e) {
                StackEm.LOGGER.severe("Failed fetching and compiling contents of stackem.sun.json");
                StackEm.LOGGER.throwing("StackEmModifications", "fetchTextureModifications", e);
                e.printStackTrace();
            }
        }
    }

    public static void fetchIconModifications(RenderEngine render) {
        if (render == null || StackEm.DEBUG_FORCE_FALLBACK) {
            return;
        }

        final TexturePackStacked stacked = StackEm.getContainerInstance();

        //no textures - no changes
        if (stacked.isEmpty()) {
            return;
        }

        for (final ZipFile file : stacked.getZipFiles()) {
            final ZipEntry entry = ZipFileHelper.getEntryFor(file, "stackem.items.json");
            if (entry == null) continue;

            try {
                final ItemIconSwap[] possible = IconSwapReader.fromJson(
                        file.getName() + "!/stackem.items.json",
                        ZipFileHelper.readTextFile(file, entry)
                );

                for (ItemIconSwap item : possible) {
                    SagittariusBridge.addIconSwapper(item);
                }

            } catch (ZipFileHelper.ZipIOException e) {
                StackEm.LOGGER.severe(String.format("Failed to read file: %s", file.getName() + "!/stackem.items.json"));
                StackEm.LOGGER.throwing("StackEmModifications", "fetchIconModifications", e);
            } catch (IconProcessorException | JsonExtractionException e) {
                StackEm.LOGGER.severe(String.format("Failed during compiling icon swapper of %s", file.getName() + "!/stackem.items.json"));
                StackEm.LOGGER.throwing("StackEmModifications", "fetchIconModifications", e);

                //we NEED to see full stacktrace at any chance!
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
    }
}
