package me.jezzadabomb.es2.common.core.network.packet.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.PacketDispatcher;
import me.jezzadabomb.es2.common.core.network.PacketUtils;
import me.jezzadabomb.es2.common.core.network.packet.IPacket;
import me.jezzadabomb.es2.common.core.network.packet.server.InventoryPacket;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class InventoryRequestPacket implements IPacket {

    private String locs;

    public InventoryRequestPacket(CoordSet... coordSets) {
        StringBuilder sb = new StringBuilder();
        for (CoordSet coordSet : coordSets)
            sb.append(new CoordSet(coordSet.getX(), coordSet.getY(), coordSet.getZ()).toPacketString() + ",");
        locs = sb.toString();
    }

    public InventoryRequestPacket() {
    }

    @Override
    public void writeBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        PacketUtils.writeStringByteBuffer(buffer, locs);
    }

    @Override
    public void readBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        locs = PacketUtils.readStringByteBuffer(buffer);
    }

    @Override
    public void executeClientSide(EntityPlayer player) {
        ESLogger.severe("Tried sending packet to wrong side!");
    }

    @Override
    public void executeServerSide(EntityPlayer player) {
        World world = player.worldObj;

        for (String loc : locs.split(",")) {
            CoordSet coordSet = CoordSet.getArrayFromString(loc);

            if (coordSet.isIInventory(world))
                PacketDispatcher.sendTo(new InventoryPacket(coordSet.getTileEntity(world), loc), (EntityPlayerMP) player);
        }
    }
}
