package me.jezzadabomb.es2.common.packets;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.hud.InventoryInstance;
import me.jezzadabomb.es2.common.packets.handler.CentralPacket;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class InventoryRequestPacket extends CentralPacket {

    private String loc;

    public InventoryRequestPacket(InventoryInstance inventory) {
        loc = new CoordSet(inventory.getX(), inventory.getY(), inventory.getZ()).toPacketString();
    }

    public InventoryRequestPacket() {
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeUTF(loc);
    }

    @Override
    public void read(ByteArrayDataInput in) throws ProtocolException {
        loc = in.readUTF();
    }

    @Override
    public void execute(EntityPlayer player, Side side) throws ProtocolException {
        if (side.isServer()) {
            World world = player.worldObj;
            int[] coord = UtilMethods.getArrayFromString(loc);
            if (coord == null)
                return;

            int x = coord[0];
            int y = coord[1];
            int z = coord[2];
            if (UtilMethods.isIInventory(world, x, y, z))
                PacketDispatcher.sendPacketToPlayer(new InventoryPacket(world.getBlockTileEntity(x, y, z), loc).makePacket(), (Player) player);
        } else {
            throw new ProtocolException("Cannot send this packet to the client!");
        }
    }

}
