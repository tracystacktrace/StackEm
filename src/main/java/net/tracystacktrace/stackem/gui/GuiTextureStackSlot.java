package net.tracystacktrace.stackem.gui;

import com.indigo3d.util.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.world.Tessellator;
import net.tracystacktrace.stackem.impl.TagTexturePack;
import net.tracystacktrace.stackem.tools.StringFeatures;

public class GuiTextureStackSlot extends GuiSlot {

    protected GuiTextureStack parentScreen;
    public int selectedIndex = -1;

    private byte animationClock;
    private final boolean[] descriptionIndex;
    private final byte[] animationIndex;
    private final byte[] maxAnimationIndex;

    public GuiTextureStackSlot(GuiTextureStack parentScreen, int width, int height) {
        super(width, height, 0, height - 30, 36, 320);
        this.parentScreen = parentScreen;

        this.animationIndex = new byte[this.getSize()];
        this.maxAnimationIndex = new byte[this.getSize()];
        this.descriptionIndex = new boolean[this.getSize()];
        for (int i = 0; i < this.animationIndex.length; i++) {
            TagTexturePack tag = parentScreen.getSequoiaCacheElement(i);
            this.maxAnimationIndex[i] = tag.hasCategories() ? (byte) tag.getBakedCSS().length : 0;
        }
    }

    public void tickAnimation() {
        if (animationClock == 40) {
            for (int i = 0; i < animationIndex.length; i++) {
                if (maxAnimationIndex[i] == 0) {
                    continue;
                }

                animationIndex[i]++;
                if (animationIndex[i] == maxAnimationIndex[i]) {
                    animationIndex[i] = 0;
                }
            }
            for (int i = 0; i < this.descriptionIndex.length; i++) {
                this.descriptionIndex[i] = !this.descriptionIndex[i];
            }
            animationClock = 0;
        } else {
            animationClock++;
        }
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        this.selectedIndex = index;
        if (doubleClick) {
            if (parentScreen.isSequoiaCacheElementInStack(index)) {
                parentScreen.removeElementFromStack(index);
            } else {
                parentScreen.addElementToStack(index);
            }
        }
        parentScreen.updateMoveButtonsState(index);

        final TagTexturePack tag = parentScreen.getSequoiaCacheElement(index);
        this.animationIndex[index] = 0;
        this.animationClock = 0;
        if (tag.hasCategories()) {
            this.maxAnimationIndex[index] = (byte) tag.getBakedCSS().length;
        }
    }

    @Override
    protected void drawSlot(Minecraft minecraft, int index, float x, float y, int iconHeight, Tessellator tessellator) {
        final TagTexturePack tag = parentScreen.getSequoiaCacheElement(index);
        final boolean isSelectedOne = this.selectedIndex == index;

        if (tag.hasThumbnail()) {
            tag.bindThumbnail(minecraft.renderEngine);
        } else {
            minecraft.renderEngine.bindTexture(minecraft.renderEngine.getTexture("/textures/gui/unknown_pack.png"));
        }

        if (tag.isInStack()) {
            this.drawGradientRect(x, y, x + 316, y + 32, 0xC0903AA2, 0xC0903AA2);
        }

        RenderSystem.color(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(16777215);
        tessellator.addVertexWithUV(x, (y + iconHeight), 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((x + iconHeight), (y + iconHeight), 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV((x + iconHeight), y, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x, y, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        String showName = tag.name;
        if (tag.isInStack()) {
            showName = "[§e" + (tag.order + 1) + "§r] " + showName;
        }

        if (!isSelectedOne) {
            showName = StringFeatures.limitString(showName, 44, true);
        }

        parentScreen.drawString(minecraft.fontRenderer, showName, x + iconHeight + 2.0F, y + 1.0F, 16777215);

        if (tag.hasCategories()) {
            if (tag.secondLine == null || tag.secondLine.isEmpty()) {
                parentScreen.drawString(minecraft.fontRenderer, tag.firstLine, x + iconHeight + 2.0F, y + 12.0F, 8421504);
            } else {
                parentScreen.drawString(minecraft.fontRenderer, descriptionIndex[index] ? tag.secondLine : tag.firstLine, x + iconHeight + 2.0F, y + 12.0F, 8421504);
            }
            parentScreen.drawString(minecraft.fontRenderer, tag.getBakedCSS()[animationIndex[index]], x + iconHeight + 2.0F, y + 22.0F, 0xFFFFFFFF);
        } else {
            parentScreen.drawString(minecraft.fontRenderer, tag.firstLine, x + iconHeight + 2.0F, y + 12.0F, 8421504);
            parentScreen.drawString(minecraft.fontRenderer, tag.secondLine, x + iconHeight + 2.0F, y + 12.0F + 11.0F, 8421504);
        }
    }

    @Override
    public boolean allowTransparency() {
        return true;
    }

    @Override
    protected int getSize() {
        return parentScreen.getSequoiaCacheSize();
    }

    @Override
    protected boolean isSelected(int index) {
        return this.selectedIndex == index;
    }

    @Override
    protected int getContentHeight() {
        return getSize() * 36;
    }
}
