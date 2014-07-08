package me.jezzadabomb.es2.common.core.utils.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PlayerHelper {

    public static boolean isPlayerWearing(EntityPlayer player, Item item) {
        if (player == null || item == null || !(item instanceof ItemArmor))
            return false;
        int index = getSlotFromIndex(((ItemArmor) item).armorType);
        if (index == -1)
            return false;
        ItemStack tempStack = player.inventory.armorItemInSlot(index);
        if (tempStack != null)
            return item.equals(tempStack.getItem());

        return false;
    }

    public static boolean isPlayerWearing(EntityPlayer player, ItemStack itemStack) {
        return isPlayerWearing(player, itemStack.getItem());
    }

    @SideOnly(Side.CLIENT)
    public static boolean isWearingItem(Item item) {
        return isPlayerWearing(Minecraft.getMinecraft().thePlayer, item);
    }

    @SideOnly(Side.CLIENT)
    public static boolean isWearingItemStack(ItemStack itemStack) {
        return isWearingItem(itemStack.getItem());
    }

    @SideOnly(Side.CLIENT)
    public static boolean isHoldingItem(Item item) {
        return isHoldingItemStack(new ItemStack(item));
    }

    @SideOnly(Side.CLIENT)
    public static boolean isHoldingItemStack(ItemStack itemStack) {
        if (Minecraft.getMinecraft().thePlayer == null)
            return false;
        ItemStack tempStack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
        if (itemStack != null && tempStack != null)
            return tempStack.getItem().equals(itemStack.getItem());
        return false;
    }

    // No NBT Comparision.
    public static boolean hasItemInInventory(EntityPlayer player, ItemStack itemStack, boolean shouldConsume, ItemStack replaceStack) {
        int index = 0;
        for (ItemStack tempStack : player.inventory.mainInventory) {
            if (tempStack != null && ItemStack.areItemStacksEqual(itemStack, tempStack)) {
                if (shouldConsume)
                    player.inventory.mainInventory[index] = replaceStack;
                return true;
            }
            index++;
        }
        return false;
    }

    public static int getSlotFromIndex(int index) {
        switch (index) {
            case 0:
                return 3;
            case 1:
                return 2;
            case 2:
                return 1;
            case 3:
                return 0;
            default:
                return -1;
        }
    }

}
