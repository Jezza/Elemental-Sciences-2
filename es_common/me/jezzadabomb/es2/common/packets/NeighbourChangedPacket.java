package me.jezzadabomb.es2.common.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.Vector3I;
import me.jezzadabomb.es2.common.packets.handler.CentralPacket;
import me.jezzadabomb.es2.common.tileentity.TileES;

public class NeighbourChangedPacket extends CentralPacket{

    public Vector3I coordSet;
    
    public NeighbourChangedPacket(Vector3I coordSet) {
        this.coordSet = coordSet;
    }
    
    public NeighbourChangedPacket() {
    }
    
    @Override
    public void write(ByteArrayDataOutput out) {
        coordSet.writeToStream(out);
    }

    @Override
    public void read(ByteArrayDataInput in) throws ProtocolException {
        coordSet = Vector3I.readFromStream(in);
    }

    @Override
    public void execute(EntityPlayer player, Side side) throws ProtocolException {
        if(side.isClient()){
            World world = player.worldObj;
            int x = coordSet.getX();
            int y = coordSet.getY();
            int z = coordSet.getZ();
            if(world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof TileES){
                TileES tileES = (TileES) world.getBlockTileEntity(x, y, z);
                tileES.onNeighbourBlockChange(this);
            }
        }else{
            throw new ProtocolException("Cannot send to server!");
        }
    }

}
