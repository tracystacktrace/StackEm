package net.tracystacktrace.stackem.processor.image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageScaler {

    public static BufferedImage scaleImage(BufferedImage original, int targetWidth, int targetHeight) {
        Image resultingImage = original.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();

        return outputImage;
    }

}
