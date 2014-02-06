package me.jezzadabomb.es2.common;

import me.jezzadabomb.es2.common.api.HUDBlackLists;
import me.jezzadabomb.es2.common.blocks.BlockAtomicConstructor;
import me.jezzadabomb.es2.common.blocks.BlockConsole;
import me.jezzadabomb.es2.common.blocks.BlockDroneBay;
import me.jezzadabomb.es2.common.blocks.BlockInventoryScanner;
import me.jezzadabomb.es2.common.blocks.BlockQuantumStateDisrupter;
import me.jezzadabomb.es2.common.blocks.BlockSolarLens;
import me.jezzadabomb.es2.common.lib.BlockIds;
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
        inventoryScanner = new BlockInventoryScanner(BlockIds.INVENTORY_SCANNER, Material.anvil, Strings.INVENTORY_SCANNER);
        atomicConstructor = new BlockAtomicConstructor(BlockIds.ATOMIC_CONSTRUCTOR, Material.anvil, Strings.ATOMIC_CONSTRUCTOR);
        quantumStateDisrupter = new BlockQuantumStateDisrupter(BlockIds.QUANTUM_STATE_DISRUPTER, Material.anvil, Strings.QUANTUM_STATE_DISRUPTER);
        solarLens = new BlockSolarLens(BlockIds.SOLAR_LENS, Material.anvil, Strings.SOLAR_LENS);
        console = new BlockConsole(BlockIds.CONSOLE, Material.anvil, Strings.CONSOLE);
        droneBay = new BlockDroneBay(BlockIds.DRONE_BAY, Material.anvil, Strings.DRONE_BAY);

        initBlockRecipes();

        HUDBlackLists.addToHUDIgnoreList(inventoryScanner);
    }

    private static void initBlockRecipes() {
        // TODO all the recipes.
    }
}
