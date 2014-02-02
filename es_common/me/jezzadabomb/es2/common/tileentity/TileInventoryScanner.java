package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;
import java.util.Random;

import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.client.hud.StoredQueues;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.packets.InventoryPacket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cofh.api.block.IDismantleable;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TileInventoryScanner extends TileES implements IDismantleable {

    private int dis = Reference.HUD_BLOCK_RANGE * 2;

    public boolean hasInventory;
    public boolean restart = false;

    @Override
    public void updateEntity() {
        hasInventory = UtilMethods.isIInventory(worldObj, xCoord, yCoord - 1, zCoord);
        if (!hasInventory) {
            worldObj.destroyBlock(xCoord, yCoord, zCoord, true);
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
}
