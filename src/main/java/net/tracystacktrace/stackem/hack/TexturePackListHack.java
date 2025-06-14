package net.tracystacktrace.stackem.hack;

import net.minecraft.client.renderer.block.ITexturePack;
import net.minecraft.client.renderer.block.TexturePackList;

import java.lang.reflect.Field;

public class TexturePackListHack {

    public static ITexturePack getDefaultTexturePack() {
        try {
            Field field = TexturePackList.class.getDeclaredField("defaultTexturePack");
            field.setAccessible(true);
            return (ITexturePack) field.get(null);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
