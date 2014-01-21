package me.jezzadabomb.es2.common.packets;

import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.packets.handler.CentralPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class SetBlockChunkPacket extends CentralPacket {

    CoordSet coordSet;
    int blockID;

    public SetBlockChunkPacket(CoordSet coordSet, int blockID) {
        this.coordSet = coordSet;
        this.blockID = blockID;
    }

    public SetBlockChunkPacket() {
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        coordSet.writeToStream(out);
        out.writeInt(blockID);
    }

    @Override
    public void read(ByteArrayDataInput in) throws ProtocolException {
        coordSet = CoordSet.readFromStream(in);
        blockID = in.readInt();
    }

    @Override
    public void execute(EntityPlayer player, Side side) throws ProtocolException {
        if (side.isServer()) {
            World world = player.worldObj;
            int x = coordSet.getX();
            int y = coordSet.getY();
            int z = coordSet.getZ();

            for (int i = -1; i < 2; i++)
                for (int j = -1; j < 2; j++)
                    for (int k = -1; k < 2; k++)
                        world.setBlock(x + i, y + j, z + k, blockID, 0, 3);
        } else {
            throw new ProtocolException("Cannot send packet to client!");
        }

    }

}
