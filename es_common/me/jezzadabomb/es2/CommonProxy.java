package me.jezzadabomb.es2;

import me.jezzadabomb.es2.common.tickers.WorldTicker;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy {

    public WorldTicker worldTicker = new WorldTicker();
    
    public void runClientSide() {
    }

    public void registerTickHandlers() {
        TickRegistry.registerTickHandler(worldTicker, Side.SERVER);
    }

    public void registerTileEntities() {

    }

}
