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
        super(width, height, 0, height - 30, 48, 320);
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
        final TagTexturePack tag = parentScreen.getSequoiaCacheElement(index);

        if (tag.hasThumbnail()) {
            tag.bindThumbnail(minecraft.renderEngine);
        } else {
            minecraft.renderEngine.bindTexture(minecraft.renderEngine.getTexture("/textures/gui/unknown_pack.png"));
        }

        if (tag.isInStack()) {
            this.drawGradientRect(x, y, x + 316, y + 44, 0xC0903AA2, 0xC0903AA2);
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

        if (showName.length() > 56) {
            showName = showName.substring(0, 56).trim() + "...";
        }

        parentScreen.drawString(minecraft.fontRenderer, showName, x + iconHeight + 2.0F, y + 1.0F, 16777215);
        parentScreen.drawString(minecraft.fontRenderer, tag.firstLine, x + iconHeight + 2.0F, y + 12.0F, 8421504);
        parentScreen.drawString(minecraft.fontRenderer, tag.secondLine, x + iconHeight + 2.0F, y + 12.0F + 11.0F, 8421504);

        if(tag.hasCategories()) {
            parentScreen.drawString(minecraft.fontRenderer, tag.getBakedCategoryString(), x + iconHeight + 2.0F, y + 34.0F, 0xFFFFFFFF);
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
