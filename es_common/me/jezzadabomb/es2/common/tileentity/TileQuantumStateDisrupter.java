package me.jezzadabomb.es2.common.tileentity;

import net.minecraft.block.Block;

public class TileQuantumStateDisrupter extends TileES {

	int brokenBlocks;

	public TileQuantumStateDisrupter() {
		brokenBlocks = 0;
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
					worldObj.destroyBlock(xCoord + i, yCoord, zCoord + j, brokenBlocks++ > 9 ? false : true);
			}
		}
	}

	private void checkForBeneath() {
		int tempY = yCoord - 1;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (worldObj.isAirBlock(xCoord + i, tempY, zCoord + j))
					worldObj.setBlock(xCoord + i, tempY, zCoord + j, Block.bedrock.blockID);
			}
		}
	}

}
