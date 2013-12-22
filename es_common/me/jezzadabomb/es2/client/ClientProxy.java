package me.jezzadabomb.es2.client;

import me.jezzadabomb.es2.CommonProxy;
import me.jezzadabomb.es2.client.handlers.HoverBootsHandler;
import me.jezzadabomb.es2.client.renderers.HUDRenderer;
import me.jezzadabomb.es2.client.renderers.ItemAtomicCatalystRenderer;
import me.jezzadabomb.es2.client.sound.SoundHandler;
import me.jezzadabomb.es2.client.tickers.PlayerTicker;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	public PlayerTicker playerTicker = new PlayerTicker();
	public static HUDRenderer hudRenderer = new HUDRenderer();

	@Override
	public void runClientSide() {
		initItemRenderer();
		initTickHandlers();
	}

	@Override
	public void initSoundHandler() {
		MinecraftForge.EVENT_BUS.register(new SoundHandler());
	}

	private void initTickHandlers() {
		TickRegistry.registerTickHandler(playerTicker, Side.CLIENT);
	}

	private void initItemRenderer() {
		MinecraftForgeClient.registerItemRenderer(ModItems.atomicCatalyst.itemID, new ItemAtomicCatalystRenderer());
	}

	public void initEventHandlers() {
		MinecraftForge.EVENT_BUS.register(hudRenderer);
		MinecraftForge.EVENT_BUS.register(hoverBootsHandler);
	}

}
