package net.tracystacktrace.stackem;

import com.fox2code.foxloader.loader.Mod;
import net.minecraft.client.Minecraft;
import net.tracystacktrace.stackem.impl.TagTexturePack;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class StackEm extends Mod {

    public static int generateRandomNDigitNumber(int digits) {
        final int min = (int) Math.pow(10, digits - 1);
        final int max = (int) (Math.pow(10, digits) - 1);
        return (int) (min + (Math.random() * ((max - min) + 1)));
    }

    public static String getRandomStackEmIdentifier() {
        return "stackem" + generateRandomNDigitNumber(5);
    }

    public static String[] processIdentifier(String input) {
        int start = input.indexOf('[');
        int end = input.indexOf(']');

        if (start == -1 || end == -1 || !input.contains("stackem")) {
            return new String[0];
        }

        return input.substring(start + 1, end).split(";");
    }

    public static String buildIdentifier(String[] input) {
        if (input == null || input.length < 1) {
            return "stackem[]";
        }
        return "stackem[" + String.join(";", input) + "]";
    }

    public static List<TagTexturePack> buildTexturePackList() {
        final File texturepacksDir = new File(Minecraft.getInstance().getMinecraftDir(), "texturepacks");

        if (!texturepacksDir.exists() || !texturepacksDir.isDirectory()) {
            return null;
        }

        final List<TagTexturePack> collector = new ArrayList<>();

        for (File file : texturepacksDir.listFiles()) {
            Object[] collect$1 = fetchDefaultObjects(file);

            if (collect$1 == null || collect$1[0] == null) {
                continue;
            }

            TagTexturePack tagTexturePack = new TagTexturePack(file, file.getName(), (String) collect$1[0], (String) collect$1[1]);

            if (collect$1[2] != null) {
                tagTexturePack.setThumbnail((java.awt.image.BufferedImage) collect$1[2]);
            }

            collector.add(tagTexturePack);
        }

        return collector;
    }

    private static Object[] fetchDefaultObjects(File file) {
        //first line, second line, thumbnail image
        Object[] objects = new Object[3];

        try {
            ZipFile zipFile = new ZipFile(file);

            ZipEntry packTxt = zipFile.getEntry("pack.txt");
            if (packTxt != null) {
                try (InputStream inputStream = zipFile.getInputStream(packTxt); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    objects[0] = formatDescriptionLine(reader.readLine());
                    objects[1] = formatDescriptionLine(reader.readLine());
                } catch (IOException ignored) {
                }
            }

            ZipEntry packPng = zipFile.getEntry("pack.png");
            if (packPng != null) {
                try (InputStream inputStream = zipFile.getInputStream(packPng)) {
                    objects[2] = ImageIO.read(inputStream);
                } catch (IOException ignored) {
                }
            }

            zipFile.close();
            return objects;
        } catch (IOException e) {
            return null;
        }
    }

    private static String formatDescriptionLine(String line) {
        if (line == null || line.length() < 34) {
            return line;
        }

        int maxLength = 34;
        int colorCodeCount = (int) line.chars().limit(maxLength - 1).filter(c -> c == '§').count();

        maxLength += colorCodeCount * 2;

        return line.length() > maxLength ? line.substring(0, maxLength) : line;
    }

}
