package me.jezzadabomb.es2.client;

import me.jezzadabomb.es2.CommonProxy;
import me.jezzadabomb.es2.client.renderers.HUDRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemAtomicCatalystRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemInventoryScannerRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileInventoryScannerRenderer;
import me.jezzadabomb.es2.client.sound.SoundHandler;
import me.jezzadabomb.es2.client.tickers.PlayerTicker;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.handlers.HoverBootsHandler;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	public PlayerTicker playerTicker = new PlayerTicker();
	public static HUDRenderer hudRenderer = new HUDRenderer();
	public HoverBootsHandler hoverBootsHandler = new HoverBootsHandler();

	@Override
	public void runClientSide() {
		initTickHandlers();
		initTileRenderers();
		initItemRenderer();
	}

	@Override
	public void initSoundHandler() {
		MinecraftForge.EVENT_BUS.register(new SoundHandler());
	}

	private void initTileRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileInventoryScanner.class, new TileInventoryScannerRenderer());
	}

	private void initItemRenderer() {
		MinecraftForgeClient.registerItemRenderer(ModItems.atomicCatalyst.itemID, new ItemAtomicCatalystRenderer());
		MinecraftForgeClient.registerItemRenderer(ModBlocks.inventoryScanner.blockID, new ItemInventoryScannerRenderer());
	}

	private void initTickHandlers() {
		TickRegistry.registerTickHandler(playerTicker, Side.CLIENT);
	}

	public void initEventHandlers() {
		ESLogger.debug("Registering handler");
		MinecraftForge.EVENT_BUS.register(hudRenderer);
		ESLogger.debug("Registered handler");
		MinecraftForge.EVENT_BUS.register(hoverBootsHandler);
	}

}
