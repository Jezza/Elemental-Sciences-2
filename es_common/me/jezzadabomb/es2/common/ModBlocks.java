package me.jezzadabomb.es2.common;

import me.jezzadabomb.es2.common.api.HUDBlackLists;
import me.jezzadabomb.es2.common.blocks.BlockAtomicConstructor;
import me.jezzadabomb.es2.common.blocks.BlockAtomicShredder;
import me.jezzadabomb.es2.common.blocks.BlockAtomicShredderDummy;
import me.jezzadabomb.es2.common.blocks.BlockAtomicShredderDummyCore;
import me.jezzadabomb.es2.common.blocks.BlockConsole;
import me.jezzadabomb.es2.common.blocks.BlockCrystalObelisk;
import me.jezzadabomb.es2.common.blocks.BlockDroneBay;
import me.jezzadabomb.es2.common.blocks.BlockInventoryScanner;
import me.jezzadabomb.es2.common.blocks.BlockPlastic;
import me.jezzadabomb.es2.common.blocks.BlockQuantumStateDisruptor;
import me.jezzadabomb.es2.common.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ModBlocks {

    public static Block inventoryScanner;
    public static Block atomicConstructor;
    public static Block quantumStateDisrupter;
    public static Block console;
    public static Block droneBay;
    public static Block atomicShredder;

    public static Block atomicShredderDummy;
    public static Block atomicShredderDummyCore;
<<<<<<< HEAD
    
    public static Block plastic;
    public static Block crystalObelisk;
>>>>>>> 903946b... Starting on the obelisk

    public static void init() {
        inventoryScanner = new BlockInventoryScanner(Material.anvil, Strings.INVENTORY_SCANNER);
        atomicConstructor = new BlockAtomicConstructor(Material.anvil, Strings.ATOMIC_CONSTRUCTOR);
        quantumStateDisrupter = new BlockQuantumStateDisruptor(Material.anvil, Strings.QUANTUM_STATE_DISRUPTER);
        console = new BlockConsole(Material.anvil, Strings.CONSOLE);
        droneBay = new BlockDroneBay(Material.anvil, Strings.DRONE_BAY);
        atomicShredder = new BlockAtomicShredder(Material.anvil, Strings.ATOMIC_SHREDDER);
        atomicShredderDummy = new BlockAtomicShredderDummy(Material.anvil, Strings.ATOMIC_SHREDDER_DUMMY);
        atomicShredderDummyCore = new BlockAtomicShredderDummyCore(Material.anvil, Strings.BLOCK_PLACE_HOLDER);
<<<<<<< HEAD
        
        plastic = new BlockPlastic(Material.anvil, Strings.PLASTIC_BLOCK);
        crystalObelisk = new BlockCrystalObelisk(Material.anvil, Strings.CRYSTAL_OBELISK);
>>>>>>> 903946b... Starting on the obelisk

        HUDBlackLists.addToHUDIgnoreList(inventoryScanner);
        HUDBlackLists.addToHUDIgnoreList(droneBay);
    }

    public static void initBlockRecipes() {
        CraftingManager craftingManager = CraftingManager.getInstance();

        craftingManager.addShapelessRecipe(new ItemStack(atomicConstructor), new Object[] { ModItems.getPlaceHolderStack("atomicFrame"), ModItems.getPlaceHolderStack("strengthenedPlate") });
    }
}
