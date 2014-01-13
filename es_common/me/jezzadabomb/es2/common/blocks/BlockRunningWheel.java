package me.jezzadabomb.es2.common.blocks;

import me.jezzadabomb.es2.common.tileentity.TileRunningWheel;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

public class BlockRunningWheel extends BlockES {

	public BlockRunningWheel(int id, Material material, String name) {
		super(id, material, name);
	}

	@Override
	public boolean renderWithModel() {
		return false;
	}

	@Override
	public TileEntity getTileEntity() {
		return new TileRunningWheel();
	}

}
