package net.tracystacktrace.stackem.processor.moon;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.impl.TexturePackStacked;
import net.tracystacktrace.stackem.processor.IJam;
import org.jetbrains.annotations.NotNull;

public class JamSunTexture implements IJam {

    @Override
    public @NotNull String getPath() {
        return "/stackem.sun.json";
    }

    @Override
    public void process(@NotNull TexturePackStacked stacked, @NotNull String rawJsonContent) {
        try {
            final JsonObject root = JsonParser.parseString(rawJsonContent).getAsJsonObject();

            String texture_path = "/textures/environment/sun.png";
            EnumCelestialCycle cycle = EnumCelestialCycle.STATIC;
            float scale = 1.0f;
            int number_x = 1;
            int number_y = 1;

            if (root.has("path")) {
                String value1 = root.get("path").getAsString();
                if (!value1.isEmpty()) {
                    if (!value1.startsWith("/")) {
                        value1 = "/" + value1;
                    }
                    texture_path = value1;
                }
            }

            if (root.has("cycle")) {
                final String value2 = root.get("cycle").getAsString();
                if (!value2.isEmpty()) {
                    final EnumCelestialCycle temp = EnumCelestialCycle.getType(value2);
                    if (temp != null) cycle = temp;
                }
            }

            if (root.has("scale")) {
                final float value3 = root.get("scale").getAsFloat();
                if (value3 > 0.0f && value3 <= 128.0f) {
                    scale = value3;
                }
            }

            if (root.has("number_x")) {
                final int value4 = root.get("number_x").getAsInt();
                if (value4 > 0) number_x = value4;
            }

            if (root.has("number_y")) {
                final int value4 = root.get("number_y").getAsInt();
                if (value4 > 0) number_y = value4;
            }

            stacked.getDeepMeta().setSunData(new CelestialMeta(texture_path, cycle, scale, number_x, number_y));
        } catch (JsonParseException e) {
            StackEm.LOGGER.severe("Failed to process stackem.sun.json for top texturepack");
            StackEm.LOGGER.throwing("JamSunTexture", "read", e);
        }
    }

}
