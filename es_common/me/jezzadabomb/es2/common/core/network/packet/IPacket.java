package me.jezzadabomb.es2.common.core.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

public interface IPacket {

    public void writeBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException;

    public void readBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException;

    public void executeClientSide(EntityPlayer player);
    
    public void executeServerSide(EntityPlayer player);
}
