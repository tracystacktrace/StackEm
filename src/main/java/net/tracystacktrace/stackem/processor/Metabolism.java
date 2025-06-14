package net.tracystacktrace.stackem.processor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.world.RenderEngine;
import net.tracystacktrace.stackem.hack.SmartHacks;

import java.awt.image.BufferedImage;

public class Metabolism {

    public static void replaceTextures(RenderEngine renderEngine) {
        if(renderEngine == null) {
            return;
        }

        for (SegmentedTexture value : SegmentManager.TEXTURES) {
            //process texture layering, get a layered texture
            BufferedImage image = GlueingProcessor.processLayering(value);

            if(SmartHacks.getTextureMap(renderEngine).containsKey(value.texture)) {
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

    public static void replaceTextures() {
        Metabolism.replaceTextures(
                Minecraft.getInstance().renderEngine
//                new String[]{
//                        "/textures/gui/effects.png",
//                        "/textures/gui/gui.png",
//                        "/textures/gui/icons.png",
//                        "/textures/gui/web_buttons.png",
//                        "/textures/gui/stats/slot.png",
//                }
        );
    }

}
