package me.jezzadabomb.es2.common.items;

import me.jezzadabomb.es2.CommonProxy;
import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.core.ESLogger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemQuantumStateDisruptor extends ItemES {

	public ItemQuantumStateDisruptor(int id, String name) {
		super(id, name);
		setMaxStackSize(1);
	}

	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (!world.isRemote && ElementalSciences2.proxy.quantumBomb.canAdd(player)) {
			player.addChatMessage("Beginning to start bomb");
		}
		return itemStack;
	}

	@Override
	protected void addInformation() {
		defaultInfoList();
		shiftList.add("Life can only stand against");
		shiftList.add("Death for so long.");
	}
}
