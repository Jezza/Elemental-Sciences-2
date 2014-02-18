package me.jezzadabomb.es2.common;

import me.jezzadabomb.es2.common.items.ItemAtomicCatalyst;
import me.jezzadabomb.es2.common.items.ItemDebugTool;
import me.jezzadabomb.es2.common.items.ItemGlasses;
import me.jezzadabomb.es2.common.items.ItemHoverBoots;
import me.jezzadabomb.es2.common.items.ItemPlaceHolder;
import me.jezzadabomb.es2.common.items.ItemQuantumStateDisruptor;
import me.jezzadabomb.es2.common.items.framework.ItemArmourES.ArmourSlotIndex;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.Strings;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ModItems {

    public static Item atomicCatalyst;
    public static Item glasses;
    public static Item debugItem;
    public static Item hoverBoots;
    public static Item quantumStateDisrupter;
    public static Item placeHolders;

    // Recipe items.
    public static Item recipeItems;

    private static int ironRenderIndex = 2;

    public static void init() {
        // // Bohr model. Bite me :P
        atomicCatalyst = new ItemAtomicCatalyst(Strings.ATOMIC_CATALYST);
        glasses = new ItemGlasses(ArmorMaterial.IRON, ironRenderIndex, ArmourSlotIndex.HEAD, Strings.GLASSES, TextureMaps.GLASSES_LOCATION);
        hoverBoots = new ItemHoverBoots(ArmorMaterial.IRON, ironRenderIndex, ArmourSlotIndex.BOOTS, Strings.HOVER_BOOTS, TextureMaps.HOVER_BOOTS_LOCATION);
        quantumStateDisrupter = new ItemQuantumStateDisruptor(Strings.ITEM_QUANTUM_STATE_DISRUPTER);
        placeHolders = new ItemPlaceHolder(Strings.PLACEHOLDER);

        if (Reference.isDebugMode)
            debugItem = new ItemDebugTool(Strings.DEBUG_TOOL);

        initItemRecipes();
    }

    public static ItemStack getPlaceHolderStack(String item) {
        String[] names = ItemPlaceHolder.names;
        int pos = 0;
        for (String name : names) {
            if (name.equals(item)) {
                return new ItemStack(placeHolders, 1, pos);
            }
            pos++;
        }
        return null;
    }

    private static void initItemRecipes() {
        CraftingManager craftingManager = CraftingManager.getInstance();
        
        // TODO all the recipes.
        craftingManager.addRecipe(new ItemStack(Items.gold_ingot), new Object[] { "i", Character.valueOf('i'), new ItemStack(placeHolders, 1, 1) });

        // TEMP Crafting until proper method comes.
        craftingManager.addRecipe(getPlaceHolderStack("ironBar"), new Object[] { "i", Character.valueOf('i'), Blocks.heavy_weighted_pressure_plate });
        craftingManager.addRecipe(getPlaceHolderStack("frameSegment"), new Object[] { "iii", Character.valueOf('i'), new ItemStack(placeHolders, 1, 4) });
        craftingManager.addRecipe(getPlaceHolderStack("glassesLens"), new Object[] { "xix", "iyi", "xix", Character.valueOf('i'), new ItemStack(placeHolders, 1, 4), Character.valueOf('x'), Blocks.iron_bars, Character.valueOf('y'), Blocks.glass });
        craftingManager.addRecipe(getPlaceHolderStack("glassesLens"), new Object[] { "xix", "iyi", "xix", Character.valueOf('x'), new ItemStack(placeHolders, 1, 4), Character.valueOf('i'), Blocks.iron_bars, Character.valueOf('y'), Blocks.glass });
        craftingManager.addRecipe(getPlaceHolderStack("spectrumSensor"), new Object[] { "xix", "xyx", "xzx", Character.valueOf('i'), Blocks.glass_pane, Character.valueOf('x'), Blocks.redstone_block, Character.valueOf('y'), Items.diamond, Character.valueOf('z'), Blocks.daylight_detector });

        craftingManager.addRecipe(new ItemStack(glasses), new Object[] { "xix", "a a", Character.valueOf('x'), getPlaceHolderStack("frameSegment"), Character.valueOf('i'), getPlaceHolderStack("spectrumSensor"), Character.valueOf('a'), getPlaceHolderStack("glassesLens") });
    }
}
