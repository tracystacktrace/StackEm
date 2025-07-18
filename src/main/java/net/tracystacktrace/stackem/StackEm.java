package net.tracystacktrace.stackem;

import com.fox2code.foxevents.EventHandler;
import com.fox2code.foxloader.client.KeyBindingAPI;
import com.fox2code.foxloader.event.client.GuiItemInfoEvent;
import com.fox2code.foxloader.loader.Mod;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.KeyBinding;
import net.minecraft.common.util.i18n.StringTranslate;
import net.tracystacktrace.stackem.impl.DeepMeta;
import net.tracystacktrace.stackem.impl.TagTexturePack;
import net.tracystacktrace.stackem.impl.TexturePackStacked;
import net.tracystacktrace.stackem.modifications.category.StackemJsonReader;
import net.tracystacktrace.stackem.tools.IOReadHelper;
import net.tracystacktrace.stackem.tools.ZipFileHelper;
import org.lwjgl.input.Keyboard;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipFile;

public class StackEm extends Mod {
    public static final StackEmConfig CONFIG = new StackEmConfig();
    public static final Logger LOGGER = Logger.getLogger("STACKEM");
    public static final KeyBinding DEBUG_KEYBIND_FALLBACK = new KeyBinding("stackem.keybind.fallback", Keyboard.KEY_F10);
    public static boolean DEBUG_FORCE_FALLBACK = false;

    public static TexturePackStacked getContainerInstance() {
        return (TexturePackStacked) Minecraft.getInstance().texturePackList.getSelectedTexturePack();
    }

    public static DeepMeta getContainerDeepMeta() {
        return getContainerInstance().getDeepMeta();
    }

    public static void toggleDefaultTextures() {
        final Minecraft mc = Minecraft.getInstance();

        // handle safely
        if (mc.thePlayer == null) {
            return;
        }

        StackEm.DEBUG_FORCE_FALLBACK = !StackEm.DEBUG_FORCE_FALLBACK;
        mc.renderEngine.refreshTextures();
        mc.thePlayer.addChatMessage(StringTranslate.getInstance().translateKey(StackEm.DEBUG_FORCE_FALLBACK ? "stackem.debug.on" : "stackem.debug.off"));
    }

    public static String[] unpackSaveString(String input) {
        final int start = input.indexOf('[');
        final int end = input.indexOf(']');

        if (start == -1 || end == -1 || !input.contains("stackem")) {
            return new String[0];
        }

        return input.substring(start + 1, end).split(";");
    }

    public static String packSaveString(String[] input) {
        if (input == null || input.length < 1) {
            return "stackem[]";
        }
        return "stackem[" + String.join(";", input) + "]";
    }

    public static boolean isValidWebsite(String website) {
        return website != null && (website.startsWith("https://") || website.startsWith("http://"));
    }

    public static List<TagTexturePack> fetchTexturepackList() {
        final File texturepacksDir = new File(Minecraft.getInstance().getMinecraftDir(), "texturepacks");

        if (!texturepacksDir.exists() || !texturepacksDir.isDirectory()) {
            return null;
        }

        final File[] filesArray = texturepacksDir.listFiles();

        if (filesArray == null || filesArray.length == 0) {
            return null;
        }

        final List<TagTexturePack> collector = new ArrayList<>();

        for (File file : filesArray) {
            if (file.isDirectory() || !file.getName().toLowerCase().endsWith(".zip")) {
                continue;
            }

            final TagTexturePack tagTexturePack = fetchTexturepackFromZip(file);
            if (tagTexturePack != null) {
                collector.add(tagTexturePack);
            }
        }

        return collector;
    }

    private static TagTexturePack fetchTexturepackFromZip(File file) {
        //first line, second line, thumbnail image
        try (final ZipFile zipFile = new ZipFile(file)) {
            //pack.txt of two strings
            final String[] packTxtContent = ZipFileHelper.readTextFile(zipFile, "pack.txt", reader -> {
                final String line1 = reader.readLine();
                final String line2 = reader.readLine();
                return new String[]{line1, line2};
            });

            if (packTxtContent == null) {
                return null;
            }

            //safely handle data
            if (packTxtContent[0] == null || packTxtContent[0].isEmpty()) {
                packTxtContent[0] = "";
            }

            if (packTxtContent[1] == null || packTxtContent[1].isEmpty()) {
                packTxtContent[1] = "";
            }

            //build an instance
            final TagTexturePack tagTexturePack = new TagTexturePack(file, file.getName(), packTxtContent[0], packTxtContent[1]);

            //2 - pack.png image (BufferedImage)
            final BufferedImage packPngImage = ZipFileHelper.readImage(zipFile, "pack.png");
            if (packPngImage != null) {
                tagTexturePack.setThumbnail(packPngImage);
            }

            //3 - stackem.json
            final String stackemJsonContent = ZipFileHelper.readTextFile(zipFile, "stackem.json");
            if (stackemJsonContent != null) {
                try {
                    final JsonObject object_1 = IOReadHelper.processJson(stackemJsonContent);
                    StackemJsonReader.pushJson(object_1, tagTexturePack);
                } catch (IOReadHelper.CustomIOException e) {
                    StackEm.LOGGER.severe("Failed to process texturepack descriptor file (stackem.json)!");
                    StackEm.LOGGER.throwing("StackEm", "fetchTexturepackFromZip", e);
                    e.printStackTrace();
                }
            }

            zipFile.close();
            return tagTexturePack;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void onPreInit() {
        this.setConfigObject(CONFIG);
        KeyBindingAPI.registerKeyBinding(DEBUG_KEYBIND_FALLBACK);
    }

    @EventHandler
    public void eventProvideMetadataInfo(GuiItemInfoEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            event.addDescriptionLine("§8[meta/max]: " + event.getItemStack().getItemDamage() + "/" + event.getItemStack().getMaxDamage());
        }
    }
}
