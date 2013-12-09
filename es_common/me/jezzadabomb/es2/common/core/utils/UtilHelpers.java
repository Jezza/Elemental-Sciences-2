package me.jezzadabomb.es2.common.core.utils;

import me.jezzadabomb.es2.common.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class UtilHelpers {

    public static ItemStack mergeItemStacks(ItemStack itemStack1, ItemStack itemStack2, boolean overflow){
        if(itemStack1 == null || itemStack2 == null || itemStack1.itemID != itemStack2.itemID)
            return null;
        itemStack1.stackSize += itemStack2.stackSize;
        itemStack2.stackSize = 0;
        if(!overflow && itemStack1.stackSize > 64){
            itemStack1.stackSize = 64;
        }
        return itemStack1;
    }
    
    public static boolean canShowDebugHUD(){
        ItemStack itemStack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
        if(itemStack != null){
            return itemStack.itemID == ModItems.debugItem.itemID;
        }
        return false;
    }
    
}
