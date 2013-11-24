package me.jezzadabomb.es2.common.packets;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class TestPacket extends CentralPacket {

    private String text;
    
    public TestPacket(String text) {
            this.text = text;
    }

    public TestPacket() { } // Be sure to always have the default constructor in your class, or the reflection code will fail!

    @Override
    public void write(ByteArrayDataOutput out) {
            out.writeUTF(text);
    }

    @Override
    public void read(ByteArrayDataInput in) throws ProtocolException {
            text = in.readUTF();
    }

    @Override
    public void execute(EntityPlayer player, Side side) throws ProtocolException {
            if (side.isServer()) {
                    System.out.println(text);
            } else {
                    throw new ProtocolException("Cannot send this packet to the server!");
            }
    }
}
