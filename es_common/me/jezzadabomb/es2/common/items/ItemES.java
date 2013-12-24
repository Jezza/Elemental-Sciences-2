package me.jezzadabomb.es2.common.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemES extends Item {

	public ItemES(int id, String name) {
		super(id);
		setUnlocalizedName(name);
		setCreativeTab(ElementalSciences2.creativeTab);
		register(name);
	}

	public void register(String name) {
		GameRegistry.registerItem(this, name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + this.getUnlocalizedName().replace("item.", ""));
	}

}
