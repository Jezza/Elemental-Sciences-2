package me.jezzadabomb.es2;

import net.minecraftforge.common.MinecraftForge;
import me.jezzadabomb.es2.common.core.handlers.HoverBootsHandler;
import me.jezzadabomb.es2.common.lib.Strings;
import me.jezzadabomb.es2.common.tickers.WorldTicker;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import cpw.mods.fml.common.registry.GameRegistry;
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
    	GameRegistry.registerTileEntity(TileInventoryScanner.class, Strings.INVENTORY_SCANNER);
    }
    
    public void initEventHandlers(){
    	
    }

}
