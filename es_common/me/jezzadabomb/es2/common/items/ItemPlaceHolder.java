package me.jezzadabomb.es2.common.items;

import java.util.List;
import java.util.Random;

import cofh.api.energy.IEnergyHandler;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.entities.EntityDrone;
import me.jezzadabomb.es2.common.items.framework.ItemES;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPlaceHolder extends ItemES {

    @SideOnly(Side.CLIENT)
    private Icon[] icons;
    public static final String[] names = new String[] { "lifeCoin", "deadCoin", "glassesLens", "frameSegment", "ironBar", "spectrumSensor", "constructorDrone", "selectiveEMPTrigger", "empTrigger" };

    public ItemPlaceHolder(int id, String name) {
        super(id, name);
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

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
                addToBothLists("A carefully crafted, highly fragile glass lens.");
                break;
            case 3:
                addToBothLists("A tough piece of metal, could be used for a frame.");
                break;
            case 4:
                addToBothLists("A solid iron rod.");
                break;
            case 5:
                addToBothLists("A very fragile crystal.");
                break;
            case 6:
                infoList.add("A shiny drone for shiny things.");
                shiftList.add("He shall be called Geoff.");
                break;
        }
    }

    public int getDamage(String name) {
        for (int i = 0; i < names.length; i++)
            if (names[i].equals(name))
                return i;
        return -1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs creativeTab, List list) {
        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        icons = new Icon[names.length];
        for (int i = 0; i < icons.length; i++)
            icons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":" + names[MathHelper.clipInt(i, names.length)]);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && stack.getItemDamage() == getDamage("constructorDrone") && world.getBlockId(x, y, z) == ModBlocks.atomicConstructor.blockID) {
            TileAtomicConstructor tAC = (TileAtomicConstructor) world.getBlockTileEntity(x, y, z);
            EntityDrone drone = new EntityDrone(world);

            Random rand = new Random();

            drone.posX = x + MathHelper.clipFloat(rand.nextFloat(), 0.1F, 0.9F);
            drone.posY = y + MathHelper.clipFloat(rand.nextFloat(), 0.1F, 0.9F);
            drone.posZ = z + MathHelper.clipFloat(rand.nextFloat(), 0.1F, 0.9F);

            if (tAC.registerDrone(drone)) {
                world.spawnEntityInWorld(drone);
                if (!player.capabilities.isCreativeMode)
                    UtilMethods.decrCurrentItem(player);
                player.swingItem();
            }
            return true;
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (stack.getItemDamage() == getDamage("empTrigger")) {
            float range = 5.0F;
            List<Object> droneList = world.getEntitiesWithinAABB(EntityDrone.class, AxisAlignedBB.getAABBPool().getAABB(player.posX - range, player.posY - range, player.posZ - range, player.posX + range, player.posY + range, player.posZ + range));
            for (Object object : droneList) {
                if (object instanceof EntityDrone) {
                    EntityDrone drone = (EntityDrone) object;
                    drone.setDead();
                    ItemStack droneStack = ModItems.getPlaceHolderStack("constructorDrone");
                    if (!player.capabilities.isCreativeMode && !player.inventory.addItemStackToInventory(droneStack))
                        world.spawnEntityInWorld(new EntityItem(world, drone.posX, drone.posY, drone.posZ, droneStack));
                }
            }
            player.swingItem();
            return player.capabilities.isCreativeMode ? stack : player.inventory.decrStackSize(player.inventory.currentItem, 1);
        }
        return stack;
    }

    @Override
    public Icon getIconFromDamage(int damage) {
        damage = MathHelper.clipInt(damage, names.length - 1);
        return icons[damage];
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return "item." + names[MathHelper.clipInt(itemStack.getItemDamage(), names.length - 1)];
    }
}
