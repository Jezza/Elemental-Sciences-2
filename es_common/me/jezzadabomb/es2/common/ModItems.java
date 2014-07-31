package me.jezzadabomb.es2.common;

import me.jezzadabomb.es2.client.lib.GuiIDs;
import me.jezzadabomb.es2.common.items.armour.ItemGlasses;
import me.jezzadabomb.es2.common.items.armour.ItemHoverBoots;
import me.jezzadabomb.es2.common.items.crafting.ItemCrafting;
import me.jezzadabomb.es2.common.items.debug.ItemDebugTool;
import me.jezzadabomb.es2.common.items.debug.ItemPylonManager;
import me.jezzadabomb.es2.common.items.equipment.ItemCombatDrone;
import me.jezzadabomb.es2.common.items.equipment.ItemEMPTrigger;
import me.jezzadabomb.es2.common.items.equipment.ItemSelectiveEMPTrigger;
import me.jezzadabomb.es2.common.items.equipment.ItemWoodenBucket;
import me.jezzadabomb.es2.common.items.framework.ItemArmourES.ArmourRenderIndex;
import me.jezzadabomb.es2.common.items.framework.ItemArmourES.ArmourSlotIndex;
import me.jezzadabomb.es2.common.items.relics.ItemAtomicCatalyst;
import me.jezzadabomb.es2.common.items.relics.ItemCoin;
import me.jezzadabomb.es2.common.items.relics.ItemQuantumStateDisruptor;
import me.jezzadabomb.es2.common.items.research.ItemResearchTool;
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

    // Debug
    public static Item pylonControl;
    public static Item debugItem;

    // Crafting Items
    public static Item atomicFrame;
    public static Item ironBar;
    public static Item ironPlate;
    public static Item glassesLens;
    public static Item spectrumSensor;
    public static Item strengthenedIron;
    public static Item strengthenedIronBar;
    public static Item strengthenedIronStrip;
    public static Item strengthenedPlate;

    // Items
    public static Item woodenBucket;

    // Armour
    public static Item hoverBoots;
    public static Item glasses;

    // Equipment
    public static Item constructorDrone;
    public static Item combatDrone;
    public static Item empTrigger;
    public static Item selectiveEMPTrigger;

    // Relics
    public static Item lifeCoin;
    public static Item deadCoin;
    public static Item gamblersCoin;
    public static Item atomicCatalyst;
    public static Item quantumStateDisruptor;

    // Research Tools
    public static Item quill;
    public static Item papyrus;
    public static Item quillAndPapyrus;
    public static Item pencil;
    public static Item pencilAndPaper;

    public static void init() {
        // Crafting
        atomicFrame = new ItemCrafting(Strings.ATOMIC_FRAME);
        ironBar = new ItemCrafting(Strings.IRON_BAR);
        ironPlate = new ItemCrafting(Strings.IRON_PLATE);
        glassesLens = new ItemCrafting(Strings.GLASSES_LENS);
        spectrumSensor = new ItemCrafting(Strings.SPECTRUM_SENSOR);
        strengthenedIron = new ItemCrafting(Strings.STRENGTHENED_IRON);
        strengthenedIronBar = new ItemCrafting(Strings.STRENGTHENED_IRON_BAR);
        strengthenedIronStrip = new ItemCrafting(Strings.STRENGTHENED_IRON_STRIP);
        strengthenedPlate = new ItemCrafting(Strings.STRENGTHENED_PLATE);

        // Items
        woodenBucket = new ItemWoodenBucket(Strings.WOODEN_BUCKET);

        // Armour
        glasses = new ItemGlasses(ArmorMaterial.IRON, ArmourRenderIndex.IRON, ArmourSlotIndex.HEAD, Strings.GLASSES, TextureMaps.GLASSES_LOCATION);
        hoverBoots = new ItemHoverBoots(ArmorMaterial.IRON, ArmourRenderIndex.IRON, ArmourSlotIndex.BOOTS, Strings.HOVER_BOOTS, TextureMaps.HOVER_BOOTS_LOCATION);

        // Equipment
        constructorDrone = new ItemCrafting(Strings.CONSTRUCTOR_DRONE);
        combatDrone = new ItemCombatDrone(Strings.COMBAT_DRONE);
        empTrigger = new ItemEMPTrigger(Strings.EMP_TRIGGER);
        selectiveEMPTrigger = new ItemSelectiveEMPTrigger(Strings.SELECTIVE_EMP_TRIGGER);

        // Relics
        quantumStateDisruptor = new ItemQuantumStateDisruptor(Strings.ITEM_QUANTUM_STATE_DISRUPTOR);
        atomicCatalyst = new ItemAtomicCatalyst(Strings.ATOMIC_CATALYST).setClickableRight();

        // Redone section
        
        // Relics
        lifeCoin = new ItemCoin(Strings.LIFE_COIN).setLore("You got it for getting", "a perfect pacman game.");
        deadCoin = new ItemCoin(Strings.DEAD_COIN).setLore("Just an ordinary coin.");
        gamblersCoin = new ItemCoin(Strings.GAMBLERS_COIN).setLore("Is luck on your side?").setClickableRight().setDonatorItem("SoulessRaven").setMaxStackSize(1);

        // Research Tools
        quill = new ItemCrafting(Strings.QUILL).setShapelessRecipe(Items.feather, Items.quartz);
        papyrus = new ItemCrafting(Strings.PAPYRUS).setShapelessRecipe(Items.leather, Items.reeds, Items.reeds);
        quillAndPapyrus = new ItemResearchTool(Strings.QUILL_AND_PAPYRUS, GuiIDs.QUILL_AND_PAPYRUS).setShapelessRecipe(quill, papyrus);

        if (Reference.isDebugMode)
            initDebug();
    }

    private static void initDebug() {
        debugItem = new ItemDebugTool(Strings.DEBUG_TOOL);
        pylonControl = new ItemPylonManager(Strings.PYLON_TOOL);
    }

    public static void initItemRecipes() {
        CraftingManager craftingManager = CraftingManager.getInstance();

        craftingManager.addRecipe(new ItemStack(Items.gold_ingot), new Object[]{"i", Character.valueOf('i'), deadCoin});

        craftingManager.addShapelessRecipe(new ItemStack(ironBar, 16), new Object[]{Blocks.heavy_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate});
        craftingManager.addRecipe(new ItemStack(strengthenedIron), new Object[]{" i ", "isi", " i ", Character.valueOf('i'), new ItemStack(ironBar), Character.valueOf('s'), Items.iron_ingot});

        craftingManager.addRecipe(new ItemStack(ironPlate), new Object[]{" i ", "isi", " i ", Character.valueOf('i'), Items.iron_ingot, Character.valueOf('s'), new ItemStack(strengthenedIron)});

        craftingManager.addRecipe(new ItemStack(strengthenedPlate), new Object[]{" s ", "sxs", " s ", Character.valueOf('x'), new ItemStack(ironPlate), Character.valueOf('s'), strengthenedIron});

        craftingManager.addRecipe(new ItemStack(strengthenedIronBar, 2), new Object[]{"i", "i", "i", Character.valueOf('i'), strengthenedIron});
        craftingManager.addRecipe(new ItemStack(atomicFrame, 4), new Object[]{"iii", "isi", "iii", Character.valueOf('i'), strengthenedIronBar, Character.valueOf('s'), Blocks.glass});

        craftingManager.addRecipe(new ItemStack(strengthenedIronStrip), new Object[]{"iii", Character.valueOf('i'), strengthenedIron});
        craftingManager.addRecipe(new ItemStack(glassesLens), new Object[]{"xix", "iyi", "xix", Character.valueOf('i'), strengthenedIronStrip, Character.valueOf('x'), ironBar, Character.valueOf('y'), Blocks.glass});
        craftingManager.addRecipe(new ItemStack(glassesLens), new Object[]{"xix", "iyi", "xix", Character.valueOf('x'), strengthenedIronStrip, Character.valueOf('i'), ironBar, Character.valueOf('y'), Blocks.glass});
        // TODO Proper way to get this.
        craftingManager.addRecipe(new ItemStack(spectrumSensor), new Object[]{"xix", "xyx", "xzx", Character.valueOf('i'), Blocks.glass_pane, Character.valueOf('x'), Blocks.redstone_block, Character.valueOf('y'), Items.diamond, Character.valueOf('z'), Blocks.daylight_detector});

        craftingManager.addRecipe(new ItemStack(glasses), new Object[]{"isi", "xix", "a a", Character.valueOf('i'), strengthenedIronStrip, Character.valueOf('s'), spectrumSensor, Character.valueOf('x'), glassesLens, Character.valueOf('a'), ironBar});
    }
}
