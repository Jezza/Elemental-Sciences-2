package me.jezzadabomb.es2.common.items;

import java.util.Random;

import me.jezzadabomb.es2.client.sound.Sounds;
import me.jezzadabomb.es2.common.items.framework.ItemES;
import me.jezzadabomb.es2.common.lib.BlackList;
import me.jezzadabomb.es2.common.tickers.CatalystTicker;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class ItemAtomicCatalyst extends ItemES {

    private int strength = 3;

    public ItemAtomicCatalyst(String name) {
        super(name);
        setMaxDamage(511);
        setMaxStackSize(1);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ) {
        Block block = world.getBlock(x, y, z);
        boolean notOnList = !BlackList.OnBlackList(block, world.getBlockMetadata(x, y, z));
        if (!world.isRemote && notOnList) {
            Sounds.CATALYST_PULSE.play(x, y, z);
            int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemStack);
            int speed = 0;
            CatalystTicker.addBreaker(world, x, y, z, block, world.getBlockMetadata(x, y, z), strength, player, fortune, speed);
            itemStack.damageItem(1, player);
        }
        return notOnList;
    }

    @Override
    protected void addInformation(EntityPlayer player, ItemStack stack) {
        defaultInfoList();
        shiftList.add("Creates a high energy particle");
        shiftList.add("wave that propagates through");
        shiftList.add("a medium, breaking it's wave");
        shiftList.add("function, and thus");
        shiftList.add("collapsing it.");
    }

    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase target) {
        if (!player.worldObj.isRemote && !(target instanceof EntityPlayerMP && ((EntityPlayerMP) target).capabilities.isCreativeMode)) {
            itemStack.damageItem(80, player);
            String name = DamageSource.outOfWorld.damageType;
            DamageSource.outOfWorld.damageType = "atomicWave" + (new Random().nextInt(3));
            target.attackEntityFrom(DamageSource.outOfWorld, 20.0F);
            DamageSource.outOfWorld.damageType = name;
        }
        player.swingItem();
        return true;
    }
}
