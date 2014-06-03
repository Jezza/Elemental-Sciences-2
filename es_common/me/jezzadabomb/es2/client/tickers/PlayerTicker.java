package me.jezzadabomb.es2.client.tickers;

import me.jezzadabomb.es2.api.HUDBlackLists;
import me.jezzadabomb.es2.client.hud.StoredQueues;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.PacketDispatcher;
import me.jezzadabomb.es2.common.core.network.packet.client.InventoryRequestPacket;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.helpers.MathHelper;
import me.jezzadabomb.es2.common.core.utils.helpers.PlayerHelper;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerTicker {

    private int dis = Reference.HUD_BLOCK_RANGE;
    private int oldX, oldY, oldZ, notMoveTick;
    private StoredQueues storedQueues = StoredQueues.getInstance();

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (PlayerHelper.isPlayerWearing(player, ModItems.glasses)) {
            World world = player.worldObj;
            int playerX = (int) Math.round(player.posX);
            int playerY = (int) Math.round(player.posY);
            int playerZ = (int) Math.round(player.posZ);

            if (playerMoved(playerX, playerY, playerZ) || notMoveTick++ == Reference.GLASSES_WAIT_TIMER) {
                notMoveTick = 0;
                for (int x = -dis; x <= dis; x++)
                    for (int y = -dis; y <= dis; y++)
                        for (int z = -dis; z <= dis; z++) {
                            CoordSet coordSet = new CoordSet(playerX + x, playerY + y, playerZ + z);
                            if (coordSet.isIInventory(world))
                                if (!HUDBlackLists.scannerBlackListContains(coordSet.getBlock(world)))
                                    storedQueues.putInventory(coordSet);
                        }

                oldX = playerX;
                oldY = playerY;
                oldZ = playerZ;

                storedQueues.setLists();

                if (storedQueues.canSendRequestList())
                    PacketDispatcher.sendToServer(new InventoryRequestPacket(storedQueues.getRequestList().toArray(new CoordSet[0])));
            }
        }
    }

    public boolean playerMoved(int x, int y, int z) {
        int range = 2;
        return (!MathHelper.withinRange(x, oldX, range) || !MathHelper.withinRange(y, oldY, range) || !MathHelper.withinRange(z, oldZ, range));
    }
}
