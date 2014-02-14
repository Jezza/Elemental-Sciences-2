package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.network.packet.server.NeighbourChangedPacket;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileES extends TileEntity {

    public void onNeighbourBlockChange(NeighbourChangedPacket neighbourChangedPacket) {
        this.onNeighbourBlockChange(neighbourChangedPacket.coordSet);
    }

    public void onNeighbourBlockChange(CoordSet coordSet) {
    }

    public CoordSet getCoordSet() {
        return new CoordSet(xCoord, yCoord, zCoord);
    }
}
