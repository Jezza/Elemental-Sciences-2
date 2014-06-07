package me.jezzadabomb.es2.common.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.interfaces.IDismantleable;
import me.jezzadabomb.es2.common.core.utils.ItemInformation;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.helpers.PlayerHelper;
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

    public static final ArrayList<String> names = new ArrayList<String>() {
        {
            add("lifeCoin");
            add("deadCoin");
            add("constructorDrone");
            add("wrenchThing");
            add("combatDrone");
        }
    };

    public ItemPlaceHolder(String name) {
        super(name);
        setMaxStackSize(1);
    }

    public int getPositionInList(String name) {
        return getNames().indexOf(name);
    }

    public boolean isDamageEqual(String name, ItemStack itemStack) {
        int damage = itemStack.getItemDamage();
        return damage == getPositionInList(name);
    }
    
    @Override
    public void onUpdate(ItemStack stack, World world, Entity par3Entity, int par4, boolean par5) {
        if (isDamageEqual("wrenchThing", stack) && !stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger("Durablity", 512);
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (isDamageEqual("combatDrone", stack)) {
            if (!world.isRemote) {
                EntityCombatDrone combatDrone = new EntityCombatDrone(world);
                Random rand = new Random();

                float dX = rand.nextFloat() - 0.5F;
                float dZ = rand.nextFloat() - 0.5F;

                combatDrone.setPosition(player.posX - dX, player.posY + 1.5, player.posZ - dZ);
                combatDrone.setOwner(player);

                world.spawnEntityInWorld(combatDrone);
            }
            return PlayerHelper.decrCurrentItem(player);
        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        CoordSet coordSet = new CoordSet(x, y, z);

        if (isDamageEqual("wrenchThing", stack) && coordSet.isDismantable(world)) {
            IDismantleable dismantle = (IDismantleable) coordSet.getTileEntity(world);
            if (dismantle.canDismantle(player, world, coordSet)) {
                ItemStack tempStack = dismantle.dismantleBlock(player, world, coordSet, !player.capabilities.isCreativeMode);
                int damage = stack.getTagCompound().getInteger("Durablity") - 1;
                stack.getTagCompound().setInteger("Durablity", damage);
                if (damage == 0) {
                    ((EntityLivingBase) player).renderBrokenItemStack(stack);
                    PlayerHelper.decrCurrentItem(player);
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
        for (int i = 1; i < names.size(); i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    protected void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information) {
        switch (stack.getItemDamage()) {
            case 0:
                information.addInfoList("You got it for getting");
                information.addInfoList("a perfect pacman game.");
                break;
            case 1:
                information.addInfoList("Just an ordinary coin.");
                break;
            case 2:
                information.addInfoList("A shiny drone for shiny things.");
                information.addShiftList("He shall be called Geoff.");
                break;
            case 3:
                if (!stack.hasTagCompound()) {
                    stack.setTagCompound(new NBTTagCompound());
                    stack.getTagCompound().setInteger("Durablity", 512);
                }
                int damage = stack.getTagCompound().getInteger("Durablity");
                information.addInfoList("Uses left: " + damage);
                break;
        }
    }

    @Override
    public List<String> getNames() {
        return names;
    }
}
