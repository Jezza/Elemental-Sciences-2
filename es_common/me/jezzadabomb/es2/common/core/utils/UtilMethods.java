package me.jezzadabomb.es2.common.core.utils;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.items.ItemDebugTool;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class UtilMethods {

	public static ItemStack mergeItemStacks(ItemStack itemStack1, ItemStack itemStack2, boolean overflow) {
		if (itemStack1 == null || itemStack2 == null || itemStack1.itemID != itemStack2.itemID || itemStack1.getItemDamage() != itemStack2.getItemDamage())
			return null;
		itemStack1.stackSize += itemStack2.stackSize;
		itemStack2.stackSize = 0;
		if (!overflow && itemStack1.stackSize > 64) {
			itemStack1.stackSize = 64;
		}
		return itemStack1;
	}

	public static boolean canShowDebugHUD() {
		return isHoldingItem(ModItems.debugItem) && Reference.CAN_DEBUG;
	}

	public static boolean isRenderType(TileEntity tileEntity, int type) {
	    if(tileEntity == null){
	        return false;
	    }
        return tileEntity.worldObj.blockGetRenderType(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord) == type;
    }
	
	public static boolean hasItemInInventory(EntityPlayer player, Item item){
		for(ItemStack itemStack : player.inventory.mainInventory){
			if(itemStack != null && itemStack.getItem().equals(item)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean canFlood() {
		if(canShowDebugHUD()){
		    ItemStack tempStack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
		    return ((ItemDebugTool)tempStack.getItem()).canFlood;
		}
		return false;
	}
	
	public static boolean correctMode(int mode) {
        if(canShowDebugHUD()){
            ItemStack tempStack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
            return ((ItemDebugTool)tempStack.getItem()).debugMode == mode;
        }
        return false;
    }

	public static boolean isHoldingItem(Item item) {
		return isHoldingItemStack(new ItemStack(item));
	}

	public static boolean isHoldingItemStack(ItemStack itemStack) {
		if (Minecraft.getMinecraft().thePlayer == null)
			return false;
		ItemStack tempStack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
		if (itemStack != null && tempStack != null)
			return tempStack.getItem().equals(itemStack.getItem());
		return false;
	}

	public static boolean isWearingItem(Item item) {
		return isWearingItemStack(new ItemStack(item));
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

	public static boolean isWearingItemStack(ItemStack itemStack) {
		if (!(itemStack.getItem() instanceof ItemArmor)) {
			return false;
		}
		ItemArmor tempArmour = (ItemArmor) itemStack.getItem();
		int index = getSlotFromIndex(tempArmour.armorType);
		if (index == -1)
			return false;
		ItemStack tempStack = Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(index);
		if (tempStack != null) {
			return tempStack.getItem().equals(itemStack.getItem());
		}
		return false;
	}

	public static String getLocFromArray(int[] coords) {
		return coords[0] + ":" + coords[1] + ":" + coords[2];
	}

	public static int[] getArrayFromString(String loc) {
		if (!loc.matches("-?\\d*:-?\\d*:-?\\d*")) {
			return null;
		}
		int[] coord = new int[3];
		coord[0] = Integer.parseInt(loc.substring(0, loc.indexOf(":")));
		coord[1] = Integer.parseInt(loc.substring(loc.indexOf(":") + 1, loc.indexOf(":", loc.indexOf(":") + 1)));
		coord[2] = Integer.parseInt(loc.substring(loc.lastIndexOf(":") + 1));
		return coord;
	}
	
	public static Block getBlockFromWorld(World world, int x, int y, int z){
		return Block.blocksList[world.getBlockId(x, y, z)];
	}

}
