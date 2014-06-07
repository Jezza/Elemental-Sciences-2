package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;

import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.interfaces.IBlockNotifier;
import me.jezzadabomb.es2.common.core.interfaces.IDismantleable;
import me.jezzadabomb.es2.common.core.network.PacketDispatcher;
import me.jezzadabomb.es2.common.core.network.packet.server.InventoryPacket;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TileInventoryScanner extends TileES implements IDismantleable, IBlockNotifier {

    private int dis = Reference.HUD_BLOCK_RANGE * 2;

    public boolean hasInventory;
    public boolean restart = false;

    @Override
    public void updateEntity() {
        CoordSet coordSet = new CoordSet(xCoord, yCoord - 1, zCoord);
        hasInventory = coordSet.isIInventory(worldObj);
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
        hasInventory = coordSet.addY(-1).isIInventory(worldObj);

        if (!hasInventory)
            ClientProxy.getHUDRenderer().removePacketAtXYZ(coordSet);
    }

    public void sendPacketToPlayers(ArrayList<EntityPlayer> players) {
        if (players == null)
            return;
        for (EntityPlayer player : players) {
            CoordSet coordSet = getCoordSet().copy().addY(-1);
            PacketDispatcher.sendPacketToPlayer(new InventoryPacket(worldObj.getTileEntity(coordSet.getX(), coordSet.getY(), coordSet.getZ()), coordSet.toPacketString()), (EntityPlayerMP) player);
        }
    }

    @Override
    public ItemStack dismantleBlock(EntityPlayer player, World world, CoordSet coordSet, boolean returnBlock) {
        coordSet.setBlockToAir(world);
        removePacket();
        if (!world.isRemote && returnBlock)
            world.spawnEntityInWorld(new EntityItem(world, coordSet.getX() + 0.5F, coordSet.getY() + 0.1F, coordSet.getZ() + 0.5F, new ItemStack(ModBlocks.inventoryScanner)));
        return null;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, CoordSet coordSet) {
        return true;
    }

    private void removePacket() {
        ClientProxy.getHUDRenderer().removePacketAtXYZ(getCoordSet().copy().addY(-1));
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        removePacket();
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {

    }
}
