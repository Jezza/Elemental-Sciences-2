package me.jezzadabomb.es2.common.core.network.packet.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import me.jezzadabomb.es2.common.core.network.packet.IPacket;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class DroneBayDoorPacket implements IPacket {

    CoordSet coordSet;
    boolean open;

    public DroneBayDoorPacket(TileDroneBay droneBay, boolean open) {
        coordSet = droneBay.getCoordSet();
        this.open = open;
    }

    public DroneBayDoorPacket() {
    }

    @Override
    public void writeBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        coordSet.writeBytes(buffer);
        buffer.writeBoolean(open);
    }

    @Override
    public void readBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        coordSet = CoordSet.readBytes(buffer);
        open = buffer.readBoolean();
    }

    @Override
    public void executeClientSide(EntityPlayer player) {
        World world = player.worldObj;

        if (coordSet.isDroneBay(world)) {
            TileDroneBay droneBay = (TileDroneBay) coordSet.getTileEntity(world);
            if (open) {
                droneBay.openHatch();
            } else {
                droneBay.closeHatch();
            }

        }
    }

    @Override
    public void executeServerSide(EntityPlayer player) {
        World world = player.worldObj;

        if (coordSet.isDroneBay(world)) {
            TileDroneBay droneBay = (TileDroneBay) coordSet.getTileEntity(world);
            droneBay.toggleDoor();
        }
    }

}
