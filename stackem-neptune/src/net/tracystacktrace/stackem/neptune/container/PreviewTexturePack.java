package net.tracystacktrace.stackem.neptune.container;

import net.tracystacktrace.stackem.neptune.category.EnumCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PreviewTexturePack extends ContainerTexturePack {
    public final @NotNull String firstLine;
    public final @NotNull String secondLine;
    public final @NotNull String sha256;

    protected @Nullable BufferedImage icon; //icon image
    protected int iconBindIdentifier = -1; //for OpenGL

    protected String target_version;
    protected String website;
    protected String[] authors;
    protected String[] custom_categories;
    protected EnumCategory[] categories;

    private String[] bakedCategoriesList; //raw shit

    public PreviewTexturePack(
            @NotNull File archiveFile,
            @NotNull String name,
            @NotNull String firstLine,
            @NotNull String secondLine,
            @NotNull String sha256
    ) {
        super(archiveFile, name);
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.sha256 = sha256;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public void setStackemData(
            @Nullable String target_version,
            @Nullable String website,
            @Nullable String[] authors,
            @Nullable String[] custom_categories,
            @Nullable String[] categories
    ) {
        this.target_version = target_version;
        this.website = website;
        this.authors = authors;
        this.custom_categories = custom_categories;

        // Custom boxing for EnumCategory
        if (categories != null && categories.length > 0) {
            final List<EnumCategory> rawData = new ArrayList<>();
            for (int i = 0; i < categories.length; i++) {
                final EnumCategory result = EnumCategory.define(categories[i]);
                if (result != null) {
                    rawData.add(result);
                }
            }
            this.categories = rawData.toArray(new EnumCategory[0]);
        }
    }

    /* Category Building Tools */

    public boolean hasWebsite() {
        return this.website != null && !this.website.isEmpty();
    }

    public boolean hasAuthors() {
        return this.authors != null && this.authors.length > 0;
    }

    public boolean hasBakedCategoriesList() {
        return this.bakedCategoriesList != null;
    }

    public @Nullable String getWebsite() {
        return this.website;
    }

    public @NotNull String @Nullable [] getAuthors() {
        return this.authors;
    }

    public @NotNull String @Nullable [] getBakedCategories() {
        return this.bakedCategoriesList;
    }

    public void bakeCategoryList(@NotNull Function<@NotNull String, @NotNull String> translateFunction) {
        if (this.categories != null || this.custom_categories != null) {
            this.bakedCategoriesList = EnumCategory.collect(translateFunction, this.categories, this.custom_categories);
        }
    }

    /* Icon Management Methods */

    public void setIcon(@NotNull BufferedImage icon) {
        this.icon = icon;
    }

    public boolean hasIcon() {
        return this.icon != null;
    }

    public @Nullable BufferedImage getIcon() {
        return this.icon;
    }

    public boolean hasTextureIndex() {
        return this.iconBindIdentifier != -1;
    }

    public int getTextureIndex() {
        return this.iconBindIdentifier;
    }

    public void setTextureIndex(int i) {
        this.iconBindIdentifier = i;
    }

    public int popTextureIndex() {
        if (this.iconBindIdentifier != -1) {
            int temp = this.iconBindIdentifier;
            this.iconBindIdentifier = -1;
            this.icon = null;
            return temp;
        }
        return this.iconBindIdentifier;
    }
}
