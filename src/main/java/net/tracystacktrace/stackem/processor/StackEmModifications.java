package net.tracystacktrace.stackem.processor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.world.RenderEngine;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.hack.SmartHacks;
import net.tracystacktrace.stackem.impl.TexturePackStacked;
import net.tracystacktrace.stackem.processor.image.GlueImages;
import net.tracystacktrace.stackem.processor.image.segment.SegmentedTexture;
import net.tracystacktrace.stackem.processor.image.segment.SegmentsProvider;

import java.awt.image.BufferedImage;

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

    /**
     * This is the actual code you should call upon refreshing textures
     */
    public static void fetchTextureModifications(RenderEngine renderEngine) {
        if (renderEngine == null) {
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
                int id = SmartHacks.getTextureMap(renderEngine).getInt(value.texture);
                renderEngine.setupTexture(image, id);
            } else {
                //no id present - we will add it then
                int i = renderEngine.allocateAndSetupTexture(image);
                SmartHacks.getTextureMap(renderEngine).put(value.texture, i);
            }
        }
    }

}
