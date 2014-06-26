package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;

import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.interfaces.IBlockNotifier;
import me.jezzadabomb.es2.common.core.network.PacketDispatcher;
import me.jezzadabomb.es2.common.core.network.packet.server.InventoryPacket;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TileInventoryScanner extends TileES implements IBlockNotifier {

    private int dis = Reference.HUD_BLOCK_RANGE * 2;

    public boolean hasInventory;
    public boolean restart = false;

    @Override
    public void updateEntity() {
        CoordSet coordSet = getLowerBlock();
        hasInventory = coordSet.isIInventory(worldObj);
        if (!hasInventory && !worldObj.isRemote) {
            worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, new ItemStack(ModBlocks.inventoryScanner)));
            getCoordSet().setBlockToAir(worldObj);
            return;
        }
        if (!worldObj.isRemote)
            sendPacketToPlayers(getNearbyPlayers());
    }

    public ArrayList<EntityPlayer> getNearbyPlayers() {
        ArrayList<EntityPlayer> playerList = new ArrayList<EntityPlayer>();
        for (int i = 0; i < worldObj.playerEntities.size(); i++) {
            EntityPlayer player = (EntityPlayer) worldObj.playerEntities.get(i);
            if (player.dimension == worldObj.provider.dimensionId && player.getDistance(xCoord, yCoord, zCoord) < dis)
                playerList.add(player);
        }
        return playerList;
    }

    public void sendPacketToPlayers(ArrayList<EntityPlayer> players) {
        if (players == null || players.isEmpty())
            return;
        for (EntityPlayer player : players) {
            CoordSet coordSet = getLowerBlock();
            PacketDispatcher.sendPacketToPlayer(new InventoryPacket((IInventory) coordSet.getTileEntity(worldObj), coordSet.toPacketString(), true), (EntityPlayerMP) player);
        }
    }

    private CoordSet getLowerBlock() {
        return new CoordSet(xCoord, yCoord - 1, zCoord);
    }

    private void removePacket() {
        ClientProxy.getHUDRenderer().removePacket(getLowerBlock());
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        removePacket();
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {

    }
}
