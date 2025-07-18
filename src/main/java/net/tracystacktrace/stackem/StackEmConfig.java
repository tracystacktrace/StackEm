package net.tracystacktrace.stackem;

import com.fox2code.foxloader.config.ConfigEntry;

public final class StackEmConfig {
    @ConfigEntry(configComment = "Will always use pride logo textures")
    public boolean eternalPride = true;

    @ConfigEntry(configComment = "Hide tilda in the item display name")
    public boolean hideTilda = false;
}
