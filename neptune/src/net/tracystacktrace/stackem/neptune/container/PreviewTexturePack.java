package net.tracystacktrace.stackem.neptune.container;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Function;
import java.util.function.IntConsumer;

public class PreviewTexturePack extends ContainerTexturePack {
    protected final @NotNull String firstLine;
    protected final @NotNull String secondLine;
    protected final @NotNull String sha256;

    protected @Nullable BufferedImage icon; //icon image
    protected int iconBindIdentifier = -1; //for OpenGL

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

    public void setIcon(@NotNull BufferedImage icon) {
        this.icon = icon;
    }

    public @Nullable BufferedImage getIcon() {
        return this.icon;
    }

    public int getTextureBind() {
        return this.iconBindIdentifier;
    }

    public void bindTexture(@NotNull Function<BufferedImage, Integer> bindProvider) {
        if (this.icon != null && this.iconBindIdentifier == -1) {
            this.iconBindIdentifier = bindProvider.apply(this.icon);
        }
    }
    public void removeTexture(@NotNull IntConsumer bindRemover) {
        if (this.iconBindIdentifier != -1) {
            bindRemover.accept(this.iconBindIdentifier);
            this.iconBindIdentifier = -1;
            this.icon = null;
        }
    }
}
