package me.jezzadabomb.es2.common.core.handlers;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.ModItems;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MiscEventHandler {

	@SubscribeEvent
	public void onItemDrop(ItemTossEvent event) {
		if (event.entityItem.getEntityItem().getItem().equals(ModItems.quantumStateDisrupter)){
			ElementalSciences2.proxy.quantumBomb.canAdd(event.player);
			ElementalSciences2.proxy.quantumBomb.addItemEntityToList(event.entityItem);
		}
	}
}
