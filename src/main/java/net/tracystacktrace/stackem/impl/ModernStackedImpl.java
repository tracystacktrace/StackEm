package net.tracystacktrace.stackem.impl;

import net.minecraft.client.renderer.block.ITexturePack;
import net.minecraft.client.renderer.block.TexturePackBase;
import net.minecraft.client.renderer.world.RenderEngine;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.neptune.StackedIO;
import net.tracystacktrace.stackem.neptune.container.ZipDrivenTexturePack;
import net.tracystacktrace.stackem.sagittarius.SagittariusBridge;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class ModernStackedImpl extends TexturePackBase {
    private final ITexturePack defaultTexturePack;
    private final StackedIO driverEngine;
    private final DeepMeta deepMeta = new DeepMeta();

    public ModernStackedImpl(
            String id,
            ITexturePack defaultTexturePack,
            List<File> texturepackArchives
    ) {
        super(id, null, "stackem.tpstapcked", defaultTexturePack);
        this.defaultTexturePack = defaultTexturePack;
        this.driverEngine = new StackedIO(texturepackArchives);
    }

    @Override
    protected void loadDescription() {
        this.firstDescriptionLine = "Stack 'Em Internal Object";
        this.secondDescriptionLine = "Do not touch, use or look.";
    }

    @Override
    public void deleteTexturePack(RenderEngine renderEngine) {
        //super.deleteTexturePack(renderEngine);
        this.driverEngine.collapse();
        this.deepMeta.flush();
        SagittariusBridge.clearAll();
    }

    @Override
    protected InputStream findResource(String resourcePath) throws IOException {
        if (StackEm.DEBUG_FORCE_FALLBACK) {
            return this.defaultTexturePack.findResource(resourcePath, false);
        }
        if (this.driverEngine == null) {
            throw new IOException("Not yet initialized!");
        }
        return this.driverEngine.getInputStream(resourcePath);
    }

    @Override
    protected URL findResourceURL(String resourcePath) {
        if (StackEm.DEBUG_FORCE_FALLBACK) {
            return this.defaultTexturePack.findResourceURL(resourcePath, false);
        }
        return this.driverEngine.find(resourcePath);
    }

    @Override
    public boolean checkIfFileExists(String resourcePath) {
        if (StackEm.DEBUG_FORCE_FALLBACK) {
            return this.defaultTexturePack.checkIfFileExists(resourcePath, false);
        }
        return this.driverEngine.fileExists(resourcePath);
    }

    @Override
    public boolean checkIfDirectoryExists(String path) {
        if (StackEm.DEBUG_FORCE_FALLBACK) {
            return this.defaultTexturePack.checkIfDirectoryExists(path, false);
        }
        return this.driverEngine.directoryExists(path);
    }

    @Override
    public boolean isCompatible() {
        return true;
    }

    public boolean isEmpty() {
        return this.driverEngine.isEmpty();
    }

    public DeepMeta getDeepMeta() {
        return this.deepMeta;
    }

    public boolean hasCustomMoon() {
        return this.deepMeta.moonData != null;
    }

    public boolean hasCustomSun() {
        return this.deepMeta.sunData != null;
    }

    public @NotNull String readTextFile(@NotNull String path) throws IOException {
        final InputStream inputStream;

        try {
            inputStream = this.getResourceAsStream(path);
        } catch (IOException e) {
            throw new IOException(String.format("Error during accessing an InputStream instance of %s", path), e);
        }

        String collected;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            collected = reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new IOException(String.format("Error during reading the file of %s", path), e);
        }

        try {
            inputStream.close();
        } catch (IOException ignored) {
        }

        return collected;
    }

    public ZipDrivenTexturePack[] getArchives() {
        return this.driverEngine.getArchives();
    }
}
