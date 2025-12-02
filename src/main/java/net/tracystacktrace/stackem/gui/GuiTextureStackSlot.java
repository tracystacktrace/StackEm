package net.tracystacktrace.stackem.gui;

import com.indigo3d.util.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.world.Tessellator;
import net.tracystacktrace.stackem.neptune.container.PreviewTexturePack;
import net.tracystacktrace.stackem.tools.StringFeatures;

public class GuiTextureStackSlot extends GuiSlot {

    protected GuiTextureStack parentScreen;
    public int selectedIndex = -1;

    public GuiTextureStackSlot(GuiTextureStack parentScreen, int width, int height) {
        super(width, height, 0, height - 30, 36, 320);
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
        final PreviewTexturePack tag = parentScreen.getSequoiaCacheElement(index);
        final boolean isSelectedOne = this.selectedIndex == index;

        if (tag.hasIcon()) {
            RenderSystem.bindTexture2D(tag.bindTexture(minecraft.renderEngine::allocateAndSetupTexture));
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
        String showName = tag.getName();
        if (tag.isInStack()) {
            showName = "[§e" + (tag.order + 1) + "§r] " + showName;
        }

        if (!isSelectedOne) {
            showName = StringFeatures.limitString(showName, 44, true);
        }

        parentScreen.drawString(minecraft.fontRenderer, showName, x + iconHeight + 2.0F, y + 1.0F, 16777215);

        parentScreen.drawString(minecraft.fontRenderer, tag.getFirstLine(), x + iconHeight + 2.0F, y + 12.0F, 8421504);
        parentScreen.drawString(minecraft.fontRenderer, tag.getSecondLine(), x + iconHeight + 2.0F, y + 12.0F + 11.0F, 8421504);
    }

    @Override
    protected void renderDecorations(float width, float height) {
        final int startX = (this.left + this.right) / 2 - this.slotWidth / 2 + 2 + this.slotOffset;
        final float initialY = this.top + 4 - (int) this.amountScrolled + this.headerPadding;

        for (int i = 0; i < this.getSize(); i++) {
            final PreviewTexturePack tag = parentScreen.getSequoiaCacheElement(i);

            if (!tag.hasBakedCategoriesList()) continue;

            if (this.isSlotHovered(mouseX, mouseY, startX, initialY + this.slotHeight * i)) {
                parentScreen.renderCategoriesTooltip(mouseX, mouseY, tag);
            }
        }
    }

    private boolean isSlotHovered(
            final float mouseX,
            final float mouseY,
            final int x,
            final float y
    ) {
        return mouseX > x && mouseX < (x + 320) && mouseY > y && mouseY < (y + 36);
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
