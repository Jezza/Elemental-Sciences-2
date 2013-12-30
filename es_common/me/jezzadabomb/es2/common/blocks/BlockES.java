package me.jezzadabomb.es2.common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class BlockES extends Block {

	public BlockES(int par1, Material par2Material, String name) {
		super(par1, par2Material);
		setUnlocalizedName(name);
		setCreativeTab(ElementalSciences2.creativeTab);
		register(name);
	}

	public void register(String name) {
		GameRegistry.registerBlock(this, name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + this.getUnlocalizedName().replace("tile.", ""));
	}

	@Override
	public boolean renderAsNormalBlock() {
		return !renderWithModel();
	}

	@Override
	public boolean isOpaqueCube() {
		return !renderWithModel();
	}

	@Override
	public int getRenderType() {
		return renderWithModel() ? -1 : super.getRenderType();
	}

	public abstract boolean renderWithModel();
}
