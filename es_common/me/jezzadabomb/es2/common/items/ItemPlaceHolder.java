package me.jezzadabomb.es2.common.items;

import java.util.List;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPlaceHolder extends ItemES {

	@SideOnly(Side.CLIENT)
	private Icon[] icons;
	public static final String[] names = new String[] { "lifeCoin", "deadCoin", "glassesLens", "frameSegment", "ironBar", "spectrumSensor"};

	public ItemPlaceHolder(int id, String name) {
		super(id, name);
		setHasSubtypes(true);
		setMaxStackSize(1);
	}

	protected void addInformation(EntityPlayer player, ItemStack stack) {
		defaultInfoList();
		switch (stack.getItemDamage()) {
		case 0:
			infoList.clear();
			addToBothLists("You got it for getting");
			addToBothLists("a perfect pacman game.");
			break;
		case 1:
			infoList.clear();
			addToBothLists("Just an ordinary coin.");
			break;
		case 2:
			shiftList.add("Something");
			break;
		case 3:
			shiftList.add("PlaceHolder");
			break;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < names.length; i++)
			list.add(new ItemStack(this, 1, i));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		icons = new Icon[names.length];
		for (int i = 0; i < icons.length; i++)
			icons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":" + names[MathHelper.clipInt(i, names.length)]);
	}

	@Override
	public Icon getIconFromDamage(int damage) {
		return icons[damage];
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return "item." + names[MathHelper.clipInt(itemStack.getItemDamage(), names.length)];
	}
}