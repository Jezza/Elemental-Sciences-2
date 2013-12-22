package me.jezzadabomb.es2.common;

import me.jezzadabomb.es2.common.blocks.BlockInventoryScanner;
import me.jezzadabomb.es2.common.lib.BlockIds;
import me.jezzadabomb.es2.common.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks {
	
	public static Block inventoryScanner;
	
    public static void init() {
    	inventoryScanner = new BlockInventoryScanner(BlockIds.INVENTORY_SCANNER, Material.anvil, Strings.INVENTORY_SCANNER);
        initBlockRecipes();
    }
    
    public static void initBlockRecipes(){
        //TODO Recipes
    }
}
