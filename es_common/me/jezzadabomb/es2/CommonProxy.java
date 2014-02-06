package me.jezzadabomb.es2;

import me.jezzadabomb.es2.common.core.handlers.HoverHandler;
import me.jezzadabomb.es2.common.core.handlers.MiscEventHandler;
import me.jezzadabomb.es2.common.lib.Strings;
import me.jezzadabomb.es2.common.tickers.QuantumBombTicker;
import me.jezzadabomb.es2.common.tickers.CatalystTicker;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import me.jezzadabomb.es2.common.tileentity.TileQuantumStateDisrupter;
import me.jezzadabomb.es2.common.tileentity.TileSolarLens;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy {

    public QuantumBombTicker quantumBomb = new QuantumBombTicker();

    // public HoverHandler hoverHandler = new HoverHandler();

    public void runServerSide() {
        MinecraftForge.EVENT_BUS.register(new MiscEventHandler());
        MinecraftForge.EVENT_BUS.register(new HoverHandler());

    }

    public void registerTickHandlers() {
        TickRegistry.registerTickHandler(quantumBomb, Side.SERVER);
        TickRegistry.registerTickHandler(new CatalystTicker(), Side.SERVER);
    }

    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileInventoryScanner.class, Strings.INVENTORY_SCANNER);
        GameRegistry.registerTileEntity(TileAtomicConstructor.class, Strings.ATOMIC_CONSTRUCTOR);
        GameRegistry.registerTileEntity(TileQuantumStateDisrupter.class, Strings.QUANTUM_STATE_DISRUPTER);
        GameRegistry.registerTileEntity(TileSolarLens.class, Strings.SOLAR_LENS);
        GameRegistry.registerTileEntity(TileConsole.class, Strings.CONSOLE);
        GameRegistry.registerTileEntity(TileDroneBay.class, Strings.DRONE_BAY);
    }

    public void runClientSide() {
    }
}
