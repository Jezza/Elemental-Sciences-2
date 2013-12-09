package me.jezzadabomb.es2.common;

import me.jezzadabomb.es2.common.items.ItemAtomicCatalyst;
import me.jezzadabomb.es2.common.items.ItemDebugTool;
import me.jezzadabomb.es2.common.items.ItemGlasses;
import me.jezzadabomb.es2.common.lib.ItemIds;
import me.jezzadabomb.es2.common.lib.Strings;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;

public class ModItems {

    public static Item atomicCatalyst;
    public static Item glasses;
    public static Item debugItem;

    public static void init() {
        // Bohr model. Bite me :P
        atomicCatalyst = new ItemAtomicCatalyst(ItemIds.ATOMIC_CATALYST, Strings.ATOMIC_CATALYST);
        glasses = new ItemGlasses(ItemIds.GLASSES, EnumArmorMaterial.CLOTH, 2, 0, Strings.GLASSES);
        debugItem = new ItemDebugTool(ItemIds.DEBUG_TOOL, Strings.DEBUG_TOOL);
    }
}
