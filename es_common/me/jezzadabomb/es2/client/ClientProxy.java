package me.jezzadabomb.es2.client;

import me.jezzadabomb.es2.CommonProxy;
import me.jezzadabomb.es2.client.gui.GuiAtomicCatalystDebug;
import me.jezzadabomb.es2.client.models.drones.ModelConstructorDrone;
import me.jezzadabomb.es2.client.renderers.HUDRenderer;
import me.jezzadabomb.es2.client.renderers.HoverRenderer;
import me.jezzadabomb.es2.client.renderers.entity.EntityDroneRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemAtomicCatalystRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemAtomicFrameRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemConstructorDroneRenderer;
import me.jezzadabomb.es2.client.renderers.item.ItemStrengthenedIronBarRenderer;
import me.jezzadabomb.es2.client.renderers.itemblock.ItemAtomicConstructorRenderer;
import me.jezzadabomb.es2.client.renderers.itemblock.ItemConsoleRenderer;
import me.jezzadabomb.es2.client.renderers.itemblock.ItemDroneBayRenderer;
import me.jezzadabomb.es2.client.renderers.itemblock.ItemInventoryScannerRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileAtomicConstructorRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileConsoleRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileDroneBayRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileInventoryScannerRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TileObeliskRenderer;
import me.jezzadabomb.es2.client.renderers.tile.TilePylonRenderer;
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
        // TODO Yep...
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
        registerTileRenderer(TilePylonCrystal.class, new TilePylonRenderer(false));
        registerTileRenderer(TileInventoryScanner.class, new TileInventoryScannerRenderer());
        registerTileRenderer(TileAtomicConstructor.class, new TileAtomicConstructorRenderer());
        registerTileRenderer(TileConsole.class, new TileConsoleRenderer());
        registerTileRenderer(TileQuantumStateDisruptor.class, new TileQuantumStateDisruptorRenderer());
        registerTileRenderer(TileDroneBay.class, new TileDroneBayRenderer());
        registerTileRenderer(TileObelisk.class, new TileObeliskRenderer());

        // Crafting
        registerItemRenderer(ModItems.strengthenedIronBar, new ItemStrengthenedIronBarRenderer());

        // Equipment
        registerItemRenderer(ModItems.atomicFrame, new ItemAtomicFrameRenderer());
        registerItemRenderer(ModItems.constructorDrone, new ItemConstructorDroneRenderer());
        registerItemRenderer(ModBlocks.inventoryScanner, new ItemInventoryScannerRenderer());
        registerItemRenderer(ModBlocks.atomicConstructor, new ItemAtomicConstructorRenderer());
        registerItemRenderer(ModBlocks.console, new ItemConsoleRenderer());
        registerItemRenderer(ModBlocks.droneBay, new ItemDroneBayRenderer());

        // Relics
        registerItemRenderer(ModItems.atomicCatalyst, new ItemAtomicCatalystRenderer());

    }

    private void registerTileRenderer(Class<? extends TileEntity> clazz, TileEntitySpecialRenderer renderer) {
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
            case 32:
                ItemStack itemStack = player.getCurrentEquippedItem();
                if (itemStack != null && itemStack.getItem() == ModItems.atomicCatalyst)
                    return new GuiAtomicCatalystDebug(player, itemStack);
            default:
                break;
        }
        return null;
    }
}
