package net.tracystacktrace.stackem.processor;

import net.minecraft.client.renderer.block.ITexturePack;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ImageReader {

    public static BufferedImage readImage(ITexturePack texturePack, String name) {
        try {
            final InputStream inputStream = texturePack.getResourceAsStream(name);
            BufferedImage image = ImageIO.read(inputStream);
            inputStream.close();
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage readImage(ZipFile zipFile, String name) {
        try {
            final ZipEntry entry = zipFile.getEntry(name.substring(1));
            if (entry != null) {
                final InputStream inputStream = zipFile.getInputStream(entry);
                final BufferedImage image = ImageIO.read(inputStream);
                inputStream.close();
                return image;
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
