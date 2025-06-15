package net.tracystacktrace.stackem.impl;

import com.indigo3d.util.RenderSystem;
import net.minecraft.client.renderer.world.RenderEngine;

import java.awt.image.BufferedImage;
import java.io.File;

public class TagTexturePack {
    public final File file;
    public final String name;
    public final String firstLine;
    public final String secondLine;

    public int order = -1;

    private BufferedImage thumbnail;
    private int rendererThumbnailID = -1;

    public TagTexturePack(File file, String name, String firstLine, String secondLine) {
        this.file = file;
        this.name = name;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
    }

    public boolean isInStack() {
        return this.order > -1;
    }

    public void setThumbnail(BufferedImage image) {
        this.thumbnail = image;
    }

    public boolean hasThumbnail() {
        return this.thumbnail != null;
    }

    public void bindThumbnail(RenderEngine renderEngine) {
        if (rendererThumbnailID == -1) {
            this.rendererThumbnailID = renderEngine.allocateAndSetupTexture(this.thumbnail);
        }
        RenderSystem.bindTexture2D(rendererThumbnailID);
    }
}
