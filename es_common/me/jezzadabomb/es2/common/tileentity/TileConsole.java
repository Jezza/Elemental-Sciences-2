package me.jezzadabomb.es2.common.tileentity;

import java.util.BitSet;

import me.jezzadabomb.es2.client.utils.CoordSet;
import me.jezzadabomb.es2.common.core.ESLogger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;

public class TileConsole extends TileES {

    int direction;
    BitSet renderCables;

    public TileConsole() {
        direction = 0;
        renderCables = new BitSet(8);
        updateRenderCables();
    }

    @Override
    public void updateEntity() {
        if (renderCables == null)
            updateRenderCables();

    }

    @Override
    public void onNeighbourBlockChange(CoordSet coordSet) {
        updateRenderCables();
        for (int i = -1; i < 2; i++)
            for (int j = -1; j < 2; j++)
                for (int k = -1; k < 2; k++) {
                    if (i == 0 && j == 0 && k == 0 || !worldObj.blockHasTileEntity(xCoord + i, yCoord + j, zCoord + k))
                        continue;
                    if (worldObj.getBlockTileEntity(xCoord + i, yCoord + j, zCoord + k) instanceof TileConsole)
                        ((TileConsole) worldObj.getBlockTileEntity(xCoord + i, yCoord + j, zCoord + k)).updateRenderCables();
                }
    }

    public void updateRenderCables() {
        renderCables.clear();
        renderCables.set(0, isMatch(xCoord - 1, yCoord, zCoord));
        renderCables.set(1, isMatch(xCoord + 1, yCoord, zCoord));
        renderCables.set(2, isMatch(xCoord, yCoord, zCoord - 1));
        renderCables.set(3, isMatch(xCoord, yCoord, zCoord + 1));
    }

    public BitSet getRenderCables() {
        return renderCables;
    }

    private boolean isMatch(int x, int y, int z) {
        if (worldObj != null) {
            return worldObj.blockHasTileEntity(x, y, z) && (worldObj.getBlockTileEntity(x, y, z) instanceof TileConsole || worldObj.getBlockTileEntity(x, y, z) instanceof TileAtomicConstructor);
        }
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt.setInteger("direction", direction);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        direction = nbt.getInteger("direction");
        updateRenderCables();
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
        readFromNBT(pkt.data);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);
    }

    public void setOrientation(int direction) {
        this.direction = direction;
    }

    public int getOrientation() {
        return direction;
    }
}
