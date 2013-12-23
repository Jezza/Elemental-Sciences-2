package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilHelpers;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.packets.InventoryPacket;
import me.jezzadabomb.es2.common.packets.InventoryTerminatePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TileInventoryScanner extends TileES {

	private int tickTime = 0;
	private int dis = Reference.HUD_BLOCK_RANGE;

	@Override
	public void updateEntity() {
		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			return;
		}

		if (!worldObj.blockHasTileEntity(xCoord, yCoord + 1, zCoord)) {
			return;
		} else {
			TileEntity tileEntity = worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord);
			if (!(tileEntity instanceof IInventory)) {
				return;
			}
		}

		if (tickTime < Reference.GLASSES_WAIT_TIMER) {
			tickTime++;
			return;
		} else {
			tickTime = 0;
		}
		sendPacketToPlayers(getNearbyPlayers());
	}

	public ArrayList<EntityPlayer> getNearbyPlayers() {
		ArrayList<EntityPlayer> playerList = new ArrayList<EntityPlayer>();
		for (Object object : worldObj.playerEntities) {
			if (object instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) object;
				if (player.dimension != worldObj.provider.dimensionId) {
					return null;
				}
				if (player.getDistance(xCoord, yCoord, zCoord) < dis) {
					playerList.add(player);
				}
			}
		}
		return playerList;
	}

	public void sendTerminatePacket(){
		for(EntityPlayer player : getNearbyPlayers()){
			int[] coords = new int[3];
			coords[0] = xCoord;
			coords[1] = yCoord + 1;
			coords[2] = zCoord;
			PacketDispatcher.sendPacketToPlayer(new InventoryTerminatePacket(UtilHelpers.getLocFromArray(coords)).makePacket(), (Player) player);
		}
	}
	
	public void sendPacketToPlayers(ArrayList<EntityPlayer> players) {
		if (players == null)
			return;
		for (EntityPlayer player : players) {
			int[] coords = new int[3];
			coords[0] = xCoord;
			coords[1] = yCoord + 1;
			coords[2] = zCoord;
			PacketDispatcher.sendPacketToPlayer(new InventoryPacket(worldObj.getBlockTileEntity(coords[0], coords[1], coords[2]), UtilHelpers.getLocFromArray(coords)).makePacket(), (Player) player);
		}
	}
}
