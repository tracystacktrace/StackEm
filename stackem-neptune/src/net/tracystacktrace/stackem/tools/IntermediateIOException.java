package net.tracystacktrace.stackem.tools;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class IntermediateIOException extends Exception {
    public IntermediateIOException(@NotNull String info, @NotNull IOException e) {
        super(info, e);
    }
}
