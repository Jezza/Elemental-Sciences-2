package me.jezzadabomb.es2.common.tileentity;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class TileQuantumStateDisrupter extends TileES {

	public TileQuantumStateDisrupter() {
	}

	@Override
	public void updateEntity() {
		checkForBlocks();
		checkForBeneath();
	}

	private void checkForBlocks() {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i == 0 && j == 0)
					continue;
				if (!worldObj.isAirBlock(xCoord + i, yCoord, zCoord + j))
					worldObj.setBlockToAir(xCoord + i, yCoord, zCoord + j);
			}
		}
	}

	private void checkForBeneath() {
		int tempY = yCoord - 1;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (worldObj.isAirBlock(xCoord + i, tempY, zCoord + j))
					worldObj.setBlock(xCoord + i, tempY, zCoord + j, Blocks.bedrock);
			}
		}
	}

}
