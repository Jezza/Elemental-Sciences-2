package me.jezzadabomb.es2.common.items;

import me.jezzadabomb.es2.CommonProxy;
import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.client.utils.CoordSet;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.tileentity.TileQuantumBomb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemQuantumStateDisruptor extends ItemES {

	public ItemQuantumStateDisruptor(int id, String name) {
		super(id, name);
		setMaxStackSize(1);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (side != 1 || !checkAround(world, x, y, z))
			return false;
		y += 1;
		if (!world.isRemote && !(new CoordSet(player).isAtXYZ(x, y, z))) {
			world.setBlock(x, y, z, ModBlocks.quantumStateDisrupter.blockID);
			stack = player.inventory.decrStackSize(player.inventory.currentItem, 1);
		}
		return true;
	}

	private boolean checkAround(World world, int x, int y, int z) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (!world.isAirBlock(x + i, y + 1, z + j) || world.isAirBlock(x + i, y, z + j)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	protected void addInformation(EntityPlayer player, ItemStack stack) {
		defaultInfoList();
		shiftList.add("The war between Life and Death");
		shiftList.add("has never been a victory.");
	}
}
