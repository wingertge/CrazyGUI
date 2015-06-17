/**
 * Class courtesy of OpenMods - somewhat modified
 */

package com.octagon.crazygui;

import com.octagon.crazygui.util.TextureUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.List;

public abstract class BaseComponent extends Gui {
    protected static final RenderItem ITEM_RENDERER = new RenderItem();

    private static final int CRAZY_1 = 0x505000FF;
    private static final int CRAZY_2 = (CRAZY_1 & 0xFEFEFE) >> 1;
    private static final int CRAZY_3 = 0xF0100010;

    public final static ResourceLocation TEXTURE_SHEET = new ResourceLocation("crazygui", "textures/gui/components.png");

    public static void bindComponentsSheet() {
        TextureUtils.bindTextureToClient(TEXTURE_SHEET);
    }

    protected int x;
    protected int y;
    protected boolean enabled = true;

    public Event<IKeyTypedListener> keyTypedEvent;
    public Event<IMouseListener> mouseEvent;

    public BaseComponent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public abstract int getWidth();

    public abstract int getHeight();

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX < x + getWidth() && mouseY >= y && mouseY < y + getHeight();
    }

    public abstract void render(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY);

    public abstract void renderOverlay(Minecraft minecraft, int offsetX, int offsetY, int mouseX, int mouseY);

    public void keyTyped(char keyChar, int keyCode) {
        keyTypedEvent.fire("componentKeyTyped", this, keyChar, keyCode);
    }

    public void mouseDown(int mouseX, int mouseY, int button) {
        mouseEvent.fire("componentMouseDown", this, mouseX, mouseY, button);
    }

    public void mouseUp(int mouseX, int mouseY, int button) {
        mouseEvent.fire("componentMouseUp", this, mouseX, mouseY, button);
    }

    public void mouseDrag(int mouseX, int mouseY, int button, /* love you */long time) {
        mouseEvent.fire("componentMouseDrag", this, mouseX, mouseY, button, time);
    }

    protected void drawHoveringText(List<String> lines, int x, int y, FontRenderer font) {
        final int lineCount = lines.size();
        if (lineCount == 0) return;

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        int width = 0;

        for (String s : lines) {
            int l = font.getStringWidth(s);
            if (l > width) width = l;
        }

        final int left = x + 12;
        int top = y - 12;

        int height = 8;
        if (lineCount > 1) height += 2 + (lineCount - 1) * 10;

        this.zLevel = 350.0F;

        drawGradientRect(left - 3, top - 4, left + width + 3, top - 3, CRAZY_3, CRAZY_3);
        drawGradientRect(left - 3, top + height + 3, left + width + 3, top + height + 4, CRAZY_3, CRAZY_3);
        drawGradientRect(left - 3, top - 3, left + width + 3, top + height + 3, CRAZY_3, CRAZY_3);
        drawGradientRect(left - 4, top - 3, left - 3, top + height + 3, CRAZY_3, CRAZY_3);
        drawGradientRect(left + width + 3, top - 3, left + width + 4, top + height + 3, CRAZY_3, CRAZY_3);

        drawGradientRect(left - 3, top - 3 + 1, left - 3 + 1, top + height + 3 - 1, CRAZY_1, CRAZY_2);
        drawGradientRect(left + width + 2, top - 3 + 1, left + width + 3, top + height + 3 - 1, CRAZY_1, CRAZY_2);
        drawGradientRect(left - 3, top - 3, left + width + 3, top - 3 + 1, CRAZY_1, CRAZY_1);
        drawGradientRect(left - 3, top + height + 2, left + width + 3, top + height + 3, CRAZY_2, CRAZY_2);

        for (int i = 0; i < lineCount; ++i) {
            String line = lines.get(i);
            font.drawStringWithShadow(line, left, top, -1);
            if (i == 0) top += 2;
            top += 10;
        }

        this.zLevel = 0.0F;
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }

    public void drawItemStack(ItemStack stack, int x, int y, String overlayText) {
        this.zLevel = 200.0F;
        ITEM_RENDERER.zLevel = 200.0F;
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glColor3f(1f, 1f, 1f);
        GL11.glEnable(GL11.GL_NORMALIZE);
        FontRenderer font = null;
        if (stack != null) font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = Minecraft.getMinecraft().fontRenderer;
        ITEM_RENDERER.renderItemAndEffectIntoGUI(font, Minecraft.getMinecraft().getTextureManager(), stack, x, y);
        ITEM_RENDERER.renderItemOverlayIntoGUI(font, Minecraft.getMinecraft().getTextureManager(), stack, x, y, overlayText);
        this.zLevel = 0.0F;
        ITEM_RENDERER.zLevel = 0.0F;
    }
}
