package me.jezzadabomb.es2.common.core.network.packet.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.packet.IPacket;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class ConsoleInfoPacket implements IPacket {

    CoordSet coordSet;

    public ConsoleInfoPacket(TileConsole tileConsole) {
        coordSet = tileConsole.getCoordSet();
    }

    public ConsoleInfoPacket() {
    }

    @Override
    public void writeBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        coordSet.writeBytes(buffer);
    }

    @Override
    public void readBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        coordSet = CoordSet.readBytes(buffer);
    }

    @Override
    public void executeClientSide(EntityPlayer player) {
        ESLogger.severe("Tried sending packet to wrong side!");
    }

    @Override
    public void executeServerSide(EntityPlayer player) {
        TileConsole tileConsole = (TileConsole) coordSet.getTileEntity(player.worldObj);

        String string = tileConsole.toString();

        List<String> list = Arrays.asList(string.split(System.lineSeparator()));

        for (String str : list)
            player.addChatComponentMessage(new ChatComponentText(str));
    }

}
