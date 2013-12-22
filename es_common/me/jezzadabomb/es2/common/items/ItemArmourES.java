package me.jezzadabomb.es2.common.items;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemArmourES extends ItemArmor {

	int slot;
	String textureLocation;

	public ItemArmourES(int id, EnumArmorMaterial armorMaterial, int renderIndex, ArmourSlotIndex armourIndex, String name, String textureLocation) {
		super(id, armorMaterial, renderIndex, armourIndex.slot);
		slot = armourIndex.slot;
		this.textureLocation = textureLocation;
		setMaxDamage(0);
		setUnlocalizedName(name);
		setCreativeTab(ElementalSciences2.creativeTab);
		register(name);
	}

	public void register(String name) {
		GameRegistry.registerItem(this, name);
	}

	@Override
	public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
		return armorType == slot;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return false;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer) {
		return textureLocation;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		itemIcon = register.registerIcon(Reference.MOD_ID + ":" + this.getUnlocalizedName().replace("item.", ""));
	}
}
