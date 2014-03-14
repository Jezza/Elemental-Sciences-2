package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;

import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.interfaces.IDismantleable;
import me.jezzadabomb.es2.common.core.network.PacketDispatcher;
import me.jezzadabomb.es2.common.core.network.packet.server.InventoryPacket;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TileInventoryScanner extends TileES implements IDismantleable {

    private int dis = Reference.HUD_BLOCK_RANGE * 2;

    public boolean hasInventory;
    public boolean restart = false;

    @Override
    public void updateEntity() {
        hasInventory = UtilMethods.isIInventory(worldObj, xCoord, yCoord - 1, zCoord);
        if (!hasInventory && !worldObj.isRemote) {
            worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, new ItemStack(ModBlocks.inventoryScanner)));
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);

            return;
        }
        if (!worldObj.isRemote)
            sendPacketToPlayers(getNearbyPlayers());
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

    
    
    @Override
    public void onNeighbourBlockChange(CoordSet coordSet) {
        hasInventory = UtilMethods.isIInventory(worldObj, xCoord, yCoord - 1, zCoord);

        if (!hasInventory)
            ClientProxy.getHUDRenderer().removePacketAtXYZ(xCoord, yCoord - 1, zCoord);
    }

    public void sendPacketToPlayers(ArrayList<EntityPlayer> players) {
        if (players == null)
            return;
        for (EntityPlayer player : players) {
            int[] coords = new int[3];
            coords[0] = xCoord;
            coords[1] = yCoord - 1;
            coords[2] = zCoord;
            PacketDispatcher.sendPacketToPlayer(new InventoryPacket(worldObj.getTileEntity(coords[0], coords[1], coords[2]), UtilMethods.getLocFromArray(coords)), (EntityPlayerMP) player);
        }
    }

    @Override
    public ItemStack dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnBlock) {
        world.setBlockToAir(x, y, z);
        ClientProxy.getHUDRenderer().removePacketAtXYZ(x, y - 1, z);
        if (!world.isRemote && returnBlock)
            world.spawnEntityInWorld(new EntityItem(world, x + 0.5F, y + 0.1F, z + 0.5F, new ItemStack(ModBlocks.inventoryScanner)));
        return null;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z) {
        return true;
    }

    @Override
    public Object getGui(int id, Side side, EntityPlayer player) {
        return null;
    }
}
