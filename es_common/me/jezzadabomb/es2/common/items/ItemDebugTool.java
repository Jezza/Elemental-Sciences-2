package me.jezzadabomb.es2.common.items;

import java.util.ArrayList;
import java.util.List;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.common.api.HUDBlackLists;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.Strings;
import me.jezzadabomb.es2.common.packets.InventoryPacket;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDebugTool extends ItemES {

	public int debugMode;
	private int DEBUG_LIMIT;
	public boolean canFlood;

	public ItemDebugTool(int id, String name) {
		super(id, name);
		canFlood = false;
		debugMode = 0;
		DEBUG_LIMIT = debugStringList.size();
	}

	private static ArrayList<String> debugStringList = new ArrayList<String>() {
		{
			add("Normal Debug");
			add("Extreme Packet Monitoring");
			add("Reset");
			add("Y-Axis Toggle");
			add("Get Bomb Holder");
		}
	};

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (world.isRemote && Reference.CAN_DEBUG) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				debugMode += 1;
				if (debugMode == DEBUG_LIMIT) {
					debugMode = 0;
				}
				player.addChatMessage(debugStringList.get(debugMode) + " Mode");
			} else {
				switch (debugMode) {
				case 0:
					break;
				case 1:
					canFlood = !canFlood;
					player.addChatMessage((canFlood ? "Enabled" : "Disabled") + " Debug Flooding.");
					break;
				case 2:
					HUDBlackLists.refreshTestList();
					player.addChatMessage("Refreshed BlackList");
					break;
				case 3:
					boolean temp = Reference.HUD_VERTICAL_ROTATION;
					Reference.HUD_VERTICAL_ROTATION = !temp;
					player.addChatMessage("HUD Rotation: " + !temp);
					break;
				case 4:
					String playerString = ElementalSciences2.proxy.quantumBomb.getPlayer();
					if(playerString == null){
						player.addChatMessage("Empty");
					}else{						
						player.addChatMessage(playerString);
					}
					break;
				}
			}
		}
		return itemStack;
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ) {
		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			InventoryPacket packet = ClientProxy.hudRenderer.getPacket(x, y, z);
			if (packet != null) {
				player.addChatMessage("");
				player.addChatMessage("Inventory Name: " + packet.inventoryTitle);
				player.addChatMessage("Contents: ");
				String tempString = packet.getItemStacksInfo();
				if (tempString == null)
					return false;
				int lastIndex = 0;
				for (int i = 0; i < tempString.length(); i++) {
					if (tempString.charAt(i) == ',') {
						player.addChatMessage(tempString.substring(lastIndex, i));
						lastIndex = i + 1;
					}
				}
			}

			if (UtilMethods.canShowDebugHUD()) {
				if (world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof TileInventoryScanner) {
					TileInventoryScanner inventoryScanner = (TileInventoryScanner) world.getBlockTileEntity(x, y, z);
					ESLogger.debug(inventoryScanner.hasInventory);
					inventoryScanner.restart = true;
				}
			}

			return packet != null;
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation() {
		defaultInfoList();
		switch (debugMode) {
		case 0:
			break;
		case 1:
			shiftList.add("Enables console to use no filter with debug messages.");
			shiftList.add("[Warning] Floods console.");
			break;
		case 2:
			shiftList.add("Refreshes the renderBlackList.");
			break;
		case 3:
			break;
		}
	}

}
