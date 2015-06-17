/**
 * Class courtesy of OpenMods
 */

package com.octagon.crazygui;

import com.octagon.crazygui.components.GuiComponentPanel;
import net.minecraft.util.StatCollector;

public class BaseGuiContainer<T extends CrazyContainer<?>> extends ComponentGui {
    protected final String name;

    public BaseGuiContainer(T container, int width, int height, String name) {
        super(container, width, height);
        this.name = name;
    }

    @Override
    protected BaseComposite createRoot() {
        return new GuiComponentPanel(0, 0, xSize, ySize, getContainer());
    }

    @SuppressWarnings("unchecked")
    public T getContainer() {
        return (T)inventorySlots;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        String machineName = StatCollector.translateToLocal(name);
        int x = this.xSize / 2 - (fontRendererObj.getStringWidth(machineName) / 2);
        fontRendererObj.drawString(machineName, x, 6, 4210752);
        String translatedName = StatCollector.translateToLocal("container.inventory");
        fontRendererObj.drawString(translatedName, 8, this.ySize - 96 + 2, 4210752);
    }

    public void sendButtonClick(int buttonId) {
        this.mc.playerController.sendEnchantPacket(getContainer().windowId, buttonId);
    }
}
