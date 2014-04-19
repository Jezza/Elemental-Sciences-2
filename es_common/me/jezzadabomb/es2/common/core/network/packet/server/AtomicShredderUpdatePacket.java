package me.jezzadabomb.es2.common.core.network.packet.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.packet.IPacket;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.tileentity.multi.TileAtomicShredderCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.ByteBufUtils;

public class AtomicShredderUpdatePacket implements IPacket {

    NBTTagCompound tag = new NBTTagCompound();

    public AtomicShredderUpdatePacket(TileAtomicShredderCore core) {
        core.writeToNBT(tag);
    }

    public AtomicShredderUpdatePacket() {
    }

    @Override
    public void writeBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        ByteBufUtils.writeTag(buffer, tag);
    }

    @Override
    public void readBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        tag = ByteBufUtils.readTag(buffer);
    }

    @Override
    public void executeClientSide(EntityPlayer player) {
        World world = player.worldObj;

        CoordSet coordSet = CoordSet.createFromMinecraftTag(tag);

        TileEntity tileEntity = world.getTileEntity(coordSet.getX(), coordSet.getY(), coordSet.getZ());
        if (tileEntity instanceof TileAtomicShredderCore)
            ((TileAtomicShredderCore) tileEntity).readFromNBT(tag);
    }

    @Override
    public void executeServerSide(EntityPlayer player) {
        ESLogger.severe("Packet sent to the wrong side!");
    }

}
