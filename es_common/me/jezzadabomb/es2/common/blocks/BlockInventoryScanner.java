package me.jezzadabomb.es2.common.blocks;

import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockInventoryScanner extends BlockES {

	public BlockInventoryScanner(int par1, Material par2Material, String name) {
		super(par1, par2Material, name);
		setHardness(2.5F);
		setBlockBounds(0F, 0F, 0F, 1F, 0.1F, 1F);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta) {
		if (world.blockHasTileEntity(x, y, z)) {
			if (world.getBlockTileEntity(x, y, z) instanceof TileInventoryScanner) {
				TileInventoryScanner tile = (TileInventoryScanner) world.getBlockTileEntity(x, y, z);
				tile.sendTerminatePacket();
			}
		}
		super.breakBlock(world, x, y, z, id, meta);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		if(world.blockHasTileEntity(x, y - 1, z)){
			return (world.getBlockTileEntity(x, y - 1, z) instanceof IInventory);
		}
		return false;
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileInventoryScanner();
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
}
