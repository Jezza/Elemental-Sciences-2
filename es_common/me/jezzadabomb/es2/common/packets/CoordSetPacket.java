package me.jezzadabomb.es2.common.packets;

import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.packets.handler.CentralPacket;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public abstract class CoordSetPacket extends CentralPacket {

	public CoordSetPacket(CoordSet coordSet) {
		
	}

	@Override
	public void write(ByteArrayDataOutput out) {

	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {

	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {

	}

}
