package me.jezzadabomb.es2.common.items;

import java.util.List;

import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilHelpers;
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

	public ItemDebugTool(int id, String name) {
		super(id, name);
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
			
			if(UtilHelpers.canShowDebugHUD()){
				if(world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof TileInventoryScanner){
					TileInventoryScanner inventoryScanner = (TileInventoryScanner)world.getBlockTileEntity(x, y, z);
					ESLogger.debug(inventoryScanner.hasInventory);
					inventoryScanner.restart = true;
				}
			}
			
			return packet != null;
		}
		return false;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){			
			list.add("Important dev stuff");
		}
	}

}
