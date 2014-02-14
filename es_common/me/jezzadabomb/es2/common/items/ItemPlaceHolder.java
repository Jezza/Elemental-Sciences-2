package me.jezzadabomb.es2.common.items;

import java.util.List;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.entities.EntityDrone;
import me.jezzadabomb.es2.common.interfaces.IDismantleable;
import me.jezzadabomb.es2.common.items.framework.ItemES;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPlaceHolder extends ItemES {

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;
    public static final String[] names = new String[] { "lifeCoin", "deadCoin", "glassesLens", "frameSegment", "ironBar", "spectrumSensor", "constructorDrone", "selectiveEMPTrigger", "empTrigger", "wrenchThing" };

    public ItemPlaceHolder(String name) {
        super(name);
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
            case 9:
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
    public int getDisplayDamage(ItemStack stack) {
        ESLogger.info("Display");
        if (getDamage("wrenchThing", stack))
            return stack.getTagCompound().getInteger("Durablity");
        return super.getDisplayDamage(stack);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (getDamage("constructorDrone", stack) && UtilMethods.isConstructor(world, x, y, z) && !world.isRemote) {
            ESLogger.info(world.isRemote);
            TileAtomicConstructor tAC = (TileAtomicConstructor) world.getTileEntity(x, y, z);
            boolean flag = tAC.hasConsole();
            // if (flag) {
            // TileConsole console = tAC.getConsole();
            // EntityDrone drone = new EntityDrone(world).setConsole(console);
            //
            // Random rand = new Random();
            //
            // drone.posX = x + MathHelper.clipFloat(rand.nextFloat(), 0.1F, 0.9F);
            // drone.posY = y + MathHelper.clipFloat(rand.nextFloat(), 0.1F, 0.9F);
            // drone.posZ = z + MathHelper.clipFloat(rand.nextFloat(), 0.1F, 0.9F);
            //
            // world.spawnEntityInWorld(drone);
            //
            // if (!player.capabilities.isCreativeMode)
            // UtilMethods.decrCurrentItem(player);
            // }
            if (flag)
                player.swingItem();
            return flag;
        } else if (getDamage("wrenchThing", stack) && UtilMethods.isDismantable(world, x, y, z)) {
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
    public void onUpdate(ItemStack stack, World world, Entity par3Entity, int par4, boolean par5) {
        super.onUpdate(stack, world, par3Entity, par4, par5);
        if (getDamage("wrenchThing", stack) && !stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger("Durablity", 512);
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (getDamage("empTrigger", stack)) {
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

    public boolean getDamage(String name, ItemStack stack) {
        return stack.getItemDamage() == getDamage(name);
    }

    public int getDamage(String name) {
        for (int i = 0; i < names.length; i++)
            if (names[i].equals(name))
                return i;
        return -1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 1; i < names.length; i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        icons = new IIcon[names.length];
        for (int i = 0; i < icons.length; i++)
            icons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":" + names[MathHelper.clipInt(i, names.length)]);
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        damage = MathHelper.clipInt(damage, names.length - 1);
        return icons[damage];
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return "item." + names[MathHelper.clipInt(itemStack.getItemDamage(), names.length - 1)];
    }
}
