package me.jezzadabomb.es2.common;

import me.jezzadabomb.es2.common.items.ItemAtomicCatalyst;
import me.jezzadabomb.es2.common.items.ItemDebugTool;
import me.jezzadabomb.es2.common.items.ItemGlasses;
import me.jezzadabomb.es2.common.items.ItemHoverBoots;
import me.jezzadabomb.es2.common.items.ItemPlaceHolder;
import me.jezzadabomb.es2.common.items.ItemQuantumStateDisruptor;
import me.jezzadabomb.es2.common.items.framework.ArmourSlotIndex;
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
    public static Item placeHolders;

    // Recipe items.
    public static Item recipeItems;

    private static int ironRenderIndex = 2;

    public static void init() {
        // // Bohr model. Bite me :P
        atomicCatalyst = new ItemAtomicCatalyst(ItemIds.ATOMIC_CATALYST, Strings.ATOMIC_CATALYST);
        glasses = new ItemGlasses(ItemIds.GLASSES, EnumArmorMaterial.IRON, ironRenderIndex, ArmourSlotIndex.HEAD, Strings.GLASSES, TextureMaps.GLASSES_LOCATION);
        hoverBoots = new ItemHoverBoots(ItemIds.HOVER_BOOTS, EnumArmorMaterial.IRON, ironRenderIndex, ArmourSlotIndex.BOOTS, Strings.HOVER_BOOTS, TextureMaps.HOVER_BOOTS_LOCATION);
        quantumStateDisrupter = new ItemQuantumStateDisruptor(ItemIds.QUANTUM_STATE_DISRUPTER, Strings.ITEM_QUANTUM_STATE_DISRUPTER);
        placeHolders = new ItemPlaceHolder(ItemIds.PLACEHOLDER, Strings.PLACEHOLDER);

        if (Reference.CAN_DEBUG)
            debugItem = new ItemDebugTool(ItemIds.DEBUG_TOOL, Strings.DEBUG_TOOL);

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
        craftingManager.addRecipe(new ItemStack(Item.ingotGold), new Object[] { "i", Character.valueOf('i'), new ItemStack(placeHolders, 1, 1) });

        // TEMP Crafting until proper method comes.
        craftingManager.addRecipe(getPlaceHolderStack("ironBar"), new Object[] { "i", Character.valueOf('i'), Block.pressurePlateIron });
        craftingManager.addRecipe(getPlaceHolderStack("frameSegment"), new Object[] { "iii", Character.valueOf('i'), new ItemStack(placeHolders, 1, 4) });
        craftingManager.addRecipe(getPlaceHolderStack("glassesLens"), new Object[] { "xix", "iyi", "xix", Character.valueOf('i'), new ItemStack(placeHolders, 1, 4), Character.valueOf('x'), Block.fenceIron, Character.valueOf('y'), Block.glass });
        craftingManager.addRecipe(getPlaceHolderStack("glassesLens"), new Object[] { "xix", "iyi", "xix", Character.valueOf('x'), new ItemStack(placeHolders, 1, 4), Character.valueOf('i'), Block.fenceIron, Character.valueOf('y'), Block.glass });
        craftingManager.addRecipe(getPlaceHolderStack("spectrumSensor"), new Object[] { "xix", "xyx", "xzx", Character.valueOf('i'), Block.thinGlass, Character.valueOf('x'), Block.blockRedstone, Character.valueOf('y'), Item.diamond, Character.valueOf('z'), Block.daylightSensor });

        craftingManager.addRecipe(new ItemStack(glasses), new Object[] { "xix", "a a", Character.valueOf('x'), getPlaceHolderStack("frameSegment"), Character.valueOf('i'), getPlaceHolderStack("spectrumSensor"), Character.valueOf('a'), getPlaceHolderStack("glassesLens") });
    }
}
