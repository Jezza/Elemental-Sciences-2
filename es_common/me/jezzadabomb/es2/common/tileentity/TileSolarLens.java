package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

public class TileSolarLens extends TileES implements IEnergyHandler {

    ArrayList<TileAtomicConstructor> constructorList;
    int tickCount = 0;
    int width = 2;
    int heightBonus;
    CoordSet coordSet;

    public TileSolarLens() {
        heightBonus = 1;
        constructorList = new ArrayList<TileAtomicConstructor>();
    }

    @Override
    public void updateEntity() {
        boolean tru = true;
        if (worldObj.isRemote || !worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord) || tru) {
            return;
        }
        if (searchForTileEntity())
            return;

        if (coordSet == null)
            coordSet = new CoordSet(xCoord, yCoord, zCoord);
        for (TileAtomicConstructor atomic : constructorList) {
            int power = (int) (((float) (getPowerOutput() + heightBonus) / (float) constructorList.size()) / (coordSet.distanceFrom(new CoordSet(atomic.xCoord, yCoord, atomic.zCoord)) + 1.0F));
            // atomic.receiveEnergy(ForgeDirection.DOWN, power, false);
        }

    }

    private int getPowerOutput() {
        return 10;
    }

    private boolean searchForTileEntity() {
        constructorList.clear();
        int y = loopDownUntilLastAirBlock(xCoord, yCoord, zCoord) - 1;
        heightBonus = (int) Math.floor((yCoord - y) / 2);
        if (heightBonus < 1)
            heightBonus = 1;
        if (UtilMethods.isConstructor(worldObj, xCoord, y, zCoord)) {
            constructorList.add((TileAtomicConstructor) worldObj.getBlockTileEntity(xCoord, y, zCoord));
            searchForOthers(y);
        } else {
            searchForOthers(y + 1);
        }
        return constructorList.isEmpty();
    }

    private void searchForOthers(int y) {
        int tempY = y;
        for (int i = -width; i < width + 1; i++) {
            for (int k = -width; k < width + 1; k++) {
                if (i == 0 && k == 0)
                    continue;
                tempY = loopDownUntilLastAirBlock(xCoord + i, y, zCoord + k);
                if (UtilMethods.isConstructor(worldObj, xCoord + i, tempY, zCoord + k))
                    constructorList.add((TileAtomicConstructor) worldObj.getBlockTileEntity(xCoord + i, tempY, zCoord + k));
            }
        }
    }

    private int loopDownUntilLastAirBlock(int x, int y, int z) {
        int tempY = y;
        while (hasAirUnderneath(x, tempY, z))
            tempY -= 1;
        return tempY;
    }

    private boolean hasAirUnderneath(int x, int y, int z) {
        return worldObj.isAirBlock(x, y - 1, z);
    }

    public ArrayList<TileAtomicConstructor> getConstructorList() {
        return constructorList;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public boolean canInterface(ForgeDirection from) {
        return true;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return 0;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return 0;
    }
}
