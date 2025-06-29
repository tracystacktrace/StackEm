package net.tracystacktrace.stackem.impl;

import com.indigo3d.util.RenderSystem;
import net.minecraft.client.renderer.world.RenderEngine;
import net.tracystacktrace.stackem.processor.category.EnumCategory;

import java.awt.image.BufferedImage;
import java.io.File;

public class TagTexturePack {
    //pack.txt/pack.png public attributes
    public final File file;
    public final String name;
    public final String firstLine;
    public final String secondLine;

    //current order
    public int order = -1;

    //thumbnail inner data
    private BufferedImage thumbnail;
    private int rendererThumbnailID = -1;

    //stackem.json attributes
    protected String author;
    protected String website;
    protected EnumCategory[] categories;
    protected String[] customCategories;

    //category inner data
    private String bakedCategoryString;

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

    public void removeThumbnail(RenderEngine renderEngine) {
        if (rendererThumbnailID != -1) {
            renderEngine.deleteTexture(this.rendererThumbnailID);
            this.thumbnail = null;
        }
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setCategories(EnumCategory[] categories) {
        this.categories = categories;
    }

    public void setCustomCategories(String[] categories) {
        this.customCategories = categories;
    }

    public boolean hasWebsite() {
        return this.website != null;
    }

    public String getWebsite() {
        return this.website;
    }

    public boolean hasAuthor() {
        return this.author != null;
    }

    public String getAuthor() {
        return this.author;
    }

    public boolean hasCategories() {
        return this.bakedCategoryString != null;
    }

    public String getBakedCategoryString() {
        return this.bakedCategoryString;
    }

    public void buildCategory() {
        if(this.categories != null || this.customCategories != null) {
            this.bakedCategoryString = EnumCategory.collect(this.categories, this.customCategories);
        }
    }

}
