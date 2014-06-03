package me.jezzadabomb.es2.api;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.core.ESLogger;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HUDBlackLists {
    /**
     * Don't render an item on the HUD
     */
    public static ArrayList<Item> renderBlackList = new ArrayList<Item>();
    /**
     * This list is the blacklist for the scanner, if the block is in this list, then the HUD won't render anything to do with that inventory.
     */
    public static ArrayList<Block> scannerBlackList = new ArrayList<Block>();
    /**
     * This list is the for the HUD renderer, if a block is in this list, then it will render the inventory screen regardless.
     */
    public static ArrayList<Block> HUDIgnoreList = new ArrayList<Block>();

    /**
     * @param item
     *            The object that is being passed into the list.
     * @return Returns true if the object is in the list, false otherwise.
     */
    public static boolean addToRenderBlackList(Item item) {
        if (item == null)
            return false;
        if (!renderBlackList.contains(item)) {
            renderBlackList.add(item);
        }
        return renderBlackList.contains(item);
    }

    /**
     * @param itemStack
     *            The object that is being passed into the list.
     * @return Returns true if the object is in the list, false otherwise.
     */
    public static boolean addToRenderBlackList(ItemStack itemStack) {
        return addToRenderBlackList(itemStack.getItem());
    }

    /**
     * @param block
     *            The block being passed into the list.
     * @return Returns true if the object is in the list, false otherwise.
     */
    public static boolean addToRenderBlackList(Block block) {
        return addToRenderBlackList(block, 0);
    }

    /**
     * @param block
     *            The block being passed into the list.
     * @param damage
     *            The damage for the block.
     * @return Returns true if the object is in the list, false otherwise.
     */
    public static boolean addToRenderBlackList(Block block, int damage) {
        return addToRenderBlackList(new ItemStack(block, 1, damage));
    }

    /**
     * Adds block to the scannerBlackList
     * 
     * @param block
     *            The block to add.
     * @return Returns true if the object is in the list, false otherwise.
     */
    public static boolean addToScannerBlackList(Block block) {
        if (block == null)
            return false;
        if (!scannerBlackList.contains(block)) {
            scannerBlackList.add(block);
            return true;
        }
        return false;
    }

    /**
     * Adds a block to the HUDIgnoreList
     * 
     * @param block
     *            The block to add.
     * @return Returns true if the object is in the list, false otherwise.
     */
    public static boolean addToHUDIgnoreList(Block block) {
        if (block == null)
            return false;
        if (!HUDIgnoreList.contains(block)) {
            HUDIgnoreList.add(block);
            return true;
        }
        return false;
    }

    public static boolean renderBlackListContains(Block block) {
        return renderBlackListContains(block, 0);
    }

    public static boolean renderBlackListContains(Block block, int damage) {
        return renderBlackListContains(new ItemStack(block, 1, damage));
    }

    public static boolean renderBlackListContains(ItemStack itemStack) {
        return renderBlackListContains(itemStack.getItem());
    }

    public static boolean renderBlackListContains(Item item) {
        return renderBlackList.contains(item);
    }

    public static boolean scannerBlackListContains(Block block) {
        return scannerBlackList.contains(block);
    }

    public static boolean ignoreListContains(Block block) {
        return HUDIgnoreList.contains(block);
    }
}
