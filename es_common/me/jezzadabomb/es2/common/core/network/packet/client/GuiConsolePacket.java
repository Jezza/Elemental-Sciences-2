package me.jezzadabomb.es2.common.core.network.packet.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import me.jezzadabomb.es2.common.containers.ContainerConsole;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.PacketUtils;
import me.jezzadabomb.es2.common.core.network.packet.IPacket;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.gui.GuiConsole;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiConsolePacket implements IPacket {

    String loc;

    public GuiConsolePacket(ContainerConsole console) {
        loc = console.loc;
    }

    public GuiConsolePacket() {
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
        if (loc != null) {
            int[] coords = UtilMethods.getArrayFromString(loc);
            if (coords == null)
                return;
            int x = coords[0];
            int y = coords[1];
            int z = coords[2];

            World world = player.worldObj;
            if (UtilMethods.isConsole(world, x, y, z)) {
                TileConsole tile = (TileConsole) world.getTileEntity(x, y, z);
                tile.closeGui(world, player);
            }
        }
    }

    @Override
    public void executeServerSide(EntityPlayer player) {
        if (loc != null) {
            int[] coords = UtilMethods.getArrayFromString(loc);
            if (coords == null)
                return;
            int x = coords[0];
            int y = coords[1];
            int z = coords[2];

            World world = player.worldObj;
            if (UtilMethods.isConsole(world, x, y, z)) {
                TileConsole tile = (TileConsole) world.getTileEntity(x, y, z);
                tile.closeGui(world, player);
            }
        }
    }

}
