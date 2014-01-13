package me.jezzadabomb.es2.common.tileentity;

import net.minecraftforge.common.ForgeDirection;
import cofh.api.energy.IEnergyHandler;

public class TileRunningWheel extends TileES implements IEnergyHandler {
	int currentEnergy;

	@Override
	public void updateEntity() {
		
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		currentEnergy += maxReceive;
		return maxReceive;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		currentEnergy -= maxExtract;
		return maxExtract;
	}

	@Override
	public boolean canInterface(ForgeDirection from) {
		return from.flag == getBlockMetadata();
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return currentEnergy;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return currentEnergy;
	}
}
