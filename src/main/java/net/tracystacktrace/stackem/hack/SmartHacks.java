package net.tracystacktrace.stackem.hack;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.renderer.block.ITexturePack;
import net.minecraft.client.renderer.block.TexturePackList;
import net.minecraft.client.renderer.world.RenderEngine;

import java.lang.reflect.Field;

public final class SmartHacks {

    public static ITexturePack getDefaultTexturePack() {
        try {
            Field field = TexturePackList.class.getDeclaredField("defaultTexturePack");
            field.setAccessible(true);
            return (ITexturePack) field.get(null);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object2IntOpenHashMap<String> getTextureMap(RenderEngine renderEngine) {
        try {
            Field field = RenderEngine.class.getDeclaredField("textureMap");
            field.setAccessible(true);
            return (Object2IntOpenHashMap<String>) field.get(renderEngine);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
