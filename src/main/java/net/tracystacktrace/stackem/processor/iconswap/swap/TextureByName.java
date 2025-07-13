package net.tracystacktrace.stackem.processor.iconswap.swap;

import com.google.gson.JsonObject;
import net.minecraft.common.block.icon.Icon;
import net.minecraft.common.block.icon.IconRegister;
import net.tracystacktrace.stackem.processor.iconswap.IconProcessorException;
import net.tracystacktrace.stackem.processor.iconswap.IconSwapReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TextureByName implements ISwapper {
    public static final byte EQUALS = 0;
    public static final byte EQUALS_IGNORE_CASE = 1;
    public static final byte CONTAINS = 2;
    public static final byte REGEX = 3; //todo: add regex support
    public static final byte STARTS_WITH = 4;
    public static final byte ENDS_WITH = 5;

    public final byte compareCode;
    public final String targetString;
    public final String texturePath;

    protected int priority = 0;
    protected String armorTexture = null;
    protected boolean armorEnableColor = false;
    public Icon textureIcon;

    public TextureByName(
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
            case STARTS_WITH -> {
                return itemName.startsWith(this.targetString);
            }
            case ENDS_WITH -> {
                return itemName.endsWith(this.targetString);
            }
        }

        return false;
    }

    public void registerIcon(IconRegister register) {
        this.textureIcon = register.registerIcon(this.texturePath);
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public boolean hasArmorTexture() {
        return this.armorTexture != null;
    }

    @Override
    public String getArmorTexture() {
        return this.armorTexture;
    }

    @Override
    public void setArmorTexture(@NotNull String s) {
        this.armorTexture = s;
    }

    @Override
    public void setEnableColor(boolean b) {
        this.armorEnableColor = b;
    }

    @Override
    public boolean hasColor() {
        return this.armorEnableColor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TextureByName textureByName = (TextureByName) o;
        return compareCode == textureByName.compareCode && Objects.equals(targetString, textureByName.targetString) && Objects.equals(texturePath, textureByName.texturePath);
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

    public static @NotNull TextureByName fromJson(@NotNull JsonObject object) throws IconProcessorException {
        byte compareCode = -1;
        String targetString = null;

        if (object.has("equals")) {
            targetString = object.get("equals").getAsString();
            compareCode = TextureByName.EQUALS;
        }

        if (object.has("equalsIgnoreCase")) {
            targetString = object.get("equalsIgnoreCase").getAsString();
            compareCode = TextureByName.EQUALS_IGNORE_CASE;
        }

        if (object.has("contains")) {
            targetString = object.get("contains").getAsString();
            compareCode = TextureByName.CONTAINS;
        }

        if (object.has("startsWith")) {
            targetString = object.get("startsWith").getAsString();
            compareCode = TextureByName.STARTS_WITH;
        }

        if (object.has("endsWith")) {
            targetString = object.get("endsWith").getAsString();
            compareCode = TextureByName.ENDS_WITH;
        }

        if (compareCode == -1 || targetString == null) {
            throw new IconProcessorException(IconProcessorException.INVALID_COMPARABLE_CODE);
        }

        //obtain texture path
        final String texture = IconSwapReader.obtainTexture(object);
        final Integer priority = IconSwapReader.obtainPriority(object);

        final TextureByName compiled = new TextureByName(compareCode, targetString, texture);

        if (priority != null) {
            compiled.priority = priority;
        }

        IconSwapReader.obtainArmorIfPossible(compiled, object);

        return compiled;
    }
}
