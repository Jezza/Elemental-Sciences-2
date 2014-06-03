package me.jezzadabomb.es2.client;

import me.jezzadabomb.es2.CommonProxy;
import me.jezzadabomb.es2.client.gui.GuiAtomicCatalystDebug;
import me.jezzadabomb.es2.client.gui.GuiConsole;
import me.jezzadabomb.es2.client.models.drones.ModelConstructorDrone;
import me.jezzadabomb.es2.client.renderers.HUDRenderer;
import me.jezzadabomb.es2.client.renderers.HoverRenderer;
import me.jezzadabomb.es2.client.renderers.entity.EntityDroneRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemAtomicCatalystRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemAtomicConstructorRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemConsoleRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemDroneBayRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemInventoryScannerRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemPlaceHolder64Renderer;
import me.jezzadabomb.es2.client.renderers.item.ItemPlaceHolderRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileAtomicConstructorRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileConsoleRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileCrystalObeliskRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileDroneBayRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileInventoryScannerRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TilePylonCrystalRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileQuantumStateDisruptorRenderer;
import me.jezzadabomb.es2.client.tickers.PlayerTicker;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.entities.EntityCombatDrone;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import me.jezzadabomb.es2.common.tileentity.TileObelisk;
import me.jezzadabomb.es2.common.tileentity.TilePylonCrystal;
import me.jezzadabomb.es2.common.tileentity.TilePylonCrystalDummy;
import me.jezzadabomb.es2.common.tileentity.TileQuantumStateDisruptor;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    private static HUDRenderer hudRenderer = new HUDRenderer();

    public static HUDRenderer getHUDRenderer() {
        return hudRenderer;
    }

    @Override
    public void runPreInit() {
        initHandlers();
        initRenderers();
    }

    @Override
    public void runPostInit() {

    }

    private void initHandlers() {
        // initEntityHandler
        RenderingRegistry.registerEntityRenderingHandler(EntityConstructorDrone.class, new EntityDroneRenderer(new ModelConstructorDrone(), true));
        RenderingRegistry.registerEntityRenderingHandler(EntityCombatDrone.class, new EntityDroneRenderer(new ModelConstructorDrone(), true));

        // initTickHandlers
        registerTickHandler(new PlayerTicker());

        // initEventHandlers
        registerEventHandler(hudRenderer);
        registerEventHandler(new HoverRenderer());
    }

    private void initRenderers() {
        // initTileRenderers
        registerTileEntityRenderer(TilePylonCrystal.class, new TilePylonCrystalRenderer(false));
        registerTileEntityRenderer(TilePylonCrystalDummy.class, new TilePylonCrystalRenderer(true));
        registerTileEntityRenderer(TileInventoryScanner.class, new TileInventoryScannerRenderer());
        registerTileEntityRenderer(TileAtomicConstructor.class, new TileAtomicConstructorRenderer());
        registerTileEntityRenderer(TileConsole.class, new TileConsoleRenderer());
        registerTileEntityRenderer(TileQuantumStateDisruptor.class, new TileQuantumStateDisruptorRenderer());
        registerTileEntityRenderer(TileDroneBay.class, new TileDroneBayRenderer());
        registerTileEntityRenderer(TileObelisk.class, new TileCrystalObeliskRenderer());

        // initItemRenderer
        registerItemRenderer(ModItems.atomicCatalyst, new ItemAtomicCatalystRenderer());
        registerItemRenderer(ModItems.placeHolders, new ItemPlaceHolderRenderer());
        registerItemRenderer(ModItems.placeHolders64, new ItemPlaceHolder64Renderer());
        registerItemRenderer(ModBlocks.inventoryScanner, new ItemInventoryScannerRenderer());
        registerItemRenderer(ModBlocks.atomicConstructor, new ItemAtomicConstructorRenderer());
        registerItemRenderer(ModBlocks.console, new ItemConsoleRenderer());
        registerItemRenderer(ModBlocks.droneBay, new ItemDroneBayRenderer());

    }

    private void registerTileEntityRenderer(Class<? extends TileEntity> clazz, TileEntitySpecialRenderer renderer) {
        ClientRegistry.bindTileEntitySpecialRenderer(clazz, renderer);
    }

    private void registerItemRenderer(Item item, IItemRenderer renderer) {
        MinecraftForgeClient.registerItemRenderer(item, renderer);
    }

    private void registerItemRenderer(Block block, IItemRenderer renderer) {
        registerItemRenderer(Item.getItemFromBlock(block), renderer);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                if (tileEntity instanceof TileConsole)
                    return new GuiConsole(player.inventory, (TileConsole) tileEntity);
            case 32:
                ItemStack itemStack = player.getCurrentEquippedItem();
                if (itemStack.getItem() == ModItems.atomicCatalyst)
                    return new GuiAtomicCatalystDebug(player, itemStack);
        }
        return null;
    }
}
