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
    
    private static int ironRenderIndex = 2;

    public static void init() {
        // Bohr model. Bite me :P
        atomicCatalyst = new ItemAtomicCatalyst(ItemIds.ATOMIC_CATALYST, Strings.ATOMIC_CATALYST);
        glasses = new ItemGlasses(ItemIds.GLASSES, EnumArmorMaterial.IRON, ironRenderIndex, ArmourSlotIndex.HEAD, Strings.GLASSES, TextureMaps.GLASSES_LOCATION);
        hoverBoots = new ItemHoverBoots(ItemIds.HOVER_BOOTS, EnumArmorMaterial.IRON, ironRenderIndex, ArmourSlotIndex.BOOTS, Strings.HOVER_BOOTS, TextureMaps.HOVER_BOOTS_LOCATION);
        
        debugItem = new ItemDebugTool(ItemIds.DEBUG_TOOL, Strings.DEBUG_TOOL);
    }
}
