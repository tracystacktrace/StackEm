package net.tracystacktrace.stackem.processor.itemstackicon.swap;

import com.google.gson.JsonObject;
import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TextureOnName {
    public static final byte EQUALS = 0;
    public static final byte EQUALS_IGNORE_CASE = 1;
    public static final byte CONTAINS = 2;
    public static final byte REGEX = 3;

    public final byte compareCode;
    public final String targetString;
    public final String texturePath;

    public Icon textureIcon;

    public TextureOnName(
            byte compareCode,
            @NotNull String targetString,
            @NotNull String texturePath
    ) {
        this.compareCode = compareCode;
        this.targetString = targetString;
        this.texturePath = texturePath;
    }

    public boolean compareString(@Nullable String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            return false;
        }

        switch (this.compareCode) {
            case EQUALS -> {
                return itemName.equals(this.targetString);
            }
            case EQUALS_IGNORE_CASE -> {
                return itemName.equalsIgnoreCase(this.targetString);
            }
            case CONTAINS -> {
                return itemName.contains(this.targetString);
            }
        }

        return false;
    }

    public void registerIcon(IconRegister register) {
        this.textureIcon = register.registerIcon(this.texturePath);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TextureOnName textureOnName = (TextureOnName) o;
        return compareCode == textureOnName.compareCode && Objects.equals(targetString, textureOnName.targetString) && Objects.equals(texturePath, textureOnName.texturePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compareCode, targetString, texturePath);
    }

    @Override
    public String toString() {
        return "TextureOnName{" +
                "compareCode=" + compareCode +
                ", targetString='" + targetString + '\'' +
                ", texturePath='" + texturePath + '\'' +
                ", textureIcon=" + textureIcon +
                '}';
    }

    public static @NotNull TextureOnName fromJson(@NotNull JsonObject object) throws IllegalArgumentException {
        if (!object.has("equals") && !object.has("equalsIgnoreCase") && !object.has("contains")) {
            throw new IllegalArgumentException("Item texture swap builder error! Cannot find correct [compareCode] for: " + object);
        }

        byte compareCode = -1;
        String targetString = null;

        if (object.has("equals")) {
            targetString = object.get("equals").getAsString();
            compareCode = TextureOnName.EQUALS;
        }

        if (object.has("equalsIgnoreCase")) {
            targetString = object.get("equalsIgnoreCase").getAsString();
            compareCode = TextureOnName.EQUALS_IGNORE_CASE;
        }

        if (object.has("contains")) {
            targetString = object.get("contains").getAsString();
            compareCode = TextureOnName.CONTAINS;
        }

        if (compareCode == -1 || targetString == null) {
            throw new IllegalArgumentException("Item texture swap builder error! Failed to fetch required arguments from: " + object);
        }

        if (!object.has("texture")) {
            throw new IllegalArgumentException("Item texture swap builder error! Failed to fetch [texture] for: " + object);
        }

        final String texture = object.get("texture").getAsString();
        return new TextureOnName(compareCode, targetString, texture);
    }
}
