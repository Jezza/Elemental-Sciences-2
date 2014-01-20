package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

import me.jezzadabomb.es2.client.drone.DroneState;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.Vector3I;
import me.jezzadabomb.es2.common.entities.EntityDrone;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraftforge.common.ForgeDirection;

public class TileConsole extends TileES implements IEnergyHandler {

    protected EnergyStorage storage = new EnergyStorage(1000000);
    ArrayList<TileAtomicConstructor> constructorList, utilList;
    ArrayList<EntityDrone> droneList, workingList, removeList;
    int direction, prevDroneSize, prevWorkingSize;
    BitSet renderCables;

    public TileConsole() {
        this(null);
    }

    public TileConsole(TileConsole master) {
        constructorList = new ArrayList<TileAtomicConstructor>();
        utilList = new ArrayList<TileAtomicConstructor>();
        droneList = new ArrayList<EntityDrone>();
        workingList = new ArrayList<EntityDrone>();
        removeList = new ArrayList<EntityDrone>();
        prevDroneSize = 0;
        prevWorkingSize = 0;
        direction = 0;
        renderCables = new BitSet(4);
        updateRenderCables();
    }

    public Vector3I getCoordSet() {
        return new Vector3I(xCoord, yCoord, zCoord);
    }

    @Override
    public void updateEntity() {
        if (renderCables == null)
            updateRenderCables();

        droneMaintenance();
        if (workingList.size() != prevWorkingSize || droneList.size() != prevDroneSize)
            markForUpdate();

        for (TileAtomicConstructor atomic : constructorList)
            if (atomic.isInvalid())
                utilList.add(atomic);

        if (utilList.size() > 0) {
            constructorList.removeAll(utilList);
            disconnectAll(false);
            utilList.clear();
        }

        prevDroneSize = droneList.size();
        prevWorkingSize = droneList.size();

    }

    private void droneMaintenance() {
        droneList.removeAll(removeList);
        removeList.clear();

        if (droneList.isEmpty()) {
            workingList.clear();
            return;
        }

        for (EntityDrone drone : workingList)
            if (drone.isIdle())
                removeList.add(drone);

        workingList.removeAll(removeList);
        removeList.clear();
    }

    public void disconnectAll(boolean resetMaster) {
        for (TileAtomicConstructor atomic : constructorList)
            atomic.resetState();
        constructorList.clear();
        droneList.clear();
    }

    public ArrayList<EntityDrone> getDroneList() {
        return droneList;
    }

    @Override
    public void onNeighbourBlockChange(Vector3I coordSet) {
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

    public int getDroneSize() {
        return droneList.size();
    }

    public boolean addDrone(EntityDrone drone) {
        droneList.add(drone);
        return droneList.contains(drone);
    }

    public boolean removeDroneFromList(EntityDrone drone) {
        if (!removeList.contains(drone))
            removeList.add(drone);
        return removeList.contains(drone);
    }

    public boolean removeDroneFromList() {
        EntityDrone drone = getRandomDrone();
        if (droneList.contains(drone) && !removeList.contains(drone)){
            removeList.add(drone);
            drone.setDead();
        }
        return removeList.contains(drone);
    }

    public EntityDrone getRandomDrone() {
        return droneList.get(new Random().nextInt(droneList.size()));
    }

    public void markForUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
        if (worldObj != null)
            return worldObj.blockHasTileEntity(x, y, z) && (worldObj.getBlockTileEntity(x, y, z) instanceof TileConsole || worldObj.getBlockTileEntity(x, y, z) instanceof TileAtomicConstructor);
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("direction", direction);
        tag.setInteger("droneSize", droneList.size());
        tag.setInteger("workingSize", workingList.size());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        direction = tag.getInteger("direction");

        processDroneNBT(tag.getInteger("droneSize"), tag.getInteger("workingSize"));
        updateRenderCables();
    }

    private void processDroneNBT(int droneSize, int workingSize) {
        if (droneList.size() == droneSize && workingList.size() == workingSize)
            return;

        if (droneList.size() < droneSize) {
            // addDrones(droneSize - droneList.size());
        } else if (droneList.size() > droneSize) {
            int numberToRemove = droneList.size() - droneSize;
            while (removeList.size() < numberToRemove)
                removeDroneFromList();
        }

        if (workingList.size() < workingSize) {
            int needed = workingSize - workingList.size();
            int index = 0;
            for (EntityDrone drone : droneList) {
                if (index == needed)
                    break;
                drone.setWorking();
                index++;
            }
        } else if (workingList.size() > workingSize) {
            int needed = workingList.size() - workingSize;
            int index = 0;
            for (EntityDrone drone : droneList) {
                if (index == needed)
                    break;
                drone.setIdle();
                index++;
            }
        }

    }

    public boolean registerDrone(EntityDrone drone) {
        if (!droneList.contains(drone))
            droneList.add(drone);
        return droneList.contains(drone);
    }

    private boolean isConstructor(int x, int y, int z) {
        return worldObj.blockHasTileEntity(x, y, z) && worldObj.getBlockTileEntity(x, y, z) instanceof TileAtomicConstructor;
    }

    private boolean isConsole(int x, int y, int z) {
        return worldObj.blockHasTileEntity(x, y, z) && worldObj.getBlockTileEntity(x, y, z) instanceof TileConsole;
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

    public boolean registerAtomicConstructor(TileAtomicConstructor atomic, ArrayList<EntityDrone> droneList) {
        if (!constructorList.contains(atomic))
            constructorList.add(atomic);
        this.droneList.addAll(droneList);
        return constructorList.contains(atomic);
    }

    public void setOrientation(int direction) {
        this.direction = direction;
    }

    public int getOrientation() {
        return direction;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
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
