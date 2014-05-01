package me.jezzadabomb.es2.common.items;

import java.util.Random;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.client.sound.Sounds;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.AtomicCatalystAttribute;
import me.jezzadabomb.es2.common.items.framework.ItemES;
import me.jezzadabomb.es2.common.lib.BlackList;
import me.jezzadabomb.es2.common.tickers.CatalystTicker;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class ItemAtomicCatalyst extends ItemES {
    public ItemAtomicCatalyst(String name) {
        super(name);
        setMaxDamage(511);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            player.openGui(ElementalSciences2.instance, 32, world, 0, 0, 0);
            return itemStack;
        }
        return super.onItemRightClick(itemStack, world, player);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ) {
        if (player.isSneaking())
            return true;

        Block block = world.getBlock(x, y, z);
        boolean notOnList = !BlackList.OnBlackList(block, world.getBlockMetadata(x, y, z));
        if (!world.isRemote && notOnList) {
            AtomicCatalystAttribute atomic = AtomicCatalystAttribute.readFromNBT(itemStack.getTagCompound());

            int strength = atomic.getStrength();
            int fortune = atomic.getFortune();
            int speed = atomic.getSpeed();
            int meta = world.getBlockMetadata(x, y, z);

            CatalystTicker.addBreaker(world, x, y, z, block, meta, strength, player, fortune, speed);
            Sounds.CATALYST_PULSE.play(player, 1.0F, 0.8F);
            itemStack.damageItem(1, player);
        }
        return notOnList;
    }

    @Override
    protected void addInformation(EntityPlayer player, ItemStack stack) {
        if (!stack.hasTagCompound())
            setTag(stack);

        AtomicCatalystAttribute attribute = AtomicCatalystAttribute.readFromNBT(stack.getTagCompound());

        int fortune = attribute.getFortune();
        int speed = attribute.getSpeed();
        int strength = attribute.getStrength();

        if (fortune > 0)
            infoList.add("Fortune: " + fortune);
        if (speed > 0)
            infoList.add("Speed: " + speed);
        if (strength > 0)
            infoList.add("Strength: " + strength);

        shiftList.add("Creates a high energy particle");
        shiftList.add("wave that propagates through");
        shiftList.add("a medium, breaking it's wave");
        shiftList.add("function, and thus");
        shiftList.add("collapsing it.");
    }

    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase target) {
        if (!player.worldObj.isRemote && !(target instanceof EntityPlayerMP && ((EntityPlayerMP) target).capabilities.isCreativeMode)) {
            if (!itemStack.hasTagCompound())
                setTag(itemStack);
            AtomicCatalystAttribute attribute = AtomicCatalystAttribute.readFromNBT(itemStack.getTagCompound());

            itemStack.damageItem(20, player);
            String name = DamageSource.outOfWorld.damageType;
            DamageSource.outOfWorld.damageType = "atomicWave" + (new Random().nextInt(3));
            target.attackEntityFrom(DamageSource.outOfWorld, 5 * attribute.getStrength());
            DamageSource.outOfWorld.damageType = name;
        }
        player.swingItem();
        return true;
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
        world.setRainStrength(0.0F);
        if (!world.isRemote && !itemStack.hasTagCompound())
            setTag(itemStack);

        super.onUpdate(itemStack, world, entity, par4, par5);
    }

    public static void setTag(ItemStack itemStack) {
        itemStack.setTagCompound(new NBTTagCompound());
        new AtomicCatalystAttribute(2, 0, 0).writeToNBT(itemStack.getTagCompound());
    }
}
