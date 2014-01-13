package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;
import java.util.Random;

import me.jezzadabomb.es2.client.drone.AtomicConstructorCoordSet;
import me.jezzadabomb.es2.client.drone.DroneState;
import me.jezzadabomb.es2.client.utils.CoordSet;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraftforge.common.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

public class TileAtomicConstructor extends TileES implements IEnergyHandler {

    boolean[] renderMatrix;
    protected EnergyStorage storage = new EnergyStorage(1000000);
    boolean finished, hasWork, breakCheck;
    ArrayList<DroneState> droneList, workingList, removeList, addList;
    ItemStack itemStack;
    int tickTiming, prevSize, renderTiming;
    int width = 2;
    int height = 2;

    public TileAtomicConstructor() {
        droneList = new ArrayList<DroneState>();
        removeList = new ArrayList<DroneState>();
        workingList = new ArrayList<DroneState>();
        addList = new ArrayList<DroneState>();
        tickTiming = prevSize = renderTiming = 0;
    }

    @Override
    public void updateEntity() {
        // TODO Handle the multiblock.
        if (++renderTiming > UtilMethods.getTicksFromSeconds(new Random().nextInt(20))) {
            constructRenderMatrix();
            renderTiming = 0;
        }
        if (renderMatrix == null)
            constructRenderMatrix();
        droneMaintenance();
        if (prevSize != droneList.size())
            markForUpdate();
        if (storage.getEnergyStored() == 0)
            return;
        distroTotalPower();
        markForUpdate();

        for (DroneState drone : droneList) {
            if (storage.extractEnergy(1, false) == 0)
                break;
            drone.moveDrone();
            if (hasWork && droneList.size() > workingList.size() && ++tickTiming > 80) {
                getRandomDroneAndAssignForWork();
                tickTiming = 0;
            }
        }
        prevSize = droneList.size();
    }

    private void distroTotalPower() {
        ArrayList<TileAtomicConstructor> atomicList = getTotalMachines();
        int powerToDistro = Math.round(storage.getEnergyStored() / (atomicList.size() + 1));
        for (TileAtomicConstructor atomic : atomicList) {
            atomic.receiveEnergy(null, storage.extractEnergy(powerToDistro, false), false);
        }
    }

