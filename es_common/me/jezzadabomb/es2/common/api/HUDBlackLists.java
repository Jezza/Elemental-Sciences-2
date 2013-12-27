package me.jezzadabomb.es2.common.api;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.core.ESLogger;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HUDBlackLists {
	// TODO Add support for damage values
	/**
	 * Don't render an item on the HUD
	 */
	public static ArrayList<Item> renderBlackList = new ArrayList<Item>();
	/**
	 * This list is the blacklist for the scanner, if the block is in this list, then the HUD won't render anything to do with that inventory.
	 */
	public static ArrayList<Block> scannerBlackList = new ArrayList<Block>();
	/**
	 * This list is the for the HUD renderer, if a block is in this list, then it will render the inventory regardless.
	 */
	public static ArrayList<Block> HUDIgnoreList = new ArrayList<Block>();

	public static boolean addItemToRenderBlackList(Item item) {
		if (item == null)
			return false;
		if (!renderBlackList.contains(item)) {
			renderBlackList.add(item);
			return true;
		}
		return false;
	}

	public static boolean addItemToRenderBlackList(ItemStack itemStack) {
		return addItemToRenderBlackList(itemStack.getItem());
	}

	public static boolean addBlockToRenderBlackList(Block block) {
		return addItemToRenderBlackList(new ItemStack(block));
	}

	public static boolean addBlockToRenderBlackList(Block block, int damage) {
		return addItemToRenderBlackList(new ItemStack(block, 0, damage));
	}

	public static ArrayList<Item> getRenderBlackList() {
		return renderBlackList;
	}

	public static void refreshTestList() {
		renderBlackList.clear();
		scannerBlackList.clear();
		HUDIgnoreList.clear();

		HUDIgnoreList.add(Block.carpet);
	}

	public static boolean addBlockToScannerBlackList(Block block) {
		if (block == null)
			return false;
		if (!scannerBlackList.contains(block)) {
			scannerBlackList.add(block);
			return true;
		}
		return false;
	}

	public static boolean addBlockToHUDIgnoreList(Block block) {
		if (block == null)
			return false;
		if (!HUDIgnoreList.contains(block)) {
			HUDIgnoreList.add(block);
			return true;
		}
		return false;
	}

	public static boolean ScannerBlackListContains(Block block) {
		return scannerBlackList.contains(block);
	}

	public static boolean IgnoreListContains(Block block) {
		return HUDIgnoreList.contains(block);
	}
}
