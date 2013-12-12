package me.jezzadabomb.es2.common.items;

import java.util.List;

import me.jezzadabomb.es2.client.sound.Sounds;
import me.jezzadabomb.es2.common.lib.BlackList;
import me.jezzadabomb.es2.common.tickers.WorldTicker;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAtomicCatalyst extends ItemES {

	private int strength = 3;

	public ItemAtomicCatalyst(int id, String name) {
		super(id, name);
		setMaxDamage(511);
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ) {
		if (!world.isRemote && !BlackList.OnBlackList(world.getBlockId(x, y, z), world.getBlockMetadata(x, y, z))) {
			Sounds.CATALYST_PULSE.play(x, y, z);
			int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemStack);
			WorldTicker.addBreaker(world, x, y, z, world.getBlockId(x, y, z), world.getBlockMetadata(x, y, z), world.getBlockId(x, y, z), world.getBlockMetadata(x, y, z), strength, player, fortune);
			itemStack.damageItem(1, player);
		}
		return !BlackList.OnBlackList(world.getBlockId(x, y, z), world.getBlockMetadata(x, y, z));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add("Creates destructive energy wave,");
		list.add("propagates it through a medium,");
		list.add("destroying the medium.");
	}
}
