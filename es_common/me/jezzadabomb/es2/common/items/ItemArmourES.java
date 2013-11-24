package me.jezzadabomb.es2.common.items;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemArmourES extends ItemArmor{
    
    public ItemArmourES(int id, EnumArmorMaterial armorMaterial, int par3, int armourIndex, String name) {
        super(id, armorMaterial, par3, armourIndex);
        setMaxDamage(0);
        setUnlocalizedName(name);
        setCreativeTab(ElementalSciences2.creativeTab);
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return false;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register)
    {
        itemIcon = register.registerIcon(Reference.MOD_ID + ":" + this.getUnlocalizedName().replace("item.", ""));
    }

}
