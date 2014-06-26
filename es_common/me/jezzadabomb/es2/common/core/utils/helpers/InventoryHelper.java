package me.jezzadabomb.es2.common.core.utils.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryHelper {

    public static boolean addItemStackToInventory(ItemStack[] inventory, ItemStack stack, int startIndex) {
        if (stack == null)
            return true;

        int openSlot = -1;
        for (int i = startIndex; i < inventory.length; i++) {
            if (areItemStacksEqual(stack, inventory[i]) && inventory[i].getMaxStackSize() > inventory[i].stackSize) {
                int hold = inventory[i].getMaxStackSize() - inventory[i].stackSize;
                if (hold >= stack.stackSize) {
                    inventory[i].stackSize += stack.stackSize;
                    stack = null;
                    return true;
                }
                stack.stackSize -= hold;
                inventory[i].stackSize += hold;
            } else if (inventory[i] == null && openSlot == -1) {
                openSlot = i;
            }
        }
        if (stack != null) {
            if (openSlot > -1) {
                inventory[openSlot] = stack;
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean addItemStackToInventory(ItemStack[] inventory, ItemStack stack) {
        return addItemStackToInventory(inventory, stack, 0);
    }

    public static boolean addItemStackToIInventory(IInventory inventory, ItemStack itemStack) {
        return addItemStackToIInventory(inventory, itemStack, 0);
    }

    public static boolean addItemStackToIInventory(IInventory inventory, ItemStack itemStack, int startIndex) {
        int size = inventory.getSizeInventory();
        ItemStack[] stackArray = new ItemStack[size];

        for (int i = 0; i < size; i++)
            stackArray[i] = inventory.getStackInSlot(i);

        return addItemStackToInventory(stackArray, itemStack, startIndex);
    }

    public static boolean addItemStackToInventory(EntityPlayer player, ItemStack itemStack) {
        if (addItemStackToInventory(player.inventory.mainInventory, itemStack)) {
            player.inventory.decrementAnimations();
            return true;
        }
        return false;
    }

    public static ItemStack mergeItemStacks(ItemStack itemStack1, ItemStack itemStack2, boolean overflow) {
        if ((itemStack1 == null && itemStack2 == null) || !ItemStack.areItemStacksEqual(itemStack1, itemStack2))
            return null;
        itemStack1.stackSize += itemStack2.stackSize;
        itemStack2.stackSize = 0;
        if (!overflow && itemStack1.stackSize > 64)
            itemStack1.stackSize = 64;
        return itemStack1;
    }

    public static int removeItemStackFromIInventory(IInventory inventory, ItemStack itemStack, int count) {
        int index = 0;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack tempStack = inventory.getStackInSlot(i);
            if (areItemStacksEqual(itemStack, tempStack)) {
                inventory.setInventorySlotContents(i, (ItemStack) null);
                if (++index >= count)
                    return index;
            }
        }

        return index;
    }

    public static boolean areItemStacksEqual(ItemStack itemStack1, ItemStack itemStack2) {
        return itemStack1 != null && itemStack2 != null && itemStack1.getItemDamage() == itemStack2.getItemDamage() && itemStack1.getItem().equals(itemStack2.getItem());
    }

}
