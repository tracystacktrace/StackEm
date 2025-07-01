package net.tracystacktrace.stackem.impl;

import net.minecraft.client.renderer.block.ITexturePack;
import net.minecraft.client.renderer.block.TexturePackBase;
import net.minecraft.client.renderer.world.RenderEngine;
import net.tracystacktrace.stackem.StackEm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class TexturePackStacked extends TexturePackBase {
    private final ITexturePack defaultTexturePack;
    private final File[] archives;
    private final List<ZipFile> stackedTextures;
    private boolean initialized = false;
    private final DeepMeta deepMeta = new DeepMeta();

    public TexturePackStacked(String id, ITexturePack defaultTexturePack, List<File> texturepackArchives) {
        super(id, null, "stackem.tpstacked", defaultTexturePack);

        final File[] files = new File[texturepackArchives.size()];
        for (int i = 0; i < files.length; i++) {
            files[i] = texturepackArchives.get(i);
        }
        this.stackedTextures = new ArrayList<>();
        this.archives = files;
        this.defaultTexturePack = defaultTexturePack;
    }

    @Override
    protected void loadDescription() {
        this.firstDescriptionLine = "Stack 'Em Internal";
        this.secondDescriptionLine = "Do not use.";
    }

    public List<ZipFile> getZipFiles() {
        return this.stackedTextures;
    }

    private void initialize() {
        if (!initialized) {
            for (int i = 0; i < archives.length; i++) {
                File file = archives[i];
                try {
                    stackedTextures.add(new ZipFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                    StackEm.LOGGER.severe("Failed to load texturepack (" + file.getAbsolutePath() + "), reason: " + e.getMessage());
                }
            }
            this.initialized = true;
        }
    }

    @Override
    public void deleteTexturePack(RenderEngine renderEngine) {
        //super.deleteTexturePack(renderEngine);
        for (int i = 0; i < this.stackedTextures.size(); i++) {
            ZipFile zipFile = this.stackedTextures.get(i);
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                StackEm.LOGGER.severe("Failed to close texturepack (" + zipFile.getName() + "), reason: " + e.getMessage());
            }
        }
        this.stackedTextures.clear();
        this.deepMeta.flush();
    }

    @Override
    protected InputStream findResource(String resourcePath) throws IOException {
        if (resourcePath.equals("/pack.png")) {
            throw new FileNotFoundException(resourcePath);
        }

        this.initialize();

        if (StackEm.DEBUG_DISABLE) {
            return this.defaultTexturePack.findResource(resourcePath, false);
        }

        for (int i = 0; i < this.stackedTextures.size(); i++) {
            ZipFile zipFile = this.stackedTextures.get(i);
            ZipEntry entry = zipFile.getEntry(resourcePath.substring(1));
            if (entry != null) {
                return zipFile.getInputStream(entry);
            }
        }

        throw new FileNotFoundException(resourcePath);
    }

    @Override
    protected URL findResourceURL(String resourcePath) {
        try {
            this.initialize();
        } catch (Exception e) {
            return null;
        }

        if (StackEm.DEBUG_DISABLE) {
            return this.defaultTexturePack.findResourceURL(resourcePath, false);
        }

        for (int i = 0; i < this.stackedTextures.size(); i++) {
            ZipFile zipFile = this.stackedTextures.get(i);
            final ZipEntry entry = zipFile.getEntry(resourcePath.substring(1));
            if (entry != null) {
                try {
                    //noinspection deprecation
                    final URL url = new URL("jar:file:" + this.archives[i].getAbsolutePath() + "!/" + entry.getName());

                    //friendly debug output
                    final String friendlyOutput = url.toString();
                    StackEm.LOGGER.info("Loading resource from \"" + friendlyOutput.substring(friendlyOutput.lastIndexOf(File.separatorChar, friendlyOutput.indexOf("!/")) + 1).replace("!", "\": "));

                    return url;
                } catch (MalformedURLException ignored) {
                }
            }
        }

        return null;
    }

    @Override
    public boolean checkIfFileExists(String resourcePath) {
        try {
            this.initialize();

            if (StackEm.DEBUG_DISABLE) {
                return this.defaultTexturePack.checkIfFileExists(resourcePath, false);
            }

            for (int i = 0; i < this.stackedTextures.size(); i++) {
                ZipFile zipFile = this.stackedTextures.get(i);
                if (zipFile.getEntry(resourcePath.substring(1)) != null)
                    return true;
            }
            return false;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public boolean checkIfDirectoryExists(String path) {
        try {
            this.initialize();

            if (StackEm.DEBUG_DISABLE) {
                return this.defaultTexturePack.checkIfDirectoryExists(path, false);
            }

            for (int i = 0; i < this.stackedTextures.size(); i++) {
                ZipFile zipFile = this.stackedTextures.get(i);
                ZipEntry entry = zipFile.getEntry(path.substring(1));
                if (entry != null && entry.isDirectory())
                    return true;
            }
            return false;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public boolean isCompatible() {
        return true;
    }

    public boolean isEmpty() {
        return this.archives == null || this.archives.length == 0;
    }

    public DeepMeta getDeepMeta() {
        return this.deepMeta;
    }
}
