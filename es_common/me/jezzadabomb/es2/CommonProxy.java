package me.jezzadabomb.es2;

import me.jezzadabomb.es2.common.core.handlers.HoverHandler;
import me.jezzadabomb.es2.common.core.handlers.MiscEventHandler;
import me.jezzadabomb.es2.common.lib.Strings;
import me.jezzadabomb.es2.common.tickers.CatalystTicker;
import me.jezzadabomb.es2.common.tickers.QuantumBombTicker;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import me.jezzadabomb.es2.common.tileentity.TileQuantumStateDisruptor;
import me.jezzadabomb.es2.common.tileentity.ee.TileCreeperManager;
import me.jezzadabomb.es2.common.tileentity.ee.TileTutorialBlock;
import me.jezzadabomb.es2.common.tileentity.multi.TileAtomicShredderCore;
import me.jezzadabomb.es2.common.tileentity.multi.TileAtomicShredderDummy;
import me.jezzadabomb.es2.common.tileentity.multi.TileAtomicShredderDummyCore;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

    public QuantumBombTicker quantumBomb = new QuantumBombTicker();

    public void runServerSide() {
        MinecraftForge.EVENT_BUS.register(new MiscEventHandler());
        MinecraftForge.EVENT_BUS.register(new HoverHandler());
    }

    public void initServerHandlers() {
        EventBus bus = FMLCommonHandler.instance().bus();
        bus.register(quantumBomb);
        bus.register(new CatalystTicker());
    }

    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileInventoryScanner.class, Strings.INVENTORY_SCANNER);
        GameRegistry.registerTileEntity(TileAtomicConstructor.class, Strings.ATOMIC_CONSTRUCTOR);
        GameRegistry.registerTileEntity(TileQuantumStateDisruptor.class, Strings.QUANTUM_STATE_DISRUPTER);
        GameRegistry.registerTileEntity(TileConsole.class, Strings.CONSOLE);
        GameRegistry.registerTileEntity(TileDroneBay.class, Strings.DRONE_BAY);
        GameRegistry.registerTileEntity(TileAtomicShredderCore.class, Strings.ATOMIC_SHREDDER);
        GameRegistry.registerTileEntity(TileAtomicShredderDummy.class, Strings.ATOMIC_SHREDDER_DUMMY);
        GameRegistry.registerTileEntity(TileAtomicShredderDummyCore.class, Strings.ATOMIC_SHREDDER_DUMMY_CORE);

        GameRegistry.registerTileEntity(TileCreeperManager.class, Strings.OKU_TROLL);
        GameRegistry.registerTileEntity(TileTutorialBlock.class, Strings.BLOCK_TUTORIAL);
    }

    public void runClientSide() {
    }

    private static class TileEntityState {

    }
}
