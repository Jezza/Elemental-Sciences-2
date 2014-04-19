package me.jezzadabomb.es2.common;

import me.jezzadabomb.es2.common.items.ItemAtomicCatalyst;
import me.jezzadabomb.es2.common.items.ItemBlockHolder;
import me.jezzadabomb.es2.common.items.ItemGlasses;
import me.jezzadabomb.es2.common.items.ItemHoverBoots;
import me.jezzadabomb.es2.common.items.ItemPlaceHolder;
import me.jezzadabomb.es2.common.items.ItemPlaceHolder64;
import me.jezzadabomb.es2.common.items.ItemQuantumStateDisruptor;
import me.jezzadabomb.es2.common.items.debug.ItemDebugTool;
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
    public static Item hoverBoots;
    public static Item quantumStateDisrupter;
    public static Item placeHolders;
    public static Item placeHolders64;
    public static Item atomicShredderHolder;
    public static Item atomicShredderDummy;

    public static Item debugItem;
    public static Item ringControl;

    private static int ironRenderIndex = 2;

    public static void init() {
        atomicCatalyst = new ItemAtomicCatalyst(Strings.ATOMIC_CATALYST);
        glasses = new ItemGlasses(ArmorMaterial.IRON, ironRenderIndex, ArmourSlotIndex.HEAD, Strings.GLASSES, TextureMaps.GLASSES_LOCATION);
        hoverBoots = new ItemHoverBoots(ArmorMaterial.IRON, ironRenderIndex, ArmourSlotIndex.BOOTS, Strings.HOVER_BOOTS, TextureMaps.HOVER_BOOTS_LOCATION);
        quantumStateDisrupter = new ItemQuantumStateDisruptor(Strings.ITEM_QUANTUM_STATE_DISRUPTER);
        placeHolders = new ItemPlaceHolder(Strings.PLACEHOLDER);
        placeHolders64 = new ItemPlaceHolder64(Strings.PLACEHOLDER_64);

        atomicShredderHolder = new ItemBlockHolder(ModBlocks.atomicShredderDummy);
        atomicShredderDummy = new ItemBlockHolder(ModBlocks.atomicShredderDummyCore);

        if (Reference.isDebugMode) {
            debugItem = new ItemDebugTool(Strings.DEBUG_TOOL);
        }
    }

    public static ItemStack getPlaceHolderStack(String item, int size) {
        String[] names = ItemPlaceHolder64.names;
        int pos = 0;
        for (String name : names) {
            if (name.equals(item))
                return new ItemStack(placeHolders64, size, pos);
            pos++;
        }

        names = ItemPlaceHolder.names;
        pos = 0;
        for (String name : names) {
            if (name.equals(item))
                return new ItemStack(placeHolders, size, pos);
            pos++;
        }

        return null;
    }

    public static ItemStack getPlaceHolderStack(String item) {
        return getPlaceHolderStack(item, 1);
    }

    public static boolean isPlaceHolderStack(String string, ItemStack stack, boolean ignoreStackSize) {
        ItemStack itemStack = getPlaceHolderStack(string);
        if (itemStack == null)
            return false;

        if (ignoreStackSize)
            return stack.getItemDamage() == itemStack.getItemDamage() && stack.getItem().equals(itemStack.getItem());
        return ItemStack.areItemStacksEqual(itemStack, stack);
    }

    public static void initItemRecipes() {
        CraftingManager craftingManager = CraftingManager.getInstance();

        craftingManager.addRecipe(new ItemStack(Items.gold_ingot), new Object[] { "i", Character.valueOf('i'), new ItemStack(placeHolders, 1, 1) });

        craftingManager.addShapelessRecipe(getPlaceHolderStack("ironBar", 16), new Object[] { Blocks.heavy_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate });
        craftingManager.addRecipe(getPlaceHolderStack("strengthenedIron"), new Object[] { " i ", "isi", " i ", Character.valueOf('i'), getPlaceHolderStack("ironBar"), Character.valueOf('s'), Items.iron_ingot });

        craftingManager.addRecipe(getPlaceHolderStack("ironPlate"), new Object[] { " i ", "isi", " i ", Character.valueOf('i'), Items.iron_ingot, Character.valueOf('s'), getPlaceHolderStack("strengthenedIron") });

        craftingManager.addRecipe(getPlaceHolderStack("strengthenedPlate"), new Object[] { " s ", "sxs", " s ", Character.valueOf('x'), getPlaceHolderStack("ironPlate"), Character.valueOf('s'), getPlaceHolderStack("strengthenedIron") });

        craftingManager.addRecipe(getPlaceHolderStack("strengthenedIronBar", 2), new Object[] { "i", "i", "i", Character.valueOf('i'), getPlaceHolderStack("strengthenedIron") });
        craftingManager.addRecipe(getPlaceHolderStack("atomicFrame", 4), new Object[] { "iii", "isi", "iii", Character.valueOf('i'), getPlaceHolderStack("strengthenedIronBar"), Character.valueOf('s'), Blocks.glass });

        craftingManager.addRecipe(getPlaceHolderStack("ironStrip"), new Object[] { "iii", Character.valueOf('i'), getPlaceHolderStack("strengthenedIron") });
        craftingManager.addRecipe(getPlaceHolderStack("glassesLens"), new Object[] { "xix", "iyi", "xix", Character.valueOf('i'), getPlaceHolderStack("ironStrip"), Character.valueOf('x'), getPlaceHolderStack("ironBar"), Character.valueOf('y'), Blocks.glass });
        craftingManager.addRecipe(getPlaceHolderStack("glassesLens"), new Object[] { "xix", "iyi", "xix", Character.valueOf('x'), getPlaceHolderStack("ironStrip"), Character.valueOf('i'), getPlaceHolderStack("ironBar"), Character.valueOf('y'), Blocks.glass });
        // TODO Proper way to get this.
        craftingManager.addRecipe(getPlaceHolderStack("spectrumSensor"), new Object[] { "xix", "xyx", "xzx", Character.valueOf('i'), Blocks.glass_pane, Character.valueOf('x'), Blocks.redstone_block, Character.valueOf('y'), Items.diamond, Character.valueOf('z'), Blocks.daylight_detector });

        craftingManager.addRecipe(new ItemStack(glasses), new Object[] { "isi", "xix", "a a", Character.valueOf('i'), getPlaceHolderStack("ironStrip"), Character.valueOf('s'), getPlaceHolderStack("spectrumSensor"), Character.valueOf('x'), getPlaceHolderStack("glassesLens"), Character.valueOf('a'), getPlaceHolderStack("ironBar") });
    }
}
