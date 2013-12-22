package me.jezzadabomb.es2;

import net.minecraftforge.common.MinecraftForge;
import me.jezzadabomb.es2.client.handlers.HoverBootsHandler;
import me.jezzadabomb.es2.common.tickers.WorldTicker;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy {

    public WorldTicker worldTicker = new WorldTicker();
    public HoverBootsHandler hoverBootsHandler = new HoverBootsHandler();

    public void runClientSide() {}
    public void initSoundHandler(){}

    public void runServerSide(){
    	
    }
    
    public void registerTickHandlers() {
        TickRegistry.registerTickHandler(worldTicker, Side.SERVER);
    }

    public void registerTileEntities() {

    }
    
    public void initEventHandlers(){
    	
    }

}
