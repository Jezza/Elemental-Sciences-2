package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.packet.server.NeighbourChangedPacket;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public abstract class TileES extends TileEntity {

    public void onNeighbourBlockChange(NeighbourChangedPacket neighbourChangedPacket) {
        this.onNeighbourBlockChange(neighbourChangedPacket.coordSet);
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

    public abstract Object getGui(int id, Side side, EntityPlayer player);

    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F) < 64;
    }
}
