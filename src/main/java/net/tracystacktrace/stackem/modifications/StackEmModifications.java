package net.tracystacktrace.stackem.modifications;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.world.RenderEngine;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.impl.ModernStackedImpl;
import net.tracystacktrace.stackem.modifications.entityvariation.EntityVariation;
import net.tracystacktrace.stackem.modifications.imageglue.ImageGlueBridge;
import net.tracystacktrace.stackem.modifications.moon.CelestialMeta;
import net.tracystacktrace.stackem.modifications.moon.MoonReader;
import net.tracystacktrace.stackem.neptune.container.ZipDrivenTexturePack;
import net.tracystacktrace.stackem.sagittarius.IconProcessorException;
import net.tracystacktrace.stackem.sagittarius.IconSwapReader;
import net.tracystacktrace.stackem.sagittarius.ItemIconSwap;
import net.tracystacktrace.stackem.sagittarius.SagittariusBridge;
import net.tracystacktrace.stackem.tools.ZipFileHelper;
import net.tracystacktrace.stackem.tools.json.JsonExtractionException;
import net.tracystacktrace.stackem.tools.json.ThrowingJson;

import java.io.IOException;

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

        final ModernStackedImpl stacked = StackEm.getContainerInstance();

        //no textures - no changes
        if (stacked.isEmpty()) {
            return;
        }

        ImageGlueBridge.processTexturesSegments(renderEngine);

        //moon and sun
        if (stacked.checkIfFileExists("/stackem.moon.json")) {
            try {
                final String content = stacked.readTextFile("/stackem.moon.json");
                final JsonObject object = ThrowingJson.stringToJsonObject(content, content); //TODO: replace 2nd content with name
                final CelestialMeta compiled = MoonReader.fromJson(object, content);
                stacked.getDeepMeta().setMoonData(compiled);
            } catch (IOException | JsonExtractionException e) {
                StackEm.LOGGER.severe("Failed fetching and compiling contents of stackem.moon.json");
                StackEm.LOGGER.throwing("StackEmModifications", "fetchTextureModifications", e);
                e.printStackTrace();
            }
        }

        if (stacked.checkIfFileExists("/stackem.sun.json")) {
            try {
                final String content = stacked.readTextFile("/stackem.sun.json");
                final JsonObject object = ThrowingJson.stringToJsonObject(content, content); //TODO: replace 2nd content with name
                final CelestialMeta compiled = MoonReader.fromJson(object, content);
                stacked.getDeepMeta().setSunData(compiled);
            } catch (IOException | JsonExtractionException e) {
                StackEm.LOGGER.severe("Failed fetching and compiling contents of stackem.sun.json");
                StackEm.LOGGER.throwing("StackEmModifications", "fetchTextureModifications", e);
                e.printStackTrace();
            }
        }

        //parse entity textures
        ZipDrivenTexturePack[] zipFiles = stacked.getArchives();
        for (int i = zipFiles.length - 1; i >= 0; i--) {
            final ZipDrivenTexturePack file = zipFiles[i];
            if (!file.hasEntry("stackem.entity.json")) continue;

            try {
                final EntityVariation.Description[] possible = EntityVariation.fromJson(
                        file.readTextFile("stackem.entity.json"),
                        file.getName() + "!/stackem.entity.json"
                );

                for (EntityVariation.Description item : possible) {
                    StackEm.getContainerInstance().getDeepMeta().addEntityVariation(item);
                }

            } catch (ZipFileHelper.ZipIOException e) {
                StackEm.LOGGER.severe(String.format("Failed to read file: %s", file.getName() + "!/stackem.items.json"));
                StackEm.LOGGER.throwing("StackEmModifications", "fetchIconModifications", e);
            } catch (JsonExtractionException e) {
                StackEm.LOGGER.severe(String.format("Failed during compiling icon swapper of %s", file.getName() + "!/stackem.items.json"));
                StackEm.LOGGER.throwing("StackEmModifications", "fetchIconModifications", e);

                //we NEED to see full stacktrace at any chance!
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
    }

    public static void fetchIconModifications(RenderEngine render) {
        if (render == null || StackEm.DEBUG_FORCE_FALLBACK) {
            return;
        }

        final ModernStackedImpl stacked = StackEm.getContainerInstance();

        //no textures - no changes
        if (stacked.isEmpty()) {
            return;
        }

        for (final ZipDrivenTexturePack file : stacked.getArchives()) {
            if (!file.hasEntry("stackem.items.json")) continue;

            try {
                final ItemIconSwap[] possible = IconSwapReader.fromJsonFile(
                        file.getName() + "!/stackem.items.json",
                        file.readTextFile("stackem.items.json")
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
