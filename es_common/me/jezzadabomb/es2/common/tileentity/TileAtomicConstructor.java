package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;
import java.util.Random;

import me.jezzadabomb.es2.client.drone.DroneState;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.core.utils.Vector3F;
import me.jezzadabomb.es2.common.core.utils.Vector3I;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

public class TileAtomicConstructor extends TileES implements IEnergyHandler {

    boolean[] renderMatrix;
    TileConsole tileConsole;
    boolean registered = false;

    @Override
    public void updateEntity() {
        if (renderMatrix == null)
            constructRenderMatrix();
        if (tileConsole == null && !findNewConsole())
            return;
        if (tileConsole.isInvalid()) {
            resetState();
            return;
        }
        if (!registered && !tileConsole.isInvalid())
            registered = tileConsole.registerAtomicConstructor(this);

    }

    public void resetState() {
        tileConsole = null;
        registered = false;
    }

    private boolean findNewConsole() {
        if (!findConsoleFromNearbyConstructors())
            findConsoleNearby();
        return tileConsole != null;
    }

    private boolean findConsoleFromNearbyConstructors() {
        for (int i = -1; i < 2; i++)
            for (int j = -1; j < 2; j++)
                for (int k = -1; k < 2; k++)
                    if (!(i == 0 && j == 0 && k == 0) && isConstructor(xCoord + i, yCoord + j, zCoord + k)) {
                        TileAtomicConstructor tile = (TileAtomicConstructor) worldObj.getBlockTileEntity(xCoord + i, yCoord + j, zCoord + k);
                        if (tile.hasConsole()) {
                            tileConsole = tile.getConsole();
                            return true;
                        }
                    }
        return false;
    }

    private boolean findConsoleNearby() {
        for (int i = -1; i < 2; i++)
            for (int j = -1; j < 2; j++)
                for (int k = -1; k < 2; k++)
                    if (!(i == 0 && j == 0 && k == 0) && isConsole(xCoord + i, yCoord + j, zCoord + k)) {
                        TileConsole tile = (TileConsole) worldObj.getBlockTileEntity(xCoord + i, yCoord + j, zCoord + k);
                        tileConsole = tile;
                        return true;
                    }
        return false;
    }

    public boolean hasConsole() {
        return tileConsole != null;
    }

    public TileConsole getConsole() {
        return tileConsole;
    }

    private boolean isConstructor(int x, int y, int z) {
        return worldObj.blockHasTileEntity(x, y, z) && worldObj.getBlockTileEntity(x, y, z) instanceof TileAtomicConstructor;
    }

    private boolean isConsole(int x, int y, int z) {
        return worldObj.blockHasTileEntity(x, y, z) && worldObj.getBlockTileEntity(x, y, z) instanceof TileConsole;
    }

    public void markForUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    // private void droneMaintenance() {
    // droneList.removeAll(removeList);
    // removeList.clear();
    //
    // droneList.addAll(addList);
    // addList.clear();
    //
    // if (droneList.isEmpty()) {
    // workingList.clear();
    // return;
    // }
    //
    // for (DroneState drone : droneList) {
    // if (drone.isIdle()) {
    // removeList.add(drone);
    // }
    // }
    //
    // workingList.removeAll(removeList);
    // removeList.clear();
    // }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
    }

    @Override
    public void onDataPacket(INetworkManager networkManager, Packet132TileEntityData packet) {
        readFromNBT(packet.data);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);
    }

    public boolean[] getRenderMatrix() {
        return renderMatrix;
    }

    @Override
    public void onNeighbourBlockChange(Vector3I coordSet) {
        constructRenderMatrix();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if (i == 0 && j == 0 && k == 0 || !worldObj.blockHasTileEntity(xCoord + i, yCoord + j, zCoord + k))
                        continue;
                    if (worldObj.getBlockTileEntity(xCoord + i, yCoord + j, zCoord + k) instanceof TileAtomicConstructor)
                        ((TileAtomicConstructor) worldObj.getBlockTileEntity(xCoord + i, yCoord + j, zCoord + k)).constructRenderMatrix();
                }
            }
        }
    }

    public boolean[] constructRenderMatrix() {
        Boolean[] localArray = new Boolean[26];
        int index = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if (i == 0 && j == 0 && k == 0)
                        continue;
                    localArray[index++] = worldObj.getBlockId(xCoord + i, yCoord + j, zCoord + k) == ModBlocks.atomicConstructor.blockID;
                }
            }
        }
        return renderMatrix = new boolean[] { (!(localArray[10])), (!(localArray[0] && localArray[1] && localArray[3] && localArray[4] && localArray[9] && localArray[10] && localArray[12])), (!(localArray[9] && localArray[10] && localArray[12] && localArray[17] && localArray[18] && localArray[20] && localArray[21])), (!(localArray[11] && localArray[13] && localArray[22] && localArray[19] && localArray[21] && localArray[18] && localArray[10])), (!(localArray[10] && localArray[11] && localArray[13] && localArray[2] && localArray[5] && localArray[1] && localArray[4])), (!(localArray[12] && localArray[14] && localArray[15])), (!(localArray[4] && localArray[7] && localArray[15])), (!(localArray[15] && localArray[24] && localArray[21])), (!(localArray[15] && localArray[16] && localArray[13])), (!(localArray[21] && localArray[22] && localArray[13])), (!(localArray[12] && localArray[4] && localArray[3])), (!(localArray[4] && localArray[5] && localArray[13])), (!(localArray[12] && localArray[21] && localArray[20])), (!(localArray[15] && localArray[7] && localArray[4] && localArray[5] && localArray[13] && localArray[16] && localArray[8])), (!(localArray[4] && localArray[7] && localArray[15] && localArray[12] && localArray[3] && localArray[6] && localArray[14])), (!(localArray[15] && localArray[16] && localArray[13] && localArray[22] && localArray[25] && localArray[24] && localArray[21])), (!(localArray[15] && localArray[14] && localArray[12] && localArray[21] && localArray[20] && localArray[23] && localArray[24])), (!(localArray[10] && localArray[9] && localArray[12])), (!(localArray[10] && localArray[21] && localArray[18])), (!(localArray[10] && localArray[11] && localArray[13])), (!(localArray[10] && localArray[1] && localArray[4])) };
    }

    public boolean addDrone() {
        if (tileConsole != null)
            return tileConsole.addDroneToList(new DroneState());
        return false;
    }

    public boolean removeDrone(DroneState drone) {
        if (tileConsole != null)
            return tileConsole.removeDroneFromList(drone);
        return false;
    }

    public boolean removeDrone() {
        if (tileConsole != null)
            return tileConsole.removeDroneFromList();
        return false;
    }

    public boolean isPartRendering(int pos) {
        if (renderMatrix != null) {
            return renderMatrix[pos];
        }
        return false;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        if (tileConsole != null)
            return tileConsole.receiveEnergy(from, maxReceive, simulate);
        return 0;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        if (tileConsole != null)
            return tileConsole.extractEnergy(from, maxExtract, simulate);
        return 0;
    }

    @Override
    public boolean canInterface(ForgeDirection from) {
        return true;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        if (tileConsole != null)
            return tileConsole.getEnergyStored(from);
        return 0;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        if (tileConsole != null)
            return tileConsole.getMaxEnergyStored(from);
        return 0;
    }

    @Override
    public String toString() {
        return "Constructor" + new Vector3I(xCoord, yCoord, zCoord);
    }
}
