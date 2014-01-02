package me.jezzadabomb.es2.common;

import me.jezzadabomb.es2.common.items.ArmourSlotIndex;
import me.jezzadabomb.es2.common.items.ItemAtomicCatalyst;
import me.jezzadabomb.es2.common.items.ItemDebugTool;
import me.jezzadabomb.es2.common.items.ItemGlasses;
import me.jezzadabomb.es2.common.items.ItemHoverBoots;
import me.jezzadabomb.es2.common.items.ItemPlaceHolder;
import me.jezzadabomb.es2.common.items.ItemQuantumStateDisruptor;
import me.jezzadabomb.es2.common.lib.ItemIds;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.Strings;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.block.Block;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ModItems {

	public static Item atomicCatalyst;
	public static Item glasses;
	public static Item debugItem;
	public static Item hoverBoots;
	public static Item quantumStateDisrupter;
	public static Item lifeCoin;
	public static Item placeHolders;

	// Recipe items.
	public static Item recipeItems;

	private static int ironRenderIndex = 2;

	public static void init() {
		// // Bohr model. Bite me :P
		atomicCatalyst = new ItemAtomicCatalyst(ItemIds.ATOMIC_CATALYST, Strings.ATOMIC_CATALYST);
		glasses = new ItemGlasses(ItemIds.GLASSES, EnumArmorMaterial.IRON, ironRenderIndex, ArmourSlotIndex.HEAD, Strings.GLASSES, TextureMaps.GLASSES_LOCATION);
		hoverBoots = new ItemHoverBoots(ItemIds.HOVER_BOOTS, EnumArmorMaterial.IRON, ironRenderIndex, ArmourSlotIndex.BOOTS, Strings.HOVER_BOOTS, TextureMaps.HOVER_BOOTS_LOCATION);
		quantumStateDisrupter = new ItemQuantumStateDisruptor(ItemIds.QUANTUM_STATE_DISRUPTER, Strings.QUANTUM_STATE_DISRUPTER);
		placeHolders = new ItemPlaceHolder(ItemIds.PLACEHOLDER, Strings.PLACEHOLDER);

		if (Reference.CAN_DEBUG)
			debugItem = new ItemDebugTool(ItemIds.DEBUG_TOOL, Strings.DEBUG_TOOL);

		initItemRecipes();
	}

	private static void initItemRecipes() {
		// TODO all the recipes.
		CraftingManager.getInstance().addRecipe(new ItemStack(Item.ingotGold), new Object[] { "i", Character.valueOf('i'), new ItemStack(placeHolders, 1, 1) });

		//TEMP Crafting until proper method comes.
		CraftingManager.getInstance().addRecipe(new ItemStack(placeHolders, 1, 4), new Object[] { "i", Character.valueOf('i'), Block.pressurePlateIron });
		CraftingManager.getInstance().addRecipe(new ItemStack(placeHolders, 1, 3), new Object[] { "iii", Character.valueOf('i'), new ItemStack(placeHolders, 1, 4) });
		CraftingManager.getInstance().addRecipe(new ItemStack(placeHolders, 1, 2), new Object[] { "xix", "iyi", "xix", Character.valueOf('i'), new ItemStack(placeHolders, 1, 4), Character.valueOf('x'), Block.fenceIron, Character.valueOf('y'), Block.glass });
		CraftingManager.getInstance().addRecipe(new ItemStack(placeHolders, 1, 2), new Object[] { "xix", "iyi", "xix", Character.valueOf('x'), new ItemStack(placeHolders, 1, 4), Character.valueOf('i'), Block.fenceIron, Character.valueOf('y'), Block.glass });
		CraftingManager.getInstance().addRecipe(new ItemStack(placeHolders, 1, 5), new Object[] { "xix", "xyx", "xzx", Character.valueOf('i'), Block.thinGlass, Character.valueOf('x'), Block.blockRedstone, Character.valueOf('y'), Item.diamond, Character.valueOf('z'), Block.daylightSensor});
		
		
		CraftingManager.getInstance().addRecipe(new ItemStack(glasses), new Object[] { "xix", "a a", Character.valueOf('x'), new ItemStack(placeHolders, 1, 3), Character.valueOf('i'), new ItemStack(placeHolders, 1, 5), Character.valueOf('a'), new ItemStack(placeHolders, 1, 2) });

		// CraftingManager.getInstance().addRecipe(new ItemStack(ironBarRecipe), new Object[] { "iii", "isi", "iii", Character.valueOf('i'), Block.glass, Character.valueOf('s'), Block.stone });
		// CraftingManager.getInstance().addRecipe(new ItemStack(ironBarRecipe, 2), new Object[] { "i", Character.valueOf('i'), Block.pressurePlateIron });
		// CraftingManager.getInstance().addRecipe(new ItemStack(frameSegment), new Object[]{"ddd",Character.valueOf('d'), ironBarRecipe});
		// CraftingManager.getInstance().addRecipe(new ItemStack(spectrumSensor), new Object[] { "xix", "xyx", "xzx", Character.valueOf('y'), Item.diamond, Character.valueOf('i'), glassesLens, Character.valueOf('x'), Block.glass, Character.valueOf('z'), Item.redstone });
		// CraftingManager.getInstance().addRecipe(new ItemStack(glasses), new Object[] { "yiy", "x x", Character.valueOf('y'), frameSegment, Character.valueOf('i'), spectrumSensor, Character.valueOf('x'), glassesLens });
	}
}
