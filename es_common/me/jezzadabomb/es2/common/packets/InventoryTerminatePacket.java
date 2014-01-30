package me.jezzadabomb.es2.common.packets;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.common.packets.handler.CentralPacket;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class InventoryTerminatePacket extends CentralPacket {

	public String loc = "null";

	public InventoryTerminatePacket(String loc) {
		this.loc = loc;
	}

	public InventoryTerminatePacket() {
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
		if (side.isClient()) {
			if(loc == "null"){
				return;
			}
			ClientProxy.getHUDRenderer().addToRemoveList(this);
		} else {
			throw new ProtocolException("Cannot send this packet to the server!");
		}
	}

}
