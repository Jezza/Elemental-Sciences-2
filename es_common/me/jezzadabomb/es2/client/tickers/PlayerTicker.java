package me.jezzadabomb.es2.client.tickers;

import java.util.EnumSet;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilHelpers;
import me.jezzadabomb.es2.common.hud.InventoryInstance;
import me.jezzadabomb.es2.common.hud.StoredQueues;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.packets.InventoryRequestPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;

public class PlayerTicker implements ITickHandler {

    // private int ticked = 0;
    private int dis = Reference.HUD_BLOCK_RANGE;
    private int oldX, oldY, oldZ, notMoveTick;

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        if (UtilHelpers.isWearingItem(ModItems.glasses)) {
            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

            int playerX = (int) Math.round(player.posX);
            int playerY = (int) Math.round(player.posY);
            int playerZ = (int) Math.round(player.posZ);
            World world = player.worldObj;

            if (playerMoved(playerX, playerY, playerZ) || notMoveTick == Reference.GLASSES_WAIT_TIMER) {
                notMoveTick = 0;
                for (int x = -dis; x < dis; x++) {
                    for (int y = -dis; y < dis; y++) {
                        for (int z = -dis; z < dis; z++) {
                            int tempX = playerX + x;
                            int tempY = playerY + y;
                            int tempZ = playerZ + z;
                            if (!world.isAirBlock(tempX, tempY, tempZ) && world.blockHasTileEntity(tempX, tempY, tempZ)) {
                                TileEntity tileEntity = world.getBlockTileEntity(tempX, tempY, tempZ);
                                if (tileEntity instanceof IInventory) {
                                    String name = ((IInventory) tileEntity).getInvName();
                                    StoredQueues.instance().putTempInventory(new InventoryInstance(name, tileEntity, tempX, tempY, tempZ));
                                    if (!StoredQueues.instance().isAlreadyInQueue(new InventoryInstance(name, tileEntity, tempX, tempY, tempZ))) {
                                        if (StoredQueues.instance().isAtXYZ(tempX, tempY, tempZ)) {
                                            StoredQueues.instance().replaceAtXYZ(x, y, z, new InventoryInstance(name, tileEntity, tempX, tempY, tempZ));
                                        } else {
                                            StoredQueues.instance().putInventory(name, tileEntity, tempX, tempY, tempZ);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                this.oldX = playerX;
                this.oldY = playerY;
                this.oldZ = playerZ;

                StoredQueues.instance().retainInventories(StoredQueues.instance().getTempInv());
                StoredQueues.instance().removeTemp();
                StoredQueues.instance().setLists();
                if (UtilHelpers.isWearingItem(ModItems.glasses)) {
                    for (InventoryInstance i : StoredQueues.instance().getRequestList()) {
                        PacketDispatcher.sendPacketToServer(new InventoryRequestPacket(i).makePacket());
                    }
                }
                StoredQueues.instance().clearTempInv();
            } else {
                notMoveTick++;
            }
        }
    }

    public boolean playerMoved(int x, int y, int z) {
        return (oldX != x || oldY != y || oldZ != z);
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.PLAYER);
    }

    @Override
    public String getLabel() {
        return "ES2-GlassesTicker";
    }

}
