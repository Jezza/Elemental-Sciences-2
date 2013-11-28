package me.jezzadabomb.es2.client;

import me.jezzadabomb.es2.CommonProxy;
import me.jezzadabomb.es2.client.renderers.ItemAtomicCatalystRenderer;
import me.jezzadabomb.es2.client.tickers.HUDHandler;
import me.jezzadabomb.es2.client.tickers.PlayerTicker;
import me.jezzadabomb.es2.common.ModItems;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    
    public PlayerTicker playerTicker = new PlayerTicker();
    public HUDHandler hudHandler = new HUDHandler();
    
    @Override
    public void runClientSide() {
        initItemRenderer();
        TickRegistry.registerTickHandler(playerTicker, Side.CLIENT);
        TickRegistry.registerTickHandler(hudHandler, Side.CLIENT);
        
    }
    
    public void initItemRenderer() {
        MinecraftForgeClient.registerItemRenderer(ModItems.atomicCatalyst.itemID, new ItemAtomicCatalystRenderer());
    }

}
