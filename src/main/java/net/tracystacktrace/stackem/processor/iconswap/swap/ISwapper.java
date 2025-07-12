package net.tracystacktrace.stackem.processor.iconswap.swap;

import org.jetbrains.annotations.NotNull;

public interface ISwapper {
    int getPriority();

    boolean hasArmorTexture();

    String getArmorTexture();

    void setArmorTexture(@NotNull String s);

    boolean hasColor();

    void setEnableColor(boolean b);
}
