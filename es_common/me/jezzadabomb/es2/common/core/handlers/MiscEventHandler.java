package me.jezzadabomb.es2.common.core.handlers;

import java.util.HashSet;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.tickers.QuantumBombTicker;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class MiscEventHandler {
    @SubscribeEvent
    public void onItemDrop(ItemTossEvent event) {
        if (event.entityItem.getEntityItem().getItem().equals(ModItems.quantumStateDisrupter))
            QuantumBombTicker.addItemEntityToList(event.entityItem);
    }
}
