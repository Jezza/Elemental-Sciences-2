package me.jezzadabomb.es2.common;

import net.minecraft.item.Item;
import me.jezzadabomb.es2.common.items.ItemAtomicCatalyst;
import me.jezzadabomb.es2.common.lib.ItemIds;
import me.jezzadabomb.es2.common.lib.Strings;

public class ModItems {
    
    public static Item atomicCatalyst;
    
    public static void init() {
        // Bohr model. Bite me :P
        atomicCatalyst = new ItemAtomicCatalyst(ItemIds.ATOMIC_CATALYST, Strings.ATOMIC_CATALYST);
    }
}
