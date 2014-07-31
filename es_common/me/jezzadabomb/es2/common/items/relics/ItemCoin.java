package me.jezzadabomb.es2.common.items.relics;

import me.jezzadabomb.es2.common.core.utils.ItemInformation;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.items.framework.ItemES;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.Random;

public class ItemCoin extends ItemES {
    private final String USES = "Uses";
    private String[] lore = null;

    public ItemCoin(String name) {
        super(name);
    }

    public ItemCoin setLore(String... lore) {
        this.lore = lore;
        return this;
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer player, ItemInformation information) {
        for (String line : lore)
            information.addInfoList(line);
        int uses = getUses(itemstack);
        if (uses > 0)
            information.addInfoList("Uses: " + uses);
    }

    private int getUses(ItemStack itemStack) {
        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());
        return itemStack.getTagCompound().getInteger(USES);
    }

    private void incrementUses(ItemStack itemStack) {
        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());
        NBTTagCompound tag = itemStack.getTagCompound();
        tag.setInteger(USES, tag.getInteger(USES) + 1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, MovingObjectPosition mop) {
        if (mop == null)
            return itemStack;
        incrementUses(itemStack);

        if (!world.isRemote) {
            if (!player.capabilities.isCreativeMode) {
                damageEntity(player, 4);
                destroyItem(itemStack);
            }
        }

        return itemStack;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase entityLivingBase) {
        if (player.worldObj.isRemote)
            return true;
        float percentForSelf = new Random().nextFloat();
        int damageForSelf = (int) Math.round(player.getMaxHealth() * percentForSelf);
        int damageForOther = (int) Math.round(entityLivingBase.getMaxHealth() * (1 - percentForSelf));
        if (damageForSelf > damageForOther)
            UtilMethods.addChatMessage(player, "The odds were not in your favour.");
        else if (damageForSelf < damageForOther)
            UtilMethods.addChatMessage(player, "The odds were in your favour.");
        else
            UtilMethods.addChatMessage(player, "The odds were equally bad.");
        damageEntity(player, damageForSelf);
        damageEntity(entityLivingBase, damageForOther);
        destroyItem(itemStack);
        return true;
    }

    private void destroyItem(ItemStack itemStack) {
        int uses = getUses(itemStack);
        if (uses < 16)
            return;
        double chance = (Math.exp(uses / 10) / 10) / (new Random().nextFloat() / 0.5F);
        if (chance > 20.0F || (chance > 1.25F && chance < 1.75F))
            itemStack.stackSize = 0;
    }

    private void damageEntity(EntityLivingBase entity, int damage) {
        if (damage > 0 && !(entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode))
            UtilMethods.damageEntityWith(entity, DamageSource.outOfWorld, damage, "gamblersCoin");
    }
}
