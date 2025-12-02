package net.tracystacktrace.stackem.neptune.fetch;

import net.tracystacktrace.stackem.neptune.container.PreviewTexturePack;
import net.tracystacktrace.stackem.tools.NeptuneProperties;
import net.tracystacktrace.stackem.tools.SafetyTools;
import net.tracystacktrace.stackem.tools.ZipFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FetchMaster {
    public static @Nullable PreviewTexturePack buildPreview(@NotNull File texturepackFile) {
        // Check for basic existence of the file
        if (!texturepackFile.exists() || !texturepackFile.isFile()) {
            return null;
        }

        // Check for file being locked
        if (SafetyTools.isFileLocked(texturepackFile)) {
            return null;
        }

        // Calculate the SHA-256 of texturepack
        String sha256;
        try {
            sha256 = SafetyTools.getSHA256(texturepackFile);
        } catch (IOException e) {
            sha256 = "SHA-256 NOT CALCULATED!!!";
        }

        // Read the inside of the zip file
        try (final ZipFile zipFile = new ZipFile(texturepackFile)) {
            // Get pack.txt description lines
            final String[] packTxtContent = ZipFileHelper.readTextFile(zipFile, "pack.txt", reader -> {
                final String line1 = reader.readLine();
                final String line2 = reader.readLine();
                return new String[]{line1, line2};
            });

            // If pack.txt is empty - ignore
            if (packTxtContent == null) {
                return null;
            }

            // Safely handle empty strings of pack.txt
            if (packTxtContent[0] == null || packTxtContent[0].isEmpty())
                packTxtContent[0] = "";
            if (packTxtContent[1] == null || packTxtContent[1].isEmpty())
                packTxtContent[1] = "";

            // Construct a preview instance
            final PreviewTexturePack pack = new PreviewTexturePack(
                    texturepackFile,
                    texturepackFile.getName(),
                    packTxtContent[0],
                    packTxtContent[1],
                    sha256
            );

            // Try to obtain pack.png image (BufferedImage)
            final BufferedImage packPngImage = ZipFileHelper.readImage(zipFile, "pack.png");
            if (packPngImage != null) {
                pack.setIcon(packPngImage);
            }

            // Try to read stackem.properties
            FetchMaster.setPossibleProperties(zipFile, pack);

            zipFile.close();
            return pack;
        } catch (IOException e) {
            System.out.printf("Failed to process \"%s\" texturepack, reason: %s\n", texturepackFile.getName(), e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static void setPossibleProperties(@NotNull ZipFile zipFile, @NotNull PreviewTexturePack pack) {
        final ZipEntry stackemCfgZipEntry = zipFile.getEntry("stackem.json");
        if (stackemCfgZipEntry != null) {
            try {
                final InputStream stackemCfgInputStream = zipFile.getInputStream(stackemCfgZipEntry);
                final NeptuneProperties properties = new NeptuneProperties();
                properties.open(stackemCfgInputStream);
                stackemCfgInputStream.close();

                final String target_game = properties.getString("target_game");
                final String website = properties.getString("website");
                final String[] authors = properties.getStringArray("authors");
                final String[] category = properties.getStringArray("category");
                final String[] custom_category = properties.getStringArray("custom_category");

            } catch (IOException e) {
                System.out.printf("Failed to process [stackem.properties] of %s, reason: %s\n", pack.getName(), e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
