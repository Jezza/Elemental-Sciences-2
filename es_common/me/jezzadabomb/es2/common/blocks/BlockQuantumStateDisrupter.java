package me.jezzadabomb.es2.common.blocks;

import me.jezzadabomb.es2.common.tileentity.TileQuantumStateDisrupter;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockQuantumStateDisrupter extends BlockES {

	public BlockQuantumStateDisrupter(int id, Material material, String name) {
		super(id, material, name);
		setHardness(-1.0F);
		setCreativeTab(null);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.01F, 1.0F);
	}

	@Override
	public boolean renderWithModel() {
		return true;
	}

	@Override
	public TileEntity getTileEntity() {
		return new TileQuantumStateDisrupter();
	}

}
