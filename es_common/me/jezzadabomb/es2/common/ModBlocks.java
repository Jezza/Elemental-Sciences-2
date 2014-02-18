package me.jezzadabomb.es2.common;

import me.jezzadabomb.es2.common.api.HUDBlackLists;
import me.jezzadabomb.es2.common.blocks.BlockAtomicConstructor;
import me.jezzadabomb.es2.common.blocks.BlockConsole;
import me.jezzadabomb.es2.common.blocks.BlockDroneBay;
import me.jezzadabomb.es2.common.blocks.BlockInventoryScanner;
import me.jezzadabomb.es2.common.blocks.BlockQuantumStateDisruptor;
import me.jezzadabomb.es2.common.blocks.BlockSolarLens;
import me.jezzadabomb.es2.common.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks {

    public static Block inventoryScanner;
    public static Block atomicConstructor;
    public static Block quantumStateDisrupter;
    public static Block solarLens;
    public static Block console;
    public static Block droneBay;

    public static void init() {
        inventoryScanner = new BlockInventoryScanner(Material.anvil, Strings.INVENTORY_SCANNER);
        atomicConstructor = new BlockAtomicConstructor(Material.anvil, Strings.ATOMIC_CONSTRUCTOR);
        quantumStateDisrupter = new BlockQuantumStateDisruptor(Material.anvil, Strings.QUANTUM_STATE_DISRUPTER);
        solarLens = new BlockSolarLens(Material.anvil, Strings.SOLAR_LENS);
        console = new BlockConsole(Material.anvil, Strings.CONSOLE);
        droneBay = new BlockDroneBay(Material.anvil, Strings.DRONE_BAY);

        initBlockRecipes();

        HUDBlackLists.addToHUDIgnoreList(inventoryScanner);
        HUDBlackLists.addToHUDIgnoreList(droneBay);
    }

    private static void initBlockRecipes() {
        // TODO all the recipes.
    }
}
