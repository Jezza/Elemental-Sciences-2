package me.jezzadabomb.es2.common.items;

import me.jezzadabomb.es2.common.items.framework.ArmourSlotIndex;
import me.jezzadabomb.es2.common.items.framework.ItemArmourES;
import net.minecraft.item.EnumArmorMaterial;

public class ItemGlasses extends ItemArmourES {

	public ItemGlasses(int id, EnumArmorMaterial armorMaterial, int renderIndex, ArmourSlotIndex armourIndex, String name, String textureLocation) {
		super(id, armorMaterial, renderIndex, armourIndex, name, textureLocation);
	}
}
