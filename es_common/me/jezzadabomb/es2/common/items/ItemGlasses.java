package me.jezzadabomb.es2.common.items;

import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.Strings;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemStack;

public class ItemGlasses extends ItemArmourES{
	
    public ItemGlasses(int id, EnumArmorMaterial armorMaterial, int par3, int armourIndex, String name) {
        super(id, armorMaterial, par3, armourIndex, name);
    }
    
    @Override
    public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
        return armorType == 0;
    }
    
    @Override
    public void registerIcons(IconRegister register) {
        itemIcon = register.registerIcon(Reference.MOD_ID + ":" + Strings.GLASSES);
    }
    
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer) {
        return TextureMaps.GLASSES_LOCATION;
    }
}
