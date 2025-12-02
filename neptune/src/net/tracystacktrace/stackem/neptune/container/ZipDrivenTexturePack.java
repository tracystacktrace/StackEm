package net.tracystacktrace.stackem.neptune.container;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipDrivenTexturePack extends ContainerTexturePack {
    protected ZipFile archive;

    public ZipDrivenTexturePack(@NotNull File archiveFile, @NotNull String name) {
        super(archiveFile, name);
    }

    public void openArchive() throws IOException {
        if (this.archive != null) {
            System.out.println("The openFile failed: the archive is already running over!");
            return;
        }
        this.archive = new ZipFile(this.file);
    }

    public void closeArchive() throws IOException {
        if (this.archive != null) {
            this.archive.close();
        }
    }

    public @NotNull URL getFullUrlOf(@NotNull ZipEntry entry) throws MalformedURLException {
        return new URL("jar:file:" + this.file.getAbsolutePath() + "!/" + entry.getName());
    }

    public @Nullable ZipEntry getEntry(@NotNull String path) {
        if (this.archive == null) {
            throw new IllegalStateException("Cannot access getEntry when the archive is not opened!");
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return this.archive.getEntry(path);
    }

    public @NotNull InputStream getInputStreamOf(@NotNull ZipEntry entry) throws IOException {
        if (this.archive == null) {
            throw new IllegalStateException("Cannot access getEntry when the archive is not opened!");
        }
        return this.archive.getInputStream(entry);
    }
}
