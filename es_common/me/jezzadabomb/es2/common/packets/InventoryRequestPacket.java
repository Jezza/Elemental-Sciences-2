package me.jezzadabomb.es2.common.packets;

import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.hud.InventoryInstance;
import me.jezzadabomb.es2.common.packets.handler.CentralPacket;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class InventoryRequestPacket extends CentralPacket {

    private String loc;

    public InventoryRequestPacket(InventoryInstance inventory) {
        loc = inventory.getX() + ":" + inventory.getY() + ":" + inventory.getZ();
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
            int[] coord = UtilMethods.getArrayFromString(loc);
            if (coord != null) {
                PacketDispatcher.sendPacketToPlayer(new InventoryPacket(player.worldObj.getBlockTileEntity(coord[0], coord[1], coord[2]), loc).makePacket(), (Player) player);
            }
        } else {
            throw new ProtocolException("Cannot send this packet to the client!");
        }
    }

}
