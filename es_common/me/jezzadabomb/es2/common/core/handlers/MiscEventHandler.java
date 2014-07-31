package me.jezzadabomb.es2.common.core.handlers;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.tickers.QuantumBombTicker;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MiscEventHandler {
    @SubscribeEvent
    public void onItemDrop(ItemTossEvent event) {
        if (event.entityItem.getEntityItem().getItem().equals(ModItems.quantumStateDisruptor))
            QuantumBombTicker.addItemEntityToList(event.entityItem);
    }
}
