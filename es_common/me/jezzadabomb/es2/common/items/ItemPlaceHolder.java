package me.jezzadabomb.es2.common.items;

import java.util.List;

import cofh.api.energy.IEnergyHandler;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.entities.EntityDrone;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPlaceHolder extends ItemES {

    @SideOnly(Side.CLIENT)
    private Icon[] icons;
    public static final String[] names = new String[] { "lifeCoin", "deadCoin", "glassesLens", "frameSegment", "ironBar", "spectrumSensor", "constructorDrone", "selectiveEMPTrigger", "energyMaintenance" };

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
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(name)) {
                return i;
            }
        }
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
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ) {
        if (itemStack.getItemDamage() == 8 && world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof IEnergyHandler) {
            IEnergyHandler tEH = (IEnergyHandler) world.getBlockTileEntity(x, y, z);
            if (world.isRemote) {
                player.addChatMessage("Client Side: " + tEH.getEnergyStored(null));
            } else {
                player.sendChatToPlayer(new ChatMessageComponent().addText("Server Side: " + tEH.getEnergyStored(null)));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && stack.getItemDamage() == getDamage("constructorDrone") && world.getBlockId(x, y, z) == ModBlocks.atomicConstructor.blockID) {
            TileAtomicConstructor tAC = (TileAtomicConstructor) world.getBlockTileEntity(x, y, z);
            EntityDrone drone = new EntityDrone(world);
            
            drone.posX = x + 0.5F;
            drone.posY = y + 0.5F;
            drone.posZ = z + 0.5F;
            
            world.spawnEntityInWorld(drone);
            tAC.registerDrone(drone);
            return true;
        }
        return false;
    }

    @Override
    public Icon getIconFromDamage(int damage) {
        return icons[damage];
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return "item." + names[MathHelper.clipInt(itemStack.getItemDamage(), names.length)];
    }
}
