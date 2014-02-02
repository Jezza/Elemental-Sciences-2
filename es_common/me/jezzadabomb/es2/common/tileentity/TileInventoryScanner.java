package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;

import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.hud.StoredQueues;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.packets.InventoryPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TileInventoryScanner extends TileES {

    private int dis = Reference.HUD_BLOCK_RANGE * 2;

    public boolean hasInventory;
    public boolean restart = false;

    @Override
    public void updateEntity() {
        hasInventory = UtilMethods.isIInventory(worldObj, xCoord, yCoord - 1, zCoord);
        if (!hasInventory) {
            notifyPacketList();
            return;
        }
        if (!worldObj.isRemote)
            sendPacketToPlayers(getNearbyPlayers());
    }

    public void notifyPacketList() {
        worldObj.destroyBlock(xCoord, yCoord, zCoord, true);
        if (worldObj.isRemote && !UtilMethods.isWearingItem(ModItems.glasses))
            ClientProxy.getHUDRenderer().removeAtXYZ(xCoord, yCoord - 1, zCoord);
    }

    public ArrayList<EntityPlayer> getNearbyPlayers() {
        ArrayList<EntityPlayer> playerList = new ArrayList<EntityPlayer>();
        for (Object object : worldObj.playerEntities) {
            if (object instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) object;
                if (player.dimension == worldObj.provider.dimensionId && player.getDistance(xCoord, yCoord, zCoord) < dis) {
                    playerList.add(player);
                }
            }
        }
        return playerList;
    }

    public void sendPacketToPlayers(ArrayList<EntityPlayer> players) {
        if (players == null)
            return;
        for (EntityPlayer player : players) {
            int[] coords = new int[3];
            coords[0] = xCoord;
            coords[1] = yCoord - 1;
            coords[2] = zCoord;
            PacketDispatcher.sendPacketToPlayer(new InventoryPacket(worldObj.getBlockTileEntity(coords[0], coords[1], coords[2]), UtilMethods.getLocFromArray(coords)).makePacket(), (Player) player);
        }
    }
}
