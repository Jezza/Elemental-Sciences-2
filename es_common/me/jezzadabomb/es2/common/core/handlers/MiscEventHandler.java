package me.jezzadabomb.es2.common.core.handlers;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.events.PlayerArmourEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.item.ItemTossEvent;

public class MiscEventHandler {

	@ForgeSubscribe
	public void onItemDrop(ItemTossEvent event) {
		if (event.entityItem.getEntityItem().getItem().equals(ModItems.quantumStateDisrupter)){
			ElementalSciences2.proxy.quantumBomb.canAdd(event.player);
			ElementalSciences2.proxy.quantumBomb.addItemEntityToList(event.entityItem);
		}
	}
}
