package net.tracystacktrace.stackem.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.common.util.i18n.StringTranslate;
import net.tracystacktrace.stackem.StackEm;
import net.tracystacktrace.stackem.hack.SmartHacks;
import net.tracystacktrace.stackem.hack.SoundCleanupHelper;
import net.tracystacktrace.stackem.impl.TagTexturePack;
import net.tracystacktrace.stackem.impl.TexturePackStacked;
import net.tracystacktrace.stackem.tools.QuickRNG;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class GuiTextureStack extends GuiScreen {

    public List<TagTexturePack> sequoiaCache;

    private final String hint1;
    private final String hint2;
    private final String actions;

    private GuiTextureStackSlot slotManager;
    private GuiButton buttonMoveUp;
    private GuiButton buttonMoveDown;
    private GuiButton buttonToggle;
    private GuiButton buttonWebsite;

    private boolean clickedAtLeastOnce;

    public GuiTextureStack(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;

        final StringTranslate translate = StringTranslate.getInstance();
        this.hint1 = translate.translateKey("stackem.gui.hint1");
        this.hint2 = translate.translateKey("stackem.gui.hint2");
        this.actions = translate.translateKey("stackem.gui.actions");
    }

    @Override
    public void initGui() {
        this.clickedAtLeastOnce = false;
        this.controlList.clear();
        this.fetchCacheFromOuterworld(StackEm.unpackSaveString(this.mc.gameSettings.texturePack));

        final StringTranslate translate = StringTranslate.getInstance();

        // texture pack folder
        final GuiButton openFolder = new GuiButton(-1, this.width - 120, this.height - 25, 20, 20, "§9☄", translate.translateKey("stackem.gui.folder"));
        openFolder.canDisplayInfo = true;
        this.controlList.add(openFolder);

        // save & close
        this.controlList.add(new GuiButton(-2, this.width - 95, this.height - 25, 90, 20, translate.translateKey("stackem.gui.done")));

        // slot manager
        this.slotManager = new GuiTextureStackSlot(this, this.width, this.height);
        this.slotManager.registerScrollButtons(7, 8);

        // action buttons
        this.controlList.add(this.buttonToggle = new GuiButton(-5, 5, 20, 16, 16, "§4❌", translate.translateKey("stackem.button.remove")));
        this.controlList.add(this.buttonWebsite = new GuiButton(-6, 5, 20 + 18, 16, 16, "§bℹ", translate.translateKey("stackem.button.website")));
        this.controlList.add(this.buttonMoveDown = new GuiButton(-4, 5 + 18, 20 + 18, 16, 16, "§9↓", translate.translateKey("stackem.button.movedown")));
        this.controlList.add(this.buttonMoveUp = new GuiButton(-3, 5 + 18, 20, 16, 16, "§9↑", translate.translateKey("stackem.button.moveup")));


        this.buttonToggle.enabled = false;
        this.buttonToggle.visible = false;
        this.buttonToggle.canDisplayInfo = true;
        this.buttonMoveUp.enabled = false;
        this.buttonMoveUp.visible = false;
        this.buttonMoveUp.canDisplayInfo = true;
        this.buttonMoveDown.enabled = false;
        this.buttonMoveDown.visible = false;
        this.buttonMoveDown.canDisplayInfo = true;
        this.buttonWebsite.enabled = false;
        this.buttonWebsite.visible = false;
        this.buttonWebsite.canDisplayInfo = true;
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

            if (button.id == -6) {
                final String website = sequoiaCache.get(this.slotManager.selectedIndex).getWebsite();
                if (StackEm.isValidWebsite(website)) {
                    try {
                        Desktop.getDesktop().browse(new URI(website));
                    } catch (IOException | URISyntaxException ignored) {
                    }
                }
                return;
            }

            this.slotManager.actionPerformed(button);
        }
    }

    @Override
    public void drawScreen(float mouseX, float mouseY, float deltaTicks) {
        this.drawDefaultBackground();
        this.slotManager.drawElement(this.mc, mouseX, mouseY, deltaTicks);

        if (this.clickedAtLeastOnce) {
            drawString(fontRenderer, actions, 5, 7, 0xFFFFFFFF);
        }

        drawString(fontRenderer, hint1, 3, this.height - 14, 0xFFFFFFFF);
        drawString(fontRenderer, hint2, 3, this.height - 26, 0xFFFFFFFF);

        super.drawScreen(mouseX, mouseY, deltaTicks);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        this.sequoiaCache.forEach(p -> p.removeThumbnail(mc.renderEngine));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.slotManager.tickAnimation();
    }

    public void updateMoveButtonsState(int index) {
        this.clickedAtLeastOnce = true;
        if (sequoiaCache.get(index).isInStack()) {
            this.buttonToggle.enabled = true;
            this.buttonToggle.visible = true;
            this.buttonToggle.displayString = "§4❌";
            this.buttonToggle.displayInfo = StringTranslate.getInstance().translateKey("stackem.button.remove");

            //todo fix buttons location
            //final int slotOffsetY = this.height / 2 - (this.sequoiaCache.size() * 18) - 9;
            //this.buttonMoveUp.yPosition = slotOffsetY + (36 * index);
            //this.buttonMoveDown.yPosition = slotOffsetY + 18 + (36 * index);

            this.buttonMoveUp.visible = true;
            this.buttonMoveDown.visible = true;
            this.buttonWebsite.visible = true;

            this.buttonMoveUp.enabled = index > 0;
            this.buttonMoveDown.enabled = index + 1 < this.countInStackElements();

        } else {
            this.buttonToggle.enabled = true;
            this.buttonToggle.visible = true;
            this.buttonToggle.displayString = "§a✔";
            this.buttonToggle.displayInfo = StringTranslate.getInstance().translateKey("stackem.button.add");

            this.buttonMoveUp.enabled = false;
            this.buttonMoveUp.visible = false;
            this.buttonMoveDown.enabled = false;
            this.buttonMoveDown.visible = false;
            this.buttonWebsite.visible = true;

            this.buttonWebsite.displayInfo = StringTranslate.getInstance().translateKey("stackem.button.website.0");
        }

        //info button process
        final TagTexturePack pack = sequoiaCache.get(index);
        this.buttonWebsite.enabled = StackEm.isValidWebsite(pack.getWebsite());

        if (pack.hasAuthor() && pack.hasWebsite()) {
            this.buttonWebsite.displayInfo = StringTranslate.getInstance().translateKeyFormat("stackem.button.website.2", pack.getAuthor());
        } else if (pack.hasAuthor()) {
            this.buttonWebsite.displayInfo = StringTranslate.getInstance().translateKeyFormat("stackem.button.website.1", pack.getAuthor());
        } else {
            this.buttonWebsite.displayInfo = StringTranslate.getInstance().translateKey("stackem.button.website.0");
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
        this.sequoiaCache.forEach(TagTexturePack::buildCategory);
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

        StackEm.DEBUG_DISABLE = false;
        StackEm.getContainerInstance().deleteTexturePack(mc.renderEngine);

        final TexturePackStacked stacked = new TexturePackStacked(QuickRNG.getRandomIdentifier(), SmartHacks.getDefaultTexturePack(), files);

        this.mc.texturePackList.setTexturePack(stacked);
        this.mc.gameSettings.texturePack = StackEm.packSaveString(stackemList.toArray(new String[0]));
        this.mc.gameSettings.saveOptions();

        this.mc.renderEngine.refreshTextures();
        this.mc.renderGlobal.loadRenderers();
        this.mc.fontRenderer = new FontRenderer(StackEm.getContainerInstance(), this.mc.renderEngine);

        SoundCleanupHelper.cleanupSoundSources(this.mc.sndManager);
        this.mc.sndManager.refreshSounds(stacked);
        this.mc.sndManager.onSoundOptionsChanged();

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
