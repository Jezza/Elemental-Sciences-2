package me.jezzadabomb.es2.client;

import me.jezzadabomb.es2.CommonProxy;
import me.jezzadabomb.es2.client.renderers.HUDRenderer;
import me.jezzadabomb.es2.client.renderers.QuantumBombRenderer;
import me.jezzadabomb.es2.client.renderers.entity.EntityDroneRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemAtomicCatalystRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemAtomicConstructorRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemConsoleRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemInventoryScannerRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemSolarLensRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileAtomicConstructorRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileConsoleRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileDroneBayRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileInventoryScannerRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileSolarLensRenderer;
import me.jezzadabomb.es2.client.sound.SoundHandler;
import me.jezzadabomb.es2.client.tickers.PlayerTicker;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.entities.EntityDrone;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import me.jezzadabomb.es2.common.tileentity.TileSolarLens;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    private static HUDRenderer hudRenderer = new HUDRenderer();

    public static HUDRenderer getHUDRenderer() {
        return hudRenderer;
    }

    @Override
    public void runClientSide() {
        initTickHandlers();
        initTileRenderers();
        initItemRenderer();
        initEventHandlers();
        initSoundHandler();
        initEntityHandler();
    }

    public void initEntityHandler() {
        RenderingRegistry.registerEntityRenderingHandler(EntityDrone.class, new EntityDroneRenderer());
    }

    public void initSoundHandler() {
        MinecraftForge.EVENT_BUS.register(new SoundHandler());
    }

    private void initTileRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileInventoryScanner.class, new TileInventoryScannerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAtomicConstructor.class, new TileAtomicConstructorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSolarLens.class, new TileSolarLensRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileConsole.class, new TileConsoleRenderer());
        // TODO Make an item renderer.
        ClientRegistry.bindTileEntitySpecialRenderer(TileDroneBay.class, new TileDroneBayRenderer());
    }

    private void initItemRenderer() {
        MinecraftForgeClient.registerItemRenderer(ModItems.atomicCatalyst.itemID, new ItemAtomicCatalystRenderer());
        MinecraftForgeClient.registerItemRenderer(ModBlocks.inventoryScanner.blockID, new ItemInventoryScannerRenderer());
        MinecraftForgeClient.registerItemRenderer(ModBlocks.atomicConstructor.blockID, new ItemAtomicConstructorRenderer());
        MinecraftForgeClient.registerItemRenderer(ModBlocks.console.blockID, new ItemConsoleRenderer());
        MinecraftForgeClient.registerItemRenderer(ModBlocks.solarLens.blockID, new ItemSolarLensRenderer());
    }

    private void initTickHandlers() {
        TickRegistry.registerTickHandler(new PlayerTicker(), Side.CLIENT);
    }

    public void initEventHandlers() {
        MinecraftForge.EVENT_BUS.register(hudRenderer);
        MinecraftForge.EVENT_BUS.register(new QuantumBombRenderer());
    }

}
