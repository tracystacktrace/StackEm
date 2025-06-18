package net.tracystacktrace.stackem.processor.image;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.world.RenderEngine;
import net.tracystacktrace.stackem.hack.SmartHacks;
import net.tracystacktrace.stackem.processor.image.segment.SegmentedTexture;
import net.tracystacktrace.stackem.processor.image.segment.SegmentsProvider;

import java.awt.image.BufferedImage;

public class TextureMerger {

    /**
     * This is the actual code you should call upon refreshing textures
     */
    public static void replaceTextures() {
        TextureMerger.replaceTextures(Minecraft.getInstance().renderEngine);
    }

    public static void replaceTextures(RenderEngine renderEngine) {
        if (renderEngine == null) {
            return;
        }

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
