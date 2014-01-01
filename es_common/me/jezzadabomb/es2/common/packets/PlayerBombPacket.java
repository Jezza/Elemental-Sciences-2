package me.jezzadabomb.es2.common.packets;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.packets.handler.CentralPacket;
import me.jezzadabomb.es2.common.packets.handler.CentralPacket.ProtocolException;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class PlayerBombPacket extends CentralPacket{

	private String player;
	
	public PlayerBombPacket(String player){
		this.player = player;
	}
	
	public PlayerBombPacket(){
	}
	
	@Override
	public void write(ByteArrayDataOutput out) {
		out.writeUTF(player);
	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException {
		player = in.readUTF();
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException {
		if(side.isClient()){
			ElementalSciences2.proxy.quantumBomb.setPlayer(this.player);
		}else{
			throw new ProtocolException("Cannot send this packet to the server!");
		}
	}

}
