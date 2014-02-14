package me.jezzadabomb.es2.common.blocks;

import me.jezzadabomb.es2.common.tileentity.TileSolarLens;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

public class BlockSolarLens extends BlockES {

	public BlockSolarLens(Material material, String name) {
		super(material, name);
		setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
	}

	@Override
	public boolean renderWithModel() {
		return true;
	}

	@Override
	public TileEntity getTileEntity() {
		return new TileSolarLens();
	}

}
