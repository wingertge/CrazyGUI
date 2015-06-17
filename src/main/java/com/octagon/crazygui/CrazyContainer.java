package com.octagon.crazygui;

import net.minecraft.inventory.IInventory;

public class CrazyContainer<T> extends ContainerBase {
    public CrazyContainer(IInventory playerInventory, IInventory ownerInventory, T owner) {
        super(playerInventory, ownerInventory, owner);
    }

    @Override
    public T getOwner() {
        return (T)super.getOwner();
    }
}
