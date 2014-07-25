package me.jezzadabomb.es2.common;

import me.jezzadabomb.es2.api.HUDBlackLists;
import me.jezzadabomb.es2.common.blocks.construction.BlockAtomicConstructor;
import me.jezzadabomb.es2.common.blocks.construction.BlockAtomicShredder;
import me.jezzadabomb.es2.common.blocks.research.BlockConsole;
import me.jezzadabomb.es2.common.blocks.construction.BlockDroneBay;
import me.jezzadabomb.es2.common.blocks.relics.BlockInventoryScanner;
import me.jezzadabomb.es2.common.blocks.power.BlockObelisk;
import me.jezzadabomb.es2.common.blocks.relics.BlockPureColour;
import me.jezzadabomb.es2.common.blocks.power.BlockPylon;
import me.jezzadabomb.es2.common.blocks.relics.BlockQuantumStateDisruptor;
import me.jezzadabomb.es2.common.blocks.relics.BlockResourceBlock;
import me.jezzadabomb.es2.common.blocks.BlockTesting;
import me.jezzadabomb.es2.common.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ModBlocks {

    public static Block resourceBlock;
    public static Block pureColour;
    public static Block inventoryScanner;
    public static Block atomicConstructor;
    public static Block quantumStateDisrupter;
    public static Block console;
    public static Block droneBay;

    public static Block pylonCrystal;
    public static Block atomicShredder;
    public static Block obelisk;

    public static Block testing;

    public static void init() {
        resourceBlock = new BlockResourceBlock(Material.anvil, Strings.RESOURCE_BLOCK);

        inventoryScanner = new BlockInventoryScanner(Material.anvil, Strings.INVENTORY_SCANNER);
        atomicConstructor = new BlockAtomicConstructor(Material.anvil, Strings.ATOMIC_CONSTRUCTOR);
        quantumStateDisrupter = new BlockQuantumStateDisruptor(Material.anvil, Strings.QUANTUM_STATE_DISRUPTER);
        console = new BlockConsole(Material.anvil, Strings.CONSOLE);
        droneBay = new BlockDroneBay(Material.anvil, Strings.DRONE_BAY);
        pylonCrystal = new BlockPylon(Material.anvil, Strings.PYLON_CRYSTAL);
        atomicShredder = new BlockAtomicShredder(Material.anvil, Strings.ATOMIC_SHREDDER);
        obelisk = new BlockObelisk(Material.anvil, Strings.OBELISK);
        pureColour = new BlockPureColour(Material.cloth, Strings.PURE_COLOUR);

        testing = new BlockTesting(Material.rock, "testing");

        HUDBlackLists.addToHUDIgnoreList(inventoryScanner);
        HUDBlackLists.addToHUDIgnoreList(droneBay);
    }

    public static void initBlockRecipes() {
        CraftingManager craftingManager = CraftingManager.getInstance();

        craftingManager.addShapelessRecipe(new ItemStack(atomicConstructor), new Object[] { ModItems.atomicFrame, ModItems.strengthenedPlate });
    }
}