    private ArrayList<TileAtomicConstructor> getTotalMachines() {
        ArrayList<TileAtomicConstructor> tempList = new ArrayList<TileAtomicConstructor>();
        if (isMatch(xCoord + 1, yCoord, zCoord)) {
            tempList.add((TileAtomicConstructor) worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord));
        }
        if (isMatch(xCoord - 1, yCoord, zCoord)) {
            tempList.add((TileAtomicConstructor) worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord));
        }
        if (isMatch(xCoord, yCoord + 1, zCoord)) {
            tempList.add((TileAtomicConstructor) worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord));
        }
        if (isMatch(xCoord, yCoord - 1, zCoord)) {
            tempList.add((TileAtomicConstructor) worldObj.getBlockTileEntity(xCoord, yCoord - 1, zCoord));
        }
        if (isMatch(xCoord, yCoord, zCoord + 1)) {
            tempList.add((TileAtomicConstructor) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + 1));
        }
        if (isMatch(xCoord, yCoord, zCoord - 1)) {
            tempList.add((TileAtomicConstructor) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1));
        }
        return tempList;
    }

    private boolean isMatch(int x, int y, int z) {
        return worldObj.blockHasTileEntity(x, y, z) && worldObj.getBlockTileEntity(x, y, z) instanceof TileAtomicConstructor;
    }

    public void markForUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    private void droneMaintenance() {
        droneList.removeAll(removeList);
        removeList.clear();

        for (DroneState drone : addList) {
            drone.setNewTargetCoords(new AtomicConstructorCoordSet(0.5F, 0.5F, 0.5F));
        }

        droneList.addAll(addList);
        addList.clear();

        if (droneList.isEmpty())
            return;

        for (DroneState drone : droneList) {
            if (drone.isIdle()) {
                removeList.add(drone);
            }
        }

        workingList.removeAll(removeList);
        removeList.clear();
    }

    private void getRandomDroneAndAssignForWork() {
        if (droneList.size() == workingList.size())
            return;
        DroneState tempDrone = getRandomDrone();
        if (tempDrone.isIdle()) {
            workingList.add(tempDrone);
            tempDrone.setWorking(new AtomicConstructorCoordSet(0.5F, 0.5F, 0.5F));
        } else {
            getRandomDroneAndAssignForWork();
        }
    }

    public boolean isDroneListEmpty() {
        return droneList.isEmpty();
    }

    public void setHasWork(ItemStack itemStack) {
        this.itemStack = itemStack;
        hasWork = true;
    }

    public ArrayList<DroneState> getDroneList() {
        return droneList;
    }

    public void removeRandomDrone() {
        if (droneList.isEmpty())
            return;
        droneList.remove(getRandomDrone());
    }

    public DroneState getRandomDrone() {
        if (droneList.isEmpty())
            return null;
        return droneList.get(new Random().nextInt(droneList.size()));
    }

    public boolean hasWork() {
        return hasWork;
    }

    public ItemStack getWork() {
        return itemStack;
    }

    public boolean getIsBelow() {
        if (worldObj == null)
            return false;
        return worldObj.getBlockId(xCoord, yCoord - 1, zCoord) == ModBlocks.atomicConstructor.blockID;
    }

    public TileAtomicConstructor getConstructor() {
        if (getIsBelow()) {
            return (TileAtomicConstructor) worldObj.getBlockTileEntity(xCoord, yCoord - 1, zCoord);
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        storage.readFromNBT(compound);
        addToDrones(compound.getInteger("droneSize"));
        boolean hasItemStack = compound.getBoolean("hasItemStack");
        if (hasItemStack) {
            NBTTagCompound tagCompound = compound.getCompoundTag("itemStack");
            itemStack = ItemStack.loadItemStackFromNBT(tagCompound);
        } else {
            itemStack = null;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        storage.writeToNBT(compound);
        compound.setInteger("droneSize", droneList.size());
        compound.setBoolean("hasItemStack", itemStack != null);
        if (itemStack != null) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            itemStack.writeToNBT(tagCompound);
            compound.setTag("itemStack", tagCompound);
        }
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

    public void addDrones(int size) {
        // if (getIsBelow()) {
        // TileAtomicConstructor below = getConstructor();
        // below.addDrones(size);
        // return;
        // }
        for (int i = 0; i < size; i++) {
            droneList.add(new DroneState(this));
        }
    }

    public void removeDrones(int size) {
        for (int i = 0; i < size; i++) {
            removeRandomDrone();
        }
    }

    public void addDroneFromNearbyTAC(DroneState droneState) {
        addList.add(droneState);
    }

    public void passDroneToNearbyTAC(TileAtomicConstructor tAC, DroneState droneState) {
        ESLogger.info("Moved drone.");
        tAC.addDroneFromNearbyTAC(droneState);
        removeList.add(droneState);
    }

    public void addToDrones(int sizeNeeded) {
        if (droneList.size() - sizeNeeded == 1 && breakCheck) {
            return;
        } else {
            breakCheck = true;
        }
        if (droneList.size() < sizeNeeded) {
            addDrones(sizeNeeded - droneList.size());
        } else if (droneList.size() > sizeNeeded) {
            removeDrones(droneList.size() - sizeNeeded);
            breakCheck = false;
        }
    }

    public boolean[] getRenderMatrix() {
        return renderMatrix;
    }

    @Override
    public void onNeighbourBlockChange(CoordSet coordSet) {
        constructRenderMatrix();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if (i == 0 && j == 0 && k == 0 || worldObj.blockHasTileEntity(xCoord + i, yCoord + j, zCoord + k))
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

    public boolean isPartRendering(int pos) {
        if (renderMatrix != null) {
            return renderMatrix[pos];
        }
        return false;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        // if (getIsBelow())
        // return getConstructor().receiveEnergy(from, maxReceive, simulate);
        return storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public boolean canInterface(ForgeDirection from) {
        return true;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return storage.getMaxEnergyStored();
    }
}
