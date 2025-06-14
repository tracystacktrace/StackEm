package net.tracystacktrace.stackem.processor;

import java.awt.image.BufferedImage;

public class ImageCheck {

    public static boolean isValidTexture(BufferedImage check) {
        return check != null && check.getWidth() == check.getHeight();
    }

}
