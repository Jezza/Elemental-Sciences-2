package me.jezzadabomb.es2.common.tileentity;

import static org.lwjgl.opengl.GL11.glRotatef;

import java.util.ArrayList;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.packets.InventoryPacket;
import me.jezzadabomb.es2.common.packets.InventoryTerminatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TileInventoryScanner extends TileES {

    public float rotYaw;
    private int tickTime = 0;
    private int dis = Reference.HUD_BLOCK_RANGE * 2;

    public boolean hasInventory;
    public boolean restart = false;

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) {
            EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().renderViewEntity;
            rotYaw = (float) (Math.atan2((xCoord + 0.5F) - player.posX, (zCoord + 0.5F) - player.posZ) * 180.0D / 3.141592653589793D);
        }
        int y = yCoord - 1;
        if (!worldObj.blockHasTileEntity(xCoord, y, zCoord)) {
            notifyPacketList();
            return;
        } else {
            if (!(worldObj.getBlockTileEntity(xCoord, y, zCoord) instanceof IInventory)) {
                notifyPacketList();
                return;
            }
        }
        hasInventory = true;
        if (worldObj.isRemote) {
            return;
        }
        if (tickTime < Reference.GLASSES_WAIT_TIMER) {
            tickTime++;
            return;
        } else {
            tickTime = 0;
        }
        sendPacketToPlayers(getNearbyPlayers());
    }

    private void notifyPacketList() {
        hasInventory = false;
        worldObj.destroyBlock(xCoord, yCoord, zCoord, true);
        if (worldObj.isRemote)
            if (!UtilMethods.isWearingItem(ModItems.glasses))
                ClientProxy.getHUDRenderer().addToRemoveList(xCoord, yCoord - 1, zCoord);
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

    public void sendTerminatePacket() {
        for (EntityPlayer player : getNearbyPlayers()) {
            int[] coords = new int[3];
            coords[0] = xCoord;
            coords[1] = yCoord - 1;
            coords[2] = zCoord;
            PacketDispatcher.sendPacketToPlayer(new InventoryTerminatePacket(UtilMethods.getLocFromArray(coords)).makePacket(), (Player) player);
        }
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
