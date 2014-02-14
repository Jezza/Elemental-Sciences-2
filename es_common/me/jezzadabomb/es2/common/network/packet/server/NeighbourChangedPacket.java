package me.jezzadabomb.es2.common.network.packet.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.network.packet.IPacket;
import me.jezzadabomb.es2.common.tileentity.TileES;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.relauncher.Side;

public class NeighbourChangedPacket implements IPacket {

    public CoordSet coordSet;

    public NeighbourChangedPacket(CoordSet coordSet) {
        this.coordSet = coordSet;
    }

    public NeighbourChangedPacket() {
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
        World world = player.worldObj;

        int x = coordSet.getX();
        int y = coordSet.getY();
        int z = coordSet.getZ();

        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (tileEntity != null && tileEntity instanceof TileES) {
            TileES tileES = (TileES) tileEntity;
            tileES.onNeighbourBlockChange(this);
        }
    }

    @Override
    public void executeServerSide(EntityPlayer player) {
        ESLogger.severe("Tried sending packet to wrong side!");
    }

}
