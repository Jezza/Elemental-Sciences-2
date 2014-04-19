package me.jezzadabomb.es2.common.core.network.packet.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import me.jezzadabomb.es2.client.hud.InventoryInstance;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.PacketDispatcher;
import me.jezzadabomb.es2.common.core.network.PacketUtils;
import me.jezzadabomb.es2.common.core.network.packet.IPacket;
import me.jezzadabomb.es2.common.core.network.packet.server.InventoryPacket;
import me.jezzadabomb.es2.common.core.utils.Identifier;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class InventoryRequestPacket implements IPacket {

    private String loc;

    public InventoryRequestPacket(InventoryInstance inventory) {
        loc = new CoordSet(inventory.getX(), inventory.getY(), inventory.getZ()).toPacketString();
    }

    public InventoryRequestPacket() {
    }

    @Override
    public void writeBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        PacketUtils.writeStringByteBuffer(buffer, loc);
    }

    @Override
    public void readBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        loc = PacketUtils.readStringByteBuffer(buffer);
    }

    @Override
    public void executeClientSide(EntityPlayer player) {
        ESLogger.severe("Tried sending packet to wrong side!");
    }

    @Override
    public void executeServerSide(EntityPlayer player) {
        World world = player.worldObj;

        CoordSet coordSet = UtilMethods.getArrayFromString(loc);

        int x = coordSet.getX();
        int y = coordSet.getY();
        int z = coordSet.getZ();

        if (Identifier.isIInventory(world, x, y, z))
            PacketDispatcher.sendTo(new InventoryPacket(world.getTileEntity(x, y, z), loc), (EntityPlayerMP) player);
    }
}
