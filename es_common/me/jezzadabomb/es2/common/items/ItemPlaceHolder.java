package me.jezzadabomb.es2.common.items;

import java.util.List;
import java.util.Random;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.interfaces.IDismantleable;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.entities.EntityCombatDrone;
import me.jezzadabomb.es2.common.items.framework.ItemMetaES;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPlaceHolder extends ItemMetaES {

    public static final String[] names = new String[] { "lifeCoin", "deadCoin", "constructorDrone", "wrenchThing", "combatDrone" };

    public ItemPlaceHolder(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity par3Entity, int par4, boolean par5) {
        super.onUpdate(stack, world, par3Entity, par4, par5);
        if (ModItems.isPlaceHolderStack("wrenchThing", stack, true) && !stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger("Durablity", 512);
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (ModItems.isPlaceHolderStack("combatDrone", stack, true)) {
            if(!world.isRemote){                
                EntityCombatDrone combatDrone = new EntityCombatDrone(world);
                Random rand = new Random();
                
                float dX = rand.nextFloat() - 0.5F;
                float dZ = rand.nextFloat() - 0.5F;
                
                combatDrone.setPosition(player.posX - dX, player.posY + 1.5, player.posZ - dZ);
                combatDrone.setOwner(player);
                
                world.spawnEntityInWorld(combatDrone);
            }
            return UtilMethods.decrCurrentItem(player);
        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (ModItems.isPlaceHolderStack("wrenchThing", stack, true) && UtilMethods.isDismantable(world, x, y, z)) {
            IDismantleable dismantle = (IDismantleable) world.getTileEntity(x, y, z);
            if (dismantle.canDismantle(player, world, x, y, z)) {
                ItemStack tempStack = dismantle.dismantleBlock(player, world, x, y, z, !player.capabilities.isCreativeMode);
                int damage = stack.getTagCompound().getInteger("Durablity") - 1;
                stack.getTagCompound().setInteger("Durablity", damage);
                if (damage == 0) {
                    ((EntityLivingBase) player).renderBrokenItemStack(stack);
                    UtilMethods.decrCurrentItem(player);
                }
                if (tempStack != null)
                    player.inventory.addItemStackToInventory(tempStack);
            }
            player.swingItem();
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 1; i < names.length; i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    protected void addInformation(EntityPlayer player, ItemStack stack) {
        switch (stack.getItemDamage()) {
            case 0:
                addToBothLists("You got it for getting");
                addToBothLists("a perfect pacman game.");
                break;
            case 1:
                addToBothLists("Just an ordinary coin.");
                break;
            case 2:
                infoList.add("A shiny drone for shiny things.");
                shiftList.add("He shall be called Geoff.");
                break;
            case 3:
                if (!stack.hasTagCompound()) {
                    stack.setTagCompound(new NBTTagCompound());
                    stack.getTagCompound().setInteger("Durablity", 512);
                }
                int damage = stack.getTagCompound().getInteger("Durablity");
                addToBothLists("Uses left: " + damage);
                break;
        }
    }

    @Override
    public String[] getNames() {
        return names;
    }
}
