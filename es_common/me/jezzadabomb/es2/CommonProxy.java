package me.jezzadabomb.es2;

import me.jezzadabomb.es2.common.containers.ContainerConsole;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.handlers.HoverHandler;
import me.jezzadabomb.es2.common.core.handlers.MiscEventHandler;
import me.jezzadabomb.es2.common.lib.Strings;
import me.jezzadabomb.es2.common.tickers.CatalystTicker;
import me.jezzadabomb.es2.common.tickers.QuantumBombTicker;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import me.jezzadabomb.es2.common.tileentity.TileCrystalObelisk;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import me.jezzadabomb.es2.common.tileentity.TilePylonCrystal;
import me.jezzadabomb.es2.common.tileentity.TilePylonDummyCrystal;
import me.jezzadabomb.es2.common.tileentity.TilePylonUserDummy;
import me.jezzadabomb.es2.common.tileentity.TileQuantumStateDisruptor;
import me.jezzadabomb.es2.common.tileentity.multi.TileAtomicShredderCore;
import me.jezzadabomb.es2.common.tileentity.multi.TileAtomicShredderDummy;
import me.jezzadabomb.es2.common.tileentity.multi.TileAtomicShredderDummyCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler {

    public void runPreInit() {
        initHandlers();
    }

    public void runPostInit() {
    }

    public void runServerStart() {
        // initTickHandlers
        registerTickHandler(new QuantumBombTicker());
        registerTickHandler(new CatalystTicker());
    }

    private void initHandlers() {
        // initEventHandlers
        registerEventHandler(new MiscEventHandler());
        registerEventHandler(new HoverHandler());
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
        GameRegistry.registerTileEntity(TileCrystalObelisk.class, Strings.CRYSTAL_OBELISK);
        GameRegistry.registerTileEntity(TilePylonCrystal.class, Strings.PYLON_CRYSTAL);
        GameRegistry.registerTileEntity(TilePylonDummyCrystal.class, Strings.PYLON_CRYSTAL_DUMMY);

        GameRegistry.registerTileEntity(TilePylonUserDummy.class, Strings.PYLON_USER_DUMMY);
    }

    public void registerTickHandler(Object target) {
        FMLCommonHandler.instance().bus().register(target);
    }

    public void registerEventHandler(Object target) {
        MinecraftForge.EVENT_BUS.register(target);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                if (tileEntity instanceof TileConsole)
                    return new ContainerConsole(player.inventory, (TileConsole) tileEntity);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
