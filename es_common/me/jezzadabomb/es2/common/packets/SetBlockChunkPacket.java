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
    int blockID, range;

    public SetBlockChunkPacket(CoordSet coordSet, int blockID, int range) {
        this.coordSet = coordSet;
        this.blockID = blockID;
        this.range = range;
    }

    public SetBlockChunkPacket() {
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        if (range <= 0)
            range = 1;
        coordSet.writeToStream(out);
        out.writeInt(blockID);
        out.writeInt(range);
    }

    @Override
    public void read(ByteArrayDataInput in) throws ProtocolException {
        coordSet = CoordSet.readFromStream(in);
        blockID = in.readInt();
        range = in.readInt();
        if (range < 0)
            range = 0;
    }

    @Override
    public void execute(EntityPlayer player, Side side) throws ProtocolException {
        if (side.isServer()) {
            World world = player.worldObj;
            int x = coordSet.getX();
            int y = coordSet.getY();
            int z = coordSet.getZ();

            int tempRange = (int) Math.floor(range / 2);

            for (int i = -tempRange; i < tempRange + 1; i++)
                for (int j = -tempRange; j < tempRange + 1; j++)
                    for (int k = -tempRange; k < tempRange + 1; k++)
                        world.setBlock(x + i, y + j, z + k, blockID, 0, 3);
        } else {
            throw new ProtocolException("Cannot send packet to client!");
        }

    }

}
