package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.core.utils.Vector3I;
import me.jezzadabomb.es2.common.packets.NeighbourChangedPacket;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileES extends TileEntity {
    
    public void onNeighbourBlockChange(NeighbourChangedPacket packet){
        this.onNeighbourBlockChange(packet.coordSet);
    }
    
    public void onNeighbourBlockChange(Vector3I coordSet){}
}
