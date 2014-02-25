package me.jezzadabomb.es2.client;

import me.jezzadabomb.es2.CommonProxy;
import me.jezzadabomb.es2.client.renderers.HUDRenderer;
import me.jezzadabomb.es2.client.renderers.entity.EntityDroneRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemAtomicCatalystRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemAtomicConstructorRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemConsoleRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemPlaceHolder64Renderer;
import me.jezzadabomb.es2.client.renderers.item.ItemPlaceHolderRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemInventoryScannerRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemSolarLensRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileAtomicConstructorRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileConsoleRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileDroneBayRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileInventoryScannerRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileQuantumStateDisruptorRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileSolarLensRenderer;
import me.jezzadabomb.es2.client.tickers.PlayerTicker;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import me.jezzadabomb.es2.common.tileentity.TileQuantumStateDisruptor;
import me.jezzadabomb.es2.common.tileentity.TileSolarLens;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
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
        initEntityHandler();
    }

    public void initEntityHandler() {
        RenderingRegistry.registerEntityRenderingHandler(EntityConstructorDrone.class, new EntityDroneRenderer());
    }

    private void initTileRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileInventoryScanner.class, new TileInventoryScannerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAtomicConstructor.class, new TileAtomicConstructorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSolarLens.class, new TileSolarLensRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileConsole.class, new TileConsoleRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileQuantumStateDisruptor.class, new TileQuantumStateDisruptorRenderer());
        // TODO Make an item renderer.
        ClientRegistry.bindTileEntitySpecialRenderer(TileDroneBay.class, new TileDroneBayRenderer());
    }

    private void initItemRenderer() {
        MinecraftForgeClient.registerItemRenderer(ModItems.atomicCatalyst, new ItemAtomicCatalystRenderer());
        MinecraftForgeClient.registerItemRenderer(ModItems.placeHolders, new ItemPlaceHolderRenderer());
        MinecraftForgeClient.registerItemRenderer(ModItems.placeHolders64, new ItemPlaceHolder64Renderer());

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.inventoryScanner), new ItemInventoryScannerRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.atomicConstructor), new ItemAtomicConstructorRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.console), new ItemConsoleRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.solarLens), new ItemSolarLensRenderer());
    }

    private void initTickHandlers() {
        FMLCommonHandler.instance().bus().register(new PlayerTicker());
    }

    public void initEventHandlers() {
        MinecraftForge.EVENT_BUS.register(hudRenderer);
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }
}
