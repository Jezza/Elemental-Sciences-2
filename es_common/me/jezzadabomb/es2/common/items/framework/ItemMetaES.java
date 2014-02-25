package me.jezzadabomb.es2.common.items.framework;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public abstract class ItemMetaES extends ItemES {

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;
    
    public ItemMetaES(String name) {
        super(name);
        setHasSubtypes(true);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        String[] names = getNames();
        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        String[] names = getNames();
        icons = new IIcon[names.length];
        for (int i = 0; i < icons.length; i++)
            icons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":" + names[MathHelper.clipInt(i, names.length)]);
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        String[] names = getNames();
        damage = MathHelper.clipInt(damage, names.length - 1);
        return icons[damage];
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        String[] names = getNames();
        return "item." + names[MathHelper.clipInt(itemStack.getItemDamage(), names.length - 1)];
    }

    public abstract String[] getNames();
    
}
