package net.tracystacktrace.stackem.neptune.container;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ContainerTexturePack {
    protected final File file;
    protected final String name;

    //current order
    public int order = -1;

    public ContainerTexturePack(@NotNull File archiveFile, @NotNull String name) {
        this.file = archiveFile;
        this.name = name;
    }

    public boolean isInStack() {
        return this.order > -1;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull File getFile() {
        return this.file;
    }

    public @NotNull String getAbsolutePath() {
        return this.file.getAbsolutePath();
    }
}
