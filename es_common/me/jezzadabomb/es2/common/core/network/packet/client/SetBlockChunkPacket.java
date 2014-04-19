package me.jezzadabomb.es2.common.core.network.packet.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.packet.IPacket;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.relauncher.Side;

public class SetBlockChunkPacket implements IPacket {

    CoordSet coordSet;
    int blockID, range;

    public SetBlockChunkPacket(CoordSet coordSet, int blockID, int range) {
        this.coordSet = coordSet;
        this.blockID = blockID;
        this.range = range;
    }

    public SetBlockChunkPacket() {
    }

    @Override
    public void writeBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        buffer.writeInt(blockID);
        buffer.writeInt(range);

        coordSet.writeBytes(buffer);
        
    }

    @Override
    public void readBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        blockID = buffer.readInt();
        range = buffer.readInt();

        coordSet = CoordSet.readBytes(buffer);
        
    }

    @Override
    public void executeClientSide(EntityPlayer player) {
        ESLogger.severe("Tried sending packet to wrong side!");
    }

    @Override
    public void executeServerSide(EntityPlayer player) {
        World world = player.worldObj;

        int x = coordSet.getX();
        int y = coordSet.getY();
        int z = coordSet.getZ();

        int tempRange = (int) Math.floor(range / 2);

        for (int i = -tempRange; i < tempRange + 1; i++)
            for (int j = -tempRange; j < tempRange + 1; j++)
                for (int k = -tempRange; k < tempRange + 1; k++)
                    world.setBlock(x + i, y + j, z + k, Block.getBlockById(blockID), 0, 3);
        
    }

}
