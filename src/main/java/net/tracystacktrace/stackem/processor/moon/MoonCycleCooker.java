package net.tracystacktrace.stackem.processor.moon;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.tracystacktrace.stackem.StackEm;

public final class MoonCycleCooker {

    public static class MoonMetadata {
        public final String path;
        public final EnumMoonCycle cycle;
        public final float scale;
        public final int number_x;
        public final int number_y;
        public final int total;

        public MoonMetadata(String path, EnumMoonCycle cycle, float scale, int numberX, int numberY) {
            this.path = path;
            this.cycle = cycle;
            this.scale = scale;
            this.number_x = numberX;
            this.number_y = numberY;
            this.total = numberX * numberY;
        }
    }

    public static MoonMetadata read(String content) {
        try {
            final JsonObject root = JsonParser.parseString(content).getAsJsonObject();

            String texture_path = "/textures/environment/moon_phases.png";
            EnumMoonCycle cycle = EnumMoonCycle.DEFAULT;
            float scale = 1.0f;
            int number_x = 4;
            int number_y = 2;

            if(root.has("path")) {
                String value1 = root.get("path").getAsString();
                if(!value1.isEmpty()) {
                    if(!value1.startsWith("/")) {
                        value1 = "/" + value1;
                    }
                    texture_path = value1;
                }
            }

            if(root.has("cycle")) {
                final String value2 = root.get("cycle").getAsString();
                if(!value2.isEmpty()) {
                    final EnumMoonCycle temp = EnumMoonCycle.getType(value2);
                    if(temp != null) cycle = temp;
                }
            }

            if(root.has("scale")) {
                final float value3 = root.get("scale").getAsFloat();
                if(value3 > 0.0f && value3 <= 128.0f) {
                    scale = value3;
                }
            }

            if(root.has("number_x")) {
                final int value4 = root.get("number_x").getAsInt();
                if(value4 > 0) number_x = value4;
            }

            if(root.has("number_y")) {
                final int value4 = root.get("number_y").getAsInt();
                if(value4 > 0) number_y = value4;
            }

            return new MoonMetadata(texture_path, cycle, scale, number_x, number_y);
        } catch (JsonParseException e) {
            StackEm.LOGGER.severe("Failed to process stackem.moon.json for top texturepack");
            StackEm.LOGGER.throwing("MoonCycleCooker", "read", e);
            return null;
        }
    }
}
