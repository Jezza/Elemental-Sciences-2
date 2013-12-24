package me.jezzadabomb.es2.common;

import me.jezzadabomb.es2.common.items.ArmourSlotIndex;
import me.jezzadabomb.es2.common.items.ItemAtomicCatalyst;
import me.jezzadabomb.es2.common.items.ItemDebugTool;
import me.jezzadabomb.es2.common.items.ItemGlasses;
import me.jezzadabomb.es2.common.items.ItemHoverBoots;
import me.jezzadabomb.es2.common.lib.ItemIds;
import me.jezzadabomb.es2.common.lib.Strings;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;

public class ModItems {

	public static Item atomicCatalyst;
	public static Item glasses;
	public static Item debugItem;
	public static Item hoverBoots;
	// Recipe items.
	public static Item recipeItems;

	private static int ironRenderIndex = 2;

	public static void init() {
		// // Bohr model. Bite me :P
		atomicCatalyst = new ItemAtomicCatalyst(ItemIds.ATOMIC_CATALYST, Strings.ATOMIC_CATALYST);
		glasses = new ItemGlasses(ItemIds.GLASSES, EnumArmorMaterial.IRON, ironRenderIndex, ArmourSlotIndex.HEAD, Strings.GLASSES, TextureMaps.GLASSES_LOCATION);
		hoverBoots = new ItemHoverBoots(ItemIds.HOVER_BOOTS, EnumArmorMaterial.IRON, ironRenderIndex, ArmourSlotIndex.BOOTS, Strings.HOVER_BOOTS, TextureMaps.HOVER_BOOTS_LOCATION);

		debugItem = new ItemDebugTool(ItemIds.DEBUG_TOOL, Strings.DEBUG_TOOL);
		initItemRecipes();
	}

	private static void initItemRecipes() {
		// TODO all the recipes.
		// CraftingManager.getInstance().addRecipe(new ItemStack(ironBarRecipe), new Object[] { "iii", "isi", "iii", Character.valueOf('i'), Block.glass, Character.valueOf('s'), Block.stone });
		// CraftingManager.getInstance().addRecipe(new ItemStack(ironBarRecipe, 2), new Object[] { "i", Character.valueOf('i'), Block.pressurePlateIron });
		// CraftingManager.getInstance().addRecipe(new ItemStack(glassesLens), new Object[] { "xix", "iyi", "xix", Character.valueOf('i'), ironBarRecipe, Character.valueOf('x'), Block.fenceIron, Character.valueOf('y'), Block.glass });
		// CraftingManager.getInstance().addRecipe(new ItemStack(glassesLens), new Object[] { "xix", "iyi", "xix", Character.valueOf('x'), ironBarRecipe, Character.valueOf('i'), Block.fenceIron, Character.valueOf('y'), Block.glass });
		// CraftingManager.getInstance().addRecipe(new ItemStack(frameSegment), new Object[]{"ddd",Character.valueOf('d'), ironBarRecipe});
		// CraftingManager.getInstance().addRecipe(new ItemStack(spectrumSensor), new Object[] { "xix", "xyx", "xzx", Character.valueOf('y'), Item.diamond, Character.valueOf('i'), glassesLens, Character.valueOf('x'), Block.glass, Character.valueOf('z'), Item.redstone });
		// CraftingManager.getInstance().addRecipe(new ItemStack(glasses), new Object[] { "yiy", "x x", Character.valueOf('y'), frameSegment, Character.valueOf('i'), spectrumSensor, Character.valueOf('x'), glassesLens });
	}
}
