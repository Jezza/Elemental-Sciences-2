package me.jezzadabomb.es2.common.items;

import java.util.List;

import me.jezzadabomb.es2.client.sound.Sounds;
import me.jezzadabomb.es2.common.lib.BlackList;
import me.jezzadabomb.es2.common.tickers.WorldTicker;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
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
	
	@Override
    protected void addInformation() {
        defaultInfoList();
        shiftList.add("Creates a high energy particle");
        shiftList.add("wave that propagates through");
        shiftList.add("a medium, breaking it's wave");
        shiftList.add("function, and thus");
        shiftList.add("collapsing it.");
    }
	
	public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase entityLivingBase)
    {
	    itemStack.damageItem(80, player);
	    player.swingItem();
	    entityLivingBase.attackEntityFrom(DamageSource.outOfWorld, 40.0F);
	    return true;
    }
}
