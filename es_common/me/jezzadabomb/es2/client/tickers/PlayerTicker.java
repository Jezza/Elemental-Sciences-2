package me.jezzadabomb.es2.client.tickers;

import java.util.EnumSet;

import me.jezzadabomb.es2.common.ModItems;
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
    private int dis = 5;
    private int oldX, oldY, oldZ, notMoveTick;

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

        int playerX = (int) Math.round(player.posX);
        int playerY = (int) Math.round(player.posY);
        int playerZ = (int) Math.round(player.posZ);
        World world = player.worldObj;

        if (player.getCurrentItemOrArmor(4) != null && player.getCurrentItemOrArmor(4).getItem() == ModItems.glasses) {
            if (playerMoved(playerX, playerY, playerZ) || notMoveTick == Reference.GLASSES_WAIT_TIMER) {
                notMoveTick = 0;
                for (int x = -dis; x < dis; x++) {
                    for (int y = -dis; y < dis; y++) {
                        for (int z = -dis; z < dis; z++) {
                            if (!world.isAirBlock(playerX + x, playerY + y, playerZ + z)) {
                                if (world.blockHasTileEntity(playerX + x, playerY + y, playerZ + z)) {
                                    TileEntity tileEntity = world.getBlockTileEntity(playerX + x, playerY + y, playerZ + z);
                                    if (tileEntity instanceof IInventory) {
                                        StoredQueues.instance().putTempInventory(new InventoryInstance(((IInventory) tileEntity).getInvName(), tileEntity, playerX + x, playerY + y, playerZ + z));
                                        if (!StoredQueues.instance().isAlreadyInQueue(new InventoryInstance(((IInventory) tileEntity).getInvName(), tileEntity, playerX + x, playerY + y, playerZ + z))) {
                                            if (StoredQueues.instance().isAtXYZ(playerX + x, playerY + y, playerZ + z)) {
                                                StoredQueues.instance().replaceAtXYZ(x, y, z, new InventoryInstance(((IInventory) tileEntity).getInvName(), tileEntity, playerX + x, playerY + y, playerZ + z));
                                            } else {
                                                StoredQueues.instance().putInventory(((IInventory) tileEntity).getInvName(), tileEntity, playerX + x, playerY + y, playerZ + z);
                                            }
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
                requestPackets(player);
                StoredQueues.instance().clearTempInv();
            } else {
                notMoveTick++;
            }

        } else {
            StoredQueues.instance().getPlayer().clear();
        }
    }

    public void requestPackets(EntityPlayer player) {
        for (InventoryInstance i : StoredQueues.instance().getRequestList()) {
            PacketDispatcher.sendPacketToServer(new InventoryRequestPacket(i).makePacket());
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
