package com.octagon.crazygui.components;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.octagon.crazygui.BoxRenderer;
import com.octagon.crazygui.GuiComponentResizableComposite;
import com.octagon.crazygui.ISlotBackgroundRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Map;

public class GuiComponentPanel extends GuiComponentResizableComposite {
    private static final BoxRenderer BOX_RENDERER = new BoxRenderer(0, 5);

    public static final ISlotBackgroundRenderer normalSlot = (gui, slot) -> gui.drawTexturedModalRect(slot.xDisplayPosition - 1, slot.yDisplayPosition - 1, 0, 20, 18, 18);

    public static final ISlotBackgroundRenderer bigSlot = (gui, slot) -> gui.drawTexturedModalRect(slot.xDisplayPosition - 5, slot.yDisplayPosition - 5, 29, 20, 26, 26);

    public static final ISlotBackgroundRenderer noRenderSlot = (gui, slot) -> {};

    public static ISlotBackgroundRenderer coloredSlot(final int color) {
        return (gui, slot) -> {
            GL11.glColor3ub((byte) (color >> 16), (byte) (color >> 8), (byte) (color >> 0));
            gui.drawTexturedModalRect(slot.xDisplayPosition - 1, slot.yDisplayPosition - 1, 0, 20, 18, 18);
            GL11.glColor3f(1, 1, 1);
        };
    }

    private final Map<Integer, ISlotBackgroundRenderer> slotRenderers = Maps.newHashMap();

    private final Container container;

    public GuiComponentPanel(int x, int y, int width, int height, Container container) {
        super(x, y, width, height);
        this.container = container;
    }

    public void setSlotRenderer(int slotId, ISlotBackgroundRenderer renderer) {
        slotRenderers.put(slotId, renderer);
    }

    @Override
    protected void renderComponentBackground(Minecraft minecraft, int x, int y, int mouseX, int mouseY) {
        GL11.glColor3f(1, 1, 1);
        bindComponentsSheet();
        BOX_RENDERER.render(this, this.x + x, this.y + y, width, height, 0xFFFFFFFF);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void renderComponentForeground(Minecraft minecraft, int x, int y, int mouseX, int mouseY) {
        GL11.glColor3f(1, 1, 1);
        bindComponentsSheet();

        if (container != null) {
            for (Slot slot : (List<Slot>)container.inventorySlots) {
                Objects.firstNonNull(slotRenderers.get(slot.slotNumber), normalSlot).render(this, slot);
            }
        }
    }
}
