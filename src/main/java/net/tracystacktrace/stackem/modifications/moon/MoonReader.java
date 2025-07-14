package net.tracystacktrace.stackem.modifications.moon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.tracystacktrace.stackem.tools.JsonReadHelper;
import org.jetbrains.annotations.NotNull;

public final class MoonReader {
    public static @NotNull CelestialMeta fromJson(@NotNull JsonObject object) {
        String texture_path = "/textures/environment/moon_phases.png";
        EnumCelestialCycle cycle = EnumCelestialCycle.DEFAULT;
        float scale = 1.0f;
        int number_x = 4;
        int number_y = 2;

        if (object.has("path")) {
            final JsonElement elementPath = object.get("path");
            String contentPath = JsonReadHelper.readString(elementPath);

            if (contentPath != null && !contentPath.isEmpty()) {
                if (!contentPath.startsWith("/")) {
                    contentPath = "/" + contentPath;
                }
                texture_path = contentPath;
            }
        }

        if (object.has("cycle")) {
            final JsonElement elementCycle = object.get("cycle");
            final String contentCycle = JsonReadHelper.readString(elementCycle);

            if (contentCycle != null && !contentCycle.isEmpty()) {
                final EnumCelestialCycle cycleType = EnumCelestialCycle.getType(contentCycle);
                if (cycleType != null) {
                    cycle = cycleType;
                }
            }
        }

        if (object.has("scale")) {
            final JsonElement elementScale = object.get("scale");
            final Float contentScale = JsonReadHelper.readFloat(elementScale);

            if (contentScale != null && contentScale > 0.0F && contentScale <= 128.0F) {
                scale = contentScale;
            }
        }

        if (object.has("number_x")) {
            final JsonElement elementX = object.get("number_x");
            final Integer contentX = JsonReadHelper.readInteger(elementX);

            if (contentX != null && contentX > 0) {
                number_x = contentX;
            }
        }

        if (object.has("number_y")) {
            final JsonElement elementY = object.get("number_y");
            final Integer contentY = JsonReadHelper.readInteger(elementY);

            if (contentY != null && contentY > 0) {
                number_y = contentY;
            }
        }

        return new CelestialMeta(texture_path, cycle, scale, number_x, number_y);
    }
}
