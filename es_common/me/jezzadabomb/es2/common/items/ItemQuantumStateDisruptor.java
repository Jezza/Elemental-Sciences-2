package me.jezzadabomb.es2.common.items;

import me.jezzadabomb.es2.CommonProxy;
import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemQuantumStateDisruptor extends ItemES {

	public ItemQuantumStateDisruptor(int id, String name) {
		super(id, name);
		setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (!world.isRemote && ElementalSciences2.proxy.quantumBomb.canAdd(player)) {
			player.addChatMessage("Starting bomb.");
		}
		return player.inventory.decrStackSize(player.inventory.currentItem, 1);
	}

	@Override
	protected void addInformation(EntityPlayer player, ItemStack stack) {
		defaultInfoList();
		shiftList.add("The war between Life and Death");
		shiftList.add("was never a victory.");
	}
}
