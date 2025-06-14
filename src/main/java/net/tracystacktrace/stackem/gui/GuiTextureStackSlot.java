package net.tracystacktrace.stackem.gui;

import com.indigo3d.util.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.world.Tessellator;
import net.tracystacktrace.stackem.impl.TagTexturePack;

public class GuiTextureStackSlot extends GuiSlot {

    protected GuiTextureStack parentScreen;
    public int selectedIndex = -1;

    public GuiTextureStackSlot(GuiTextureStack parentScreen, int width, int height) {
        super(width, height, 32, height - 55 + 4, 36);
        this.parentScreen = parentScreen;
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
    }

    @Override
    protected void drawSlot(Minecraft minecraft, int index, float x, float y, int iconHeight, Tessellator tessellator) {
        TagTexturePack tag = parentScreen.getSequoiaCacheElement(index);

        if (tag.thumbnail == null) {
            minecraft.renderEngine.bindTexture(minecraft.renderEngine.getTexture("/textures/gui/unknown_pack.png"));
        } else {
            tag.bindThumbnail(minecraft.renderEngine);
        }

        if (tag.isInStack()) {
            this.drawGradientRect(x, y, x + 216, y + 32, 0xC0903AA2, 0xC0903AA2);
        }

        RenderSystem.color(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(16777215);
        tessellator.addVertexWithUV(x, (y + iconHeight), 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((x + 32.0F), (y + iconHeight), 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV((x + 32.0F), y, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x, y, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        String showName = tag.name;
        if (tag.isInStack()) {
            showName = "[" + (tag.order + 1) + "] " + showName;
        }

        if (showName.length() > 32) {
            showName = showName.substring(0, 32).trim() + "...";
        }

        parentScreen.drawString(minecraft.fontRenderer, showName, x + 32.0F + 2.0F, y + 1.0F, 16777215);
        parentScreen.drawString(minecraft.fontRenderer, tag.firstLine, x + 32.0F + 2.0F, y + 12.0F, 8421504);
        parentScreen.drawString(minecraft.fontRenderer, tag.secondLine, x + 32.0F + 2.0F, y + 12.0F + 10.0F, 8421504);
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
