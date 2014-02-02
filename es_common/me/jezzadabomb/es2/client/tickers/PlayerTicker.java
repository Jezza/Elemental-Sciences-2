package me.jezzadabomb.es2.client.tickers;

import java.util.EnumSet;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.api.HUDBlackLists;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.hud.InventoryInstance;
import me.jezzadabomb.es2.common.hud.StoredQueues;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.packets.InventoryRequestPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerTicker implements ITickHandler {

    // private int ticked = 0;
    private int dis;
    private int oldX, oldY, oldZ, notMoveTick;
    StoredQueues storedQueues;

    public PlayerTicker() {
        dis = Reference.HUD_BLOCK_RANGE;
        storedQueues = new StoredQueues();
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (UtilMethods.isPlayerWearing(player, ModItems.glasses)) {
            World world = player.worldObj;
            int playerX = (int) Math.round(player.posX);
            int playerY = (int) Math.round(player.posY);
            int playerZ = (int) Math.round(player.posZ);

            if (playerMoved(playerX, playerY, playerZ) || notMoveTick == Reference.GLASSES_WAIT_TIMER) {
                notMoveTick = 0;
                for (int x = -dis; x < dis; x++)
                    for (int y = -dis; y < dis; y++)
                        for (int z = -dis; z < dis; z++) {
                            int tempX = playerX + x;
                            int tempY = playerY + y;
                            int tempZ = playerZ + z;
                            if (UtilMethods.isIInventory(world, tempX, tempY, tempZ)) {
                                TileEntity tileEntity = world.getBlockTileEntity(tempX, tempY, tempZ);
                                if (HUDBlackLists.scannerBlackListContains(tileEntity.getBlockType()))
                                    break;

                                InventoryInstance tempInstance = new InventoryInstance(((IInventory) tileEntity).getInvName(), tempX, tempY, tempZ);

                                storedQueues.putTempInventory(tempInstance);
                                if (!storedQueues.isAlreadyInQueue(tempInstance))
                                    if (storedQueues.isAtXYZ(tempX, tempY, tempZ)) {
                                        storedQueues.replaceAtXYZ(tempX, tempY, tempZ, tempInstance);
                                    } else {
                                        storedQueues.putInventory(tempInstance);
                                    }
                            }
                        }

                oldX = playerX;
                oldY = playerY;
                oldZ = playerZ;

                storedQueues.setLists();

                // TODO Make a packet method to send more than one InventoryInstance at once.
                for (InventoryInstance i : storedQueues.getRequestList())
                    PacketDispatcher.sendPacketToServer(new InventoryRequestPacket(i).makePacket());
            } else {
                notMoveTick++;
            }
        }
    }

    public boolean playerMoved(int x, int y, int z) {
        // return (oldX != x || oldY != y || oldZ != z);
        int range = 2;
        return (!MathHelper.withinRange(x, oldX, range) || !MathHelper.withinRange(y, oldY, range) || !MathHelper.withinRange(z, oldZ, range));
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel() {
        return "ES2-GlassesTicker";
    }

}
