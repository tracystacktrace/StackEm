package net.tracystacktrace.stackem.processor;

import net.tracystacktrace.stackem.impl.TexturePackStacked;
import org.jetbrains.annotations.NotNull;

public interface IJam {

    @NotNull String getPath();

    void process(
            @NotNull TexturePackStacked stacked,
            @NotNull String rawJsonContent
    );
}
