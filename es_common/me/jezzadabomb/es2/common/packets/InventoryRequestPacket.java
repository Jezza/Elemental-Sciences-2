package me.jezzadabomb.es2.common.packets;

import me.jezzadabomb.es2.common.hud.InventoryInstance;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

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
            System.out.println(loc);
        } else {
            throw new ProtocolException("Cannot send this packet to the server!");
        }
    }

}
