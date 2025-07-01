package net.tracystacktrace.stackem.processor.image;

import net.minecraft.client.renderer.block.ITexturePack;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class ImageHelper {

    public static boolean isValidTexture(BufferedImage check) {
        return check != null && check.getWidth() == check.getHeight();
    }

    public static BufferedImage scaleImage(BufferedImage original, int targetWidth, int targetHeight) {
        Image resultingImage = original.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();

        return outputImage;
    }

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
