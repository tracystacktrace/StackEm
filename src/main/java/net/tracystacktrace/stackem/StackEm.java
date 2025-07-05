package net.tracystacktrace.stackem;

import com.fox2code.foxevents.EventHandler;
import com.fox2code.foxloader.event.client.GuiItemInfoEvent;
import com.fox2code.foxloader.loader.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.common.util.i18n.StringTranslate;
import net.tracystacktrace.stackem.impl.DeepMeta;
import net.tracystacktrace.stackem.impl.TagTexturePack;
import net.tracystacktrace.stackem.impl.TexturePackStacked;
import net.tracystacktrace.stackem.processor.category.DescriptionFileCooker;
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

    public static boolean DEBUG_DISABLE = false;
    public static final Logger LOGGER = Logger.getLogger("STACKEM");

    public static TexturePackStacked getContainerInstance() {
        return (TexturePackStacked) Minecraft.getInstance().texturePackList.getSelectedTexturePack();
    }

    public static DeepMeta getContainerDeepMeta() {
        return getContainerInstance().getDeepMeta();
    }

    public static void toggleDebug() {
        final Minecraft mc = Minecraft.getInstance();

        // handle safely
        if (mc.thePlayer == null) {
            return;
        }

        DEBUG_DISABLE = !DEBUG_DISABLE;
        mc.renderEngine.refreshTextures();
        mc.thePlayer.addChatMessage(StringTranslate.getInstance().translateKey(DEBUG_DISABLE ? "stackem.debug.on" : "stackem.debug.off"));
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

        final List<TagTexturePack> collector = new ArrayList<>();

        for (File file : texturepacksDir.listFiles()) {
            if(file.isDirectory() || !file.getName().toLowerCase().endsWith(".zip")) {
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
                DescriptionFileCooker.read(tagTexturePack, stackemJsonContent);
            }

            zipFile.close();
            return tagTexturePack;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * @deprecated Remove this before release or change the behaviour
     */
    @Deprecated
    @EventHandler
    public void shitTestRemoveMe(GuiItemInfoEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            event.addDescriptionLine("§8[meta/max]: " + event.getItemStack().getItemDamage() + "/" + event.getItemStack().getMaxDamage());
        }
    }

}
