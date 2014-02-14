package me.jezzadabomb.es2.common.network.packet;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface IPacket {

    public void writeBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException;

    public void readBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException;

    public void executeClientSide(EntityPlayer player);
    
    public void executeServerSide(EntityPlayer player);
}
