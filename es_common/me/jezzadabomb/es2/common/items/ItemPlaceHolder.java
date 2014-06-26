package me.jezzadabomb.es2.common.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.utils.ItemInformation;
import me.jezzadabomb.es2.common.core.utils.helpers.PlayerHelper;
import me.jezzadabomb.es2.common.entities.EntityCombatDrone;
import me.jezzadabomb.es2.common.items.framework.ItemMetaES;
import net.minecraft.creativetab.CreativeTabs;
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
            add("combatDrone");
        }
    };

    public ItemPlaceHolder(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (ModItems.isPlaceHolderStack(stack, "combatDrone")) {
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
