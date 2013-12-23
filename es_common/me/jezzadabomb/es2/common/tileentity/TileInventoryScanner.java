package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilHelpers;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.packets.InventoryPacket;

public class TileInventoryScanner extends TileES {

	private int tickTime = 0;
	private int dis = Reference.HUD_BLOCK_RANGE;

	@Override
	public void updateEntity() {
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

	public void sendPacketToPlayers(ArrayList<EntityPlayer> players) {
		if(FMLCommonHandler.instance().getEffectiveSide().isClient()){
			return;
		}
		if (players == null)
			return;
		for (EntityPlayer player : players) {
			int[] coords = new int[3];
			coords[0] = xCoord;
			coords[1] = yCoord + 1;
			coords[2] = zCoord;
			ESLogger.debug("Sent packet to " + player.username);
			PacketDispatcher.sendPacketToPlayer(new InventoryPacket(worldObj.getBlockTileEntity(coords[0], coords[1], coords[2]), UtilHelpers.getLocFromArray(coords)).makePacket(), (Player)player);
		}
	}
}
