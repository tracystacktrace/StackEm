package net.tracystacktrace.stackem.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.client.renderer.block.TexturePackBase;
import net.minecraft.common.util.i18n.StringTranslate;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.hack.SmartHacks;
import net.tracystacktrace.stackem.impl.TagTexturePack;
import net.tracystacktrace.stackem.impl.TexturePackStacked;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiTextureStack extends GuiScreen {

    public List<TagTexturePack> sequoiaCache;

    private final String title1;
    private final String title2;
    private final String hint1;
    private final String hint2;

    private GuiTextureStackSlot slotManager;
    private GuiButton buttonMoveUp;
    private GuiButton buttonMoveDown;
    private GuiButton buttonToggle;

    public GuiTextureStack(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;

        final StringTranslate translate = StringTranslate.getInstance();
        this.title1 = translate.translateKey("stackem.gui.title");
        this.title2 = translate.translateKey("stackem.gui.folderhint");
        this.hint1 = translate.translateKey("stackem.gui.hint1");
        this.hint2 = translate.translateKey("stackem.gui.hint2");
    }

    @Override
    public void initGui() {
        this.controlList.clear();
        this.fetchCacheFromOuterworld(StackEm.processIdentifier(this.mc.gameSettings.texturePack));

        final StringTranslate translate = StringTranslate.getInstance();

        this.controlList.add(new GuiSmallButton(-1, this.width / 2 - 154, this.height - 48, translate.translateKey("stackem.gui.folder")));
        this.controlList.add(new GuiSmallButton(-2, this.width / 2 + 4, this.height - 48, translate.translateKey("stackem.gui.done")));


        this.slotManager = new GuiTextureStackSlot(this, this.width, this.height);
        this.slotManager.registerScrollButtons(7, 8);


        this.controlList.add(this.buttonToggle = new GuiButton(-5, 5, 40, 16, 16, "❌"));
        this.controlList.add(this.buttonMoveUp = new GuiButton(-3, 5, 40 + 18, 16, 16, "↑"));
        this.controlList.add(this.buttonMoveDown = new GuiButton(-4, 5, 40 + 36, 16, 16, "↓"));


        this.buttonToggle.enabled = false;
        this.buttonToggle.visible = false;
        this.buttonMoveUp.enabled = false;
        this.buttonMoveUp.visible = false;
        this.buttonMoveDown.enabled = false;
        this.buttonMoveDown.visible = false;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {

            if (button.id == -1) {
                try {
                    Desktop.getDesktop().open(new File(Minecraft.getInstance().getMinecraftDir(), "texturepacks"));
                } catch (IOException ignored) {
                }
                return;
            }

            if (button.id == -2) {
                this.pushChangesGlobally();
                this.mc.displayGuiScreen(this.parentScreen);
                return;
            }

            if (button.id == -3) {
                this.moveUpElement(slotManager.selectedIndex);
                return;
            }

            if (button.id == -4) {
                this.moveDownElement(slotManager.selectedIndex);
                return;
            }

            if (button.id == -5) {
                this.slotManager.elementClicked(slotManager.selectedIndex, true);
                return;
            }

            this.slotManager.actionPerformed(button);
        }
    }

    @Override
    public void drawScreen(float mouseX, float mouseY, float deltaTicks) {
        this.drawDefaultBackground();
        this.slotManager.drawElement(this.mc, mouseX, mouseY, deltaTicks);

        //drawCenteredString(this.fontRenderer, title1, this.width / 2, 16.0F, 16777215);
        drawCenteredString(this.fontRenderer, title2, this.width / 2 - 77, this.height - 26, 8421504);

        drawString(fontRenderer, hint1, 2, 2, 0xFFFFFFFF);
        drawString(fontRenderer, hint2, 2, 14, 0xFFFFFFFF);

        super.drawScreen(mouseX, mouseY, deltaTicks);
    }

    public void updateMoveButtonsState(int index) {
        if (sequoiaCache.get(index).isInStack()) {
            this.buttonToggle.enabled = true;
            this.buttonToggle.visible = true;
            this.buttonToggle.displayString = "❌";

            //todo fix buttons location
            //final int slotOffsetY = this.height / 2 - (this.sequoiaCache.size() * 18) - 9;
            //this.buttonMoveUp.yPosition = slotOffsetY + (36 * index);
            //this.buttonMoveDown.yPosition = slotOffsetY + 18 + (36 * index);

            this.buttonMoveUp.visible = true;
            this.buttonMoveDown.visible = true;

            this.buttonMoveUp.enabled = index > 0;
            this.buttonMoveDown.enabled = index + 1 < this.countInStackElements();
        } else {
            this.buttonToggle.enabled = true;
            this.buttonToggle.visible = true;
            this.buttonToggle.displayString = "✔";

            this.buttonMoveUp.enabled = false;
            this.buttonMoveUp.visible = false;
            this.buttonMoveDown.enabled = false;
            this.buttonMoveDown.visible = false;
        }
    }

    /* code to obtain info from outside */

    private void fetchCacheFromOuterworld(String[] previousCached) {
        List<TagTexturePack> candidates = StackEm.buildTexturePackList();
        if (candidates == null) {
            this.sequoiaCache = new ArrayList<>();
            return;
        }

        for (int i = 0; i < previousCached.length; i++) {
            String s = previousCached[i];
            for (TagTexturePack q : candidates) {
                if (q.name.equals(s)) {
                    q.order = i;
                }
            }
        }

        this.sequoiaCache = candidates;
        this.pushSequoiaCacheSort();
    }

    /* slot interactive code */

    public int getSequoiaCacheSize() {
        return sequoiaCache.size();
    }

    public void pushSequoiaCacheSort() {
        sequoiaCache.sort((o1, o2) -> {
            if (o1.isInStack() && o2.isInStack()) return Integer.compare(o1.order, o2.order);
            if (o1.isInStack()) return -1;
            if (o2.isInStack()) return 1;
            return o1.name.compareTo(o2.name);
        });
    }

    public void recalculateStack() {
        for (int i = 0; i < sequoiaCache.size(); i++) {
            if (sequoiaCache.get(i).isInStack()) {
                sequoiaCache.get(i).order = i;
            }
            //skip
            if (sequoiaCache.get(i).order == -1) {
                break;
            }
        }
    }

    public TagTexturePack getSequoiaCacheElement(int index) {
        return sequoiaCache.get(index);
    }

    public boolean isSequoiaCacheElementInStack(int index) {
        return sequoiaCache.get(index).isInStack();
    }

    public int countInStackElements() {
        return (int) sequoiaCache.stream().filter(TagTexturePack::isInStack).count();
    }

    public void addElementToStack(int index) {
        sequoiaCache.get(index).order = countInStackElements();
        this.pushSequoiaCacheSort();
    }

    public void removeElementFromStack(int index) {
        sequoiaCache.get(index).order = -1;
        this.pushSequoiaCacheSort();
        this.recalculateStack();
    }

    public void pushChangesGlobally() {
        this.pushSequoiaCacheSort();
        List<File> files = new ArrayList<>();
        List<String> stackemList = new ArrayList<>();

        for (int i = 0; i < this.countInStackElements(); i++) {
            files.add(this.sequoiaCache.get(i).file);
            stackemList.add(this.sequoiaCache.get(i).name);
        }

        final TexturePackStacked stacked = new TexturePackStacked("stackem", SmartHacks.getDefaultTexturePack(), files);

        this.mc.texturePackList.setTexturePack(stacked);
        this.mc.gameSettings.texturePack = StackEm.buildIdentifier(stackemList.toArray(new String[0]));
        this.mc.gameSettings.saveOptions();
        this.mc.renderEngine.refreshTextures();
        this.mc.renderGlobal.loadRenderers();
        this.mc.sndManager.refreshSounds(this.mc.texturePackList.getSelectedTexturePack());
        this.mc.fontRenderer = new FontRenderer((TexturePackBase) this.mc.texturePackList.getSelectedTexturePack(), this.mc.renderEngine);

        Display.update();
    }

    public void moveUpElement(int index) {
        sequoiaCache.get(index - 1).order = index;
        sequoiaCache.get(index).order = index - 1;
        this.pushSequoiaCacheSort();
        slotManager.elementClicked(index - 1, false);
    }

    public void moveDownElement(int index) {
        sequoiaCache.get(index + 1).order = index;
        sequoiaCache.get(index).order = index + 1;
        this.pushSequoiaCacheSort();
        slotManager.elementClicked(index + 1, false);
    }
}
