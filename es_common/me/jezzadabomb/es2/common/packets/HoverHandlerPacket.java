package me.jezzadabomb.es2.common.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.packets.handler.CentralPacket;
import me.jezzadabomb.es2.common.tickers.HoverHandler;

public class HoverHandlerPacket extends CentralPacket {

    String name;
    boolean beginning;

    public HoverHandlerPacket(EntityPlayer player, boolean beginning) {
        name = player.username;
        this.beginning = beginning;
    }

    public HoverHandlerPacket(String player, boolean beginning) {
        name = player;
        this.beginning = beginning;
    }

    public HoverHandlerPacket() {
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeUTF(name);
        out.writeBoolean(beginning);
    }

    @Override
    public void read(ByteArrayDataInput in) throws ProtocolException {
        name = in.readUTF();
        beginning = in.readBoolean();
    }

    @Override
    public void execute(EntityPlayer player, Side side) throws ProtocolException {
        if (side.isServer()) {
//            ElementalSciences2.proxy.hoverHandler.addPlayer(player.worldObj.getPlayerEntityByName(name), beginning);
            PacketDispatcher.sendPacketToAllAround(player.posX, player.posY, player.posZ, 64, player.dimension, new HoverHandlerPacket(name, beginning).makePacket());
        } else {
//            ElementalSciences2.proxy.hoverHandler.addPlayer(player.worldObj.getPlayerEntityByName(name), beginning);
        }
    }

}
