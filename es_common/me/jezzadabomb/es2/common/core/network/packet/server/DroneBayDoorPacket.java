package me.jezzadabomb.es2.common.core.network.packet.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.packet.IPacket;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
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
//        ESLogger.info(open ? "Opening" : "Closing");
        World world = player.worldObj;

        int x = coordSet.getX();
        int y = coordSet.getY();
        int z = coordSet.getZ();

        if (UtilMethods.isDroneBay(world, x, y, z)) {
            TileDroneBay droneBay = (TileDroneBay) world.getTileEntity(x, y, z);
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

        int x = coordSet.getX();
        int y = coordSet.getY();
        int z = coordSet.getZ();

        if (UtilMethods.isDroneBay(world, x, y, z)) {
            TileDroneBay droneBay = (TileDroneBay) world.getTileEntity(x, y, z);
            ESLogger.info("Toggle");
            droneBay.toggleDoor();
        }
    }

}
