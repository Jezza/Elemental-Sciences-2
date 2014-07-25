package me.jezzadabomb.es2.common.items.relics;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.client.sound.Sounds;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.utils.AtomicCatalystAttribute;
import me.jezzadabomb.es2.common.core.utils.ItemInformation;
import me.jezzadabomb.es2.common.core.utils.MovingObjectPositionUtil;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.items.framework.ItemES;
import me.jezzadabomb.es2.common.lib.BlackList;
import me.jezzadabomb.es2.common.tickers.CatalystTicker;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ItemAtomicCatalyst extends ItemES {
    public ItemAtomicCatalyst(String name) {
        super(name);
        setMaxDamage(511);
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(getDefault());
    }

    public static ItemStack getDefault() {
        ItemStack itemStack = new ItemStack(ModItems.atomicCatalyst);
        itemStack.setTagCompound(new NBTTagCompound());
        new AtomicCatalystAttribute(2, 0, 0).writeToNBT(itemStack.getTagCompound());
        return itemStack;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, MovingObjectPosition mop) {
        if (player.isSneaking()) {
            player.openGui(ElementalSciences2.instance, 32, world, 0, 0, 0);
            return itemStack;
        }
        startChain(player, itemStack, mop, false);
        player.swingItem();
        return itemStack;
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entity, ItemStack itemStack) {
        return entity instanceof EntityPlayer && startChain((EntityPlayer) entity, itemStack, MovingObjectPositionUtil.getCurrentMovingObjectPosition(entity), true);
    }

    private boolean startChain(EntityPlayer player, ItemStack itemStack, MovingObjectPosition mop, boolean overrideStrength) {
        World world = player.getEntityWorld();
        if (mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)
            return false;

        int x = mop.blockX;
        int y = mop.blockY;
        int z = mop.blockZ;
        Block block = world.getBlock(x, y, z);
        boolean notOnList = !BlackList.OnBlackList(block, world.getBlockMetadata(x, y, z));
        if (!world.isRemote && notOnList) {
            AtomicCatalystAttribute atomic = AtomicCatalystAttribute.readFromNBT(itemStack.getTagCompound());

            int strength = overrideStrength ? 0 : atomic.getStrength();
            int fortune = atomic.getFortune();
            int speed = atomic.getSpeed();
            int meta = world.getBlockMetadata(x, y, z);

            CatalystTicker.addBreaker(world, x, y, z, block, meta, strength, (EntityPlayer) player, fortune, speed);
            Sounds.CATALYST_PULSE.play(player, 1.0F, 0.8F);
            itemStack.damageItem(1, player);
            return true;
        }
        return false;
    }

    @Override
    protected void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information) {
        if (!stack.hasTagCompound()) {
            information.addToBothLists(EnumChatFormatting.RED + "Errr, this is awkward...");
            information.addToBothLists(EnumChatFormatting.RED + "You shouldn't have this...");
            return;
        }

        AtomicCatalystAttribute attribute = AtomicCatalystAttribute.readFromNBT(stack.getTagCompound());

        int fortune = attribute.getFortune();
        int speed = attribute.getSpeed();
        int strength = attribute.getStrength();

        if (fortune > 0)
            information.addInfoList("Fortune: " + fortune);
        if (speed > 0)
            information.addInfoList("Speed: " + speed);
        if (strength > 0)
            information.addInfoList("Strength: " + strength);

        information.addShiftList("Creates a high energy particle");
        information.addShiftList("wave that propagates through");
        information.addShiftList("a medium, breaking it's wave");
        information.addShiftList("function, and thus");
        information.addShiftList("collapsing it.");
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase target) {
        if (!itemStack.hasTagCompound())
            return false;

        if (!player.worldObj.isRemote && !(target instanceof EntityPlayerMP && ((EntityPlayerMP) target).capabilities.isCreativeMode)) {
            AtomicCatalystAttribute attribute = AtomicCatalystAttribute.readFromNBT(itemStack.getTagCompound());

            itemStack.damageItem(20, player);
            UtilMethods.damageEntityWith(target, DamageSource.outOfWorld, 5 * attribute.getStrength(), "atomicWave" + (new Random().nextInt(3)));
        }
        player.swingItem();
        return true;
    }
}
