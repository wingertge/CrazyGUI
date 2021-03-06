package com.octagon.crazygui.util;

import net.minecraft.item.ItemStack;

public class InventoryUtils {
    public static boolean tryMergeStacks(ItemStack stackToMerge, ItemStack stackInSlot) {
        if (stackInSlot == null || !stackInSlot.isItemEqual(stackToMerge) || !ItemStack.areItemStackTagsEqual(stackToMerge, stackInSlot)) return false;

        int newStackSize = stackInSlot.stackSize + stackToMerge.stackSize;

        final int maxStackSize = stackToMerge.getMaxStackSize();
        if (newStackSize <= maxStackSize) {
            stackToMerge.stackSize = 0;
            stackInSlot.stackSize = newStackSize;
            return true;
        } else if (stackInSlot.stackSize < maxStackSize) {
            stackToMerge.stackSize -= maxStackSize - stackInSlot.stackSize;
            stackInSlot.stackSize = maxStackSize;
            return true;
        }

        return false;
    }
}
