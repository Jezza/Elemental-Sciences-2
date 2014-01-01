package me.jezzadabomb.es2;

import me.jezzadabomb.es2.client.renderers.HUDRenderer;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.handlers.MiscEventHandler;
import me.jezzadabomb.es2.common.lib.Strings;
import me.jezzadabomb.es2.common.tickers.QuantumBombTicker;
import me.jezzadabomb.es2.common.tickers.WorldTicker;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy {
	
	public HUDRenderer hudRenderer = new HUDRenderer();
	public QuantumBombTicker quantumBomb = new QuantumBombTicker();

	public void runServerSide() {
		MinecraftForge.EVENT_BUS.register(new MiscEventHandler());
	}

	public void registerTickHandlers() {
		TickRegistry.registerTickHandler(quantumBomb, Side.SERVER);
		TickRegistry.registerTickHandler(new WorldTicker(), Side.SERVER);
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileInventoryScanner.class, Strings.INVENTORY_SCANNER);
	}

	public void runClientSide() {}
}
