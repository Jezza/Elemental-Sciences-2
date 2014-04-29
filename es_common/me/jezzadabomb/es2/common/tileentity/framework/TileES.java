package me.jezzadabomb.es2.common.tileentity.framework;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.packet.server.NeighbourChangedPacket;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;

public abstract class TileES extends TileEntity {
    public void onNeighbourBlockChange(NeighbourChangedPacket neighbourChangedPacket) {
        onNeighbourBlockChange(neighbourChangedPacket.coordSet);
    }

    public void onNeighbourBlockChange(CoordSet coordSet) {
    }

    public CoordSet getCoordSet() {
        return new CoordSet(xCoord, yCoord, zCoord);
    }

    public void markForUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + getCoordSet();
    }
}
