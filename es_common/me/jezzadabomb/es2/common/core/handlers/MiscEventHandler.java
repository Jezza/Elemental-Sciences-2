package me.jezzadabomb.es2.common.core.handlers;

import java.awt.event.ItemEvent;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.item.ItemTossEvent;

public class MiscEventHandler {

	@ForgeSubscribe
	public void onItemDrop(ItemTossEvent event){
		ESLogger.info(event);
		if(event.entityItem.getEntityItem().getItem().equals(ModItems.quantumStateDisruptor)){
			ESLogger.info("Oh noes, " + event.player.username + " dropped something.");
			ElementalSciences2.proxy.quantumBomb.canAdd(event.player);
		}
	}
	
}
