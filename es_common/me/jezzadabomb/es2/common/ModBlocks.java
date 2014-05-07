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
import me.jezzadabomb.es2.common.blocks.BlockPylonCrystal;
import me.jezzadabomb.es2.common.blocks.BlockQuantumStateDisruptor;
import me.jezzadabomb.es2.common.blocks.BlockStrengthenedIron;
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

    public static Block strengthenedIronBlock;
    public static Block crystalObelisk;
    public static Block pylonCrystal;

    public static void init() {
        inventoryScanner = new BlockInventoryScanner(Material.anvil, Strings.INVENTORY_SCANNER);
        atomicConstructor = new BlockAtomicConstructor(Material.anvil, Strings.ATOMIC_CONSTRUCTOR);
        quantumStateDisrupter = new BlockQuantumStateDisruptor(Material.anvil, Strings.QUANTUM_STATE_DISRUPTER);
        console = new BlockConsole(Material.anvil, Strings.CONSOLE);
        droneBay = new BlockDroneBay(Material.anvil, Strings.DRONE_BAY);
        atomicShredder = new BlockAtomicShredder(Material.anvil, Strings.ATOMIC_SHREDDER);
        atomicShredderDummy = new BlockAtomicShredderDummy(Material.anvil, Strings.ATOMIC_SHREDDER_DUMMY);
        atomicShredderDummyCore = new BlockAtomicShredderDummyCore(Material.anvil, Strings.BLOCK_PLACE_HOLDER);
        crystalObelisk = new BlockCrystalObelisk(Material.anvil, Strings.CRYSTAL_OBELISK);
        pylonCrystal = new BlockPylonCrystal(Material.anvil, Strings.PYLON_CRYSTAL);
        strengthenedIronBlock = new BlockStrengthenedIron(Material.anvil, Strings.STRENGTHENED_IRON_BLOCK);

        HUDBlackLists.addToHUDIgnoreList(inventoryScanner);
        HUDBlackLists.addToHUDIgnoreList(droneBay);
    }

    public static void initBlockRecipes() {
        CraftingManager craftingManager = CraftingManager.getInstance();

        craftingManager.addShapelessRecipe(new ItemStack(atomicConstructor), new Object[] { ModItems.getPlaceHolderStack("atomicFrame"), ModItems.getPlaceHolderStack("strengthenedPlate") });
    }
}
