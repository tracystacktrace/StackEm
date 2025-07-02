package net.tracystacktrace.stackem.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.common.util.i18n.StringTranslate;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.impl.TexturePackStacked;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class GuiCharMap extends GuiScreen {

    private static final String[] FONT_IDENTIFIERS = new String[] {
            "default.png - ascii",
            "font_01.png - accented (1)",
            "font_02.png - nonlatin european (1)",
            "font_03.png - accented (2)",
            "font_04.png - nonlatin european (2)",
            "font_05.png - accented (3)",
            "font_06.png - nonlatin european (3)",
            "font_07.png - accented (4)",
            "font_08.png - nonlatin european (4)",
            "font_09.png - accented (5)",
            "font_0A.png - dla"
    };

    private final GuiButton[] charmapButtons = new GuiButton[16 * 16];
    private final char[][] charmapSets = new char[FONT_IDENTIFIERS.length][];

    private String titlePage = "";
    private GuiButton buttonLeft;
    private GuiButton buttonRight;
    private byte index = 0;

    public GuiCharMap(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        this.fetchFontTxtFile();
    }

    @Override
    public void initGui() {
        this.controlList.clear();

        final int c_offset_x = this.width - (14 * 16) - 35;
        final int c_offset_y = this.height / 2 - (14 * 16 / 2);

        for(int i = 0; i < this.charmapButtons.length; i++) {
            this.controlList.add(this.charmapButtons[i] = new GuiButton(i, c_offset_x + (i % 16) * 14, c_offset_y + (i / 16) * 14, 14, 14, ""));
            this.charmapButtons[i].displayInfo = "";
            this.charmapButtons[i].canDisplayInfo = true;
        }

        this.controlList.add(this.buttonLeft = new GuiButton(-1, 5, this.height - 50, 40, 20, "<"));
        this.controlList.add(this.buttonRight = new GuiButton(-2, 45, this.height - 50, 40, 20, ">"));
        this.controlList.add(new GuiButton(-3, 5, this.height - 25, 80, 20, StringTranslate.getInstance().translateKey("stackem.charmap.close")));

        this.moveToPage(this.index);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if(guiButton.enabled) {
            if(guiButton.id == -1) {
                if(this.index > 0) {
                    this.index--;
                    this.moveToPage(this.index);
                }
                return;
            }

            if(guiButton.id == -2) {
                if(this.index + 1 < FONT_IDENTIFIERS.length) {
                    this.index++;
                    this.moveToPage(this.index);
                }
                return;
            }

            if(guiButton.id == -3) {
                this.mc.displayGuiScreen(this.parentScreen);
                return;
            }


            if(guiButton.id >= 0 && guiButton.id < 256) {

            }
        }
    }

    @Override
    public void drawScreen(float mouseX, float mouseY, float deltaTicks) {
        this.drawDefaultBackground();

        fontRenderer.drawString("CharMap", 5, 11, 0xFFFFFFFF);
        fontRenderer.drawString(this.titlePage, 5, 23, 0xFFFFFFFF);
        fontRenderer.drawString(FONT_IDENTIFIERS[index], 5, 47, 0xFFFFFFFF);

        super.drawScreen(mouseX, mouseY, deltaTicks);
    }


    private void fetchFontTxtFile() {
        TexturePackStacked stacked = StackEm.getContainerInstance();
        try (InputStream inputStream = stacked.getResourceAsStream("/font.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            final char[] chars = reader.lines().filter(e -> !e.isEmpty() && !e.startsWith("#")).collect(Collectors.joining()).toCharArray();

            for(int i = 0; i < FONT_IDENTIFIERS.length; i++) {
                this.charmapSets[i] = new char[256];
                System.arraycopy(chars, 256 * i, this.charmapSets[i], 0, 256);
            }

        } catch (IOException e) {
            StackEm.LOGGER.severe("Couldn't read file: /font.txt");
            StackEm.LOGGER.throwing("GuiCharMap", "fetchFontTxtFile", e);
        }
    }

    private void moveToPage(int i) {
        this.buttonLeft.enabled = i > 0;
        this.buttonRight.enabled = (i + 1) < this.charmapSets.length;

        this.titlePage = StringTranslate.getInstance().translateKeyFormat("stackem.charmap.page", (i+1), FONT_IDENTIFIERS.length);
        final char[] lol = this.charmapSets[i];
        for(int b = 0; b < 256; b++) {
            this.charmapButtons[b].displayInfo = String.format("\\u%04x", (int) lol[b]);
            this.charmapButtons[b].displayString = Character.toString(lol[b]);
        }
    }
}
