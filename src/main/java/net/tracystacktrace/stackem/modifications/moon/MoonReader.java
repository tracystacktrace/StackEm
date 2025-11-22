package net.tracystacktrace.stackem.modifications.moon;

import com.google.gson.JsonObject;
import net.tracystacktrace.stackem.tools.json.JsonExtractionException;
import net.tracystacktrace.stackem.tools.json.ThrowingJson;
import org.jetbrains.annotations.NotNull;

public final class MoonReader {
    public static @NotNull CelestialMeta fromJson(
            @NotNull final JsonObject object,
            @NotNull final String sourceName
    ) throws JsonExtractionException {
        String texture_path = "/textures/environment/moon_phases.png";
        EnumCelestialCycle cycle = EnumCelestialCycle.DEFAULT;
        float scale = 1.0f;
        int number_x = 4;
        int number_y = 2;

        if (object.has("path")) {
            String contentPath = ThrowingJson.cautiouslyGetString(object, "path", sourceName);
            if (!contentPath.isEmpty()) {
                if (!contentPath.startsWith("/")) {
                    contentPath = "/" + contentPath;
                }
                texture_path = contentPath;
            }
        }

        if (object.has("cycle")) {
            final String contentCycle = ThrowingJson.cautiouslyGetString(object, "cycle", sourceName);
            if (!contentCycle.isEmpty()) {
                final EnumCelestialCycle cycleType = EnumCelestialCycle.getType(contentCycle);
                if (cycleType != null) {
                    cycle = cycleType;
                }
            }
        }

        if (object.has("scale")) {
            final float contentScale = ThrowingJson.cautiouslyGetFloat(object, "scale", "null"); //TODO: remove null
            if (contentScale > 0.0F && contentScale <= 128.0F) {
                scale = contentScale;
            }
        }

        if (object.has("number_x")) {
            final int contentX = ThrowingJson.cautiouslyGetInt(object, "number_x", "null"); //TODO: remove null
            if (contentX > 0) {
                number_x = contentX;
            }
        }

        if (object.has("number_y")) {
            final int contentY = ThrowingJson.cautiouslyGetInt(object, "number_y", "null"); //TODO: remove null
            if (contentY > 0) {
                number_y = contentY;
            }
        }

        return new CelestialMeta(texture_path, cycle, scale, number_x, number_y);
    }
}
