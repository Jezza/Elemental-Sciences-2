package me.jezzadabomb.es2.common.items;

import java.util.ArrayList;
import java.util.List;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.utils.ItemInformation;
import me.jezzadabomb.es2.common.items.framework.ItemMetaES;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWoodenBucket extends ItemMetaES {

    private Fluid fluid = null;

    private static final ArrayList<String> names = new ArrayList<String>() {
        {
            add("woodenBucket");
            add("woodenBucket_filled");
        }
    };

    public ItemWoodenBucket(String name) {
        super(name);
        maxStackSize = 1;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        boolean flag = itemStack.getItemDamage() == 0;
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, flag);

        if (mop == null)
            return itemStack;

        FillBucketEvent event = new FillBucketEvent(player, itemStack, world, mop);
        if (MinecraftForge.EVENT_BUS.post(event))
            return itemStack;

        if (event.getResult() == Event.Result.ALLOW) {
            if (player.capabilities.isCreativeMode)
                return itemStack;

            if (--itemStack.stackSize <= 0)
                return event.result;

            if (!player.inventory.addItemStackToInventory(event.result))
                player.dropPlayerItemWithRandomChoice(event.result, false);

            return itemStack;
        }

        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int x = mop.blockX;
            int y = mop.blockY;
            int z = mop.blockZ;

            if (!world.canMineBlock(player, x, y, z))
                return itemStack;

            if (flag) {
                if (!player.canPlayerEdit(x, y, z, mop.sideHit, itemStack))
                    return itemStack;

                Block block = world.getBlock(x, y, z);
                Fluid fluid = FluidRegistry.lookupFluidForBlock(block);

                if (fluid == null)
                    return itemStack;

                this.fluid = fluid;
                if (fluid.getTemperature() > 295) {
                    spawnBurnEffects(world, x, y, z);
                    itemStack.stackSize = 0;
                    return itemStack;
                }

                world.setBlockToAir(x, y, z);
                return getFilledBucket();
            }

            ForgeDirection direction = ForgeDirection.getOrientation(mop.sideHit);

            x += direction.offsetX;
            y += direction.offsetY;
            z += direction.offsetZ;

            if (!player.canPlayerEdit(x, y, z, mop.sideHit, itemStack))
                return itemStack;

            if (tryPlaceContainedLiquid(world, x, y, z) && !player.capabilities.isCreativeMode)
                return getEmptyBucket();
        }

        return itemStack;
    }

    public boolean tryPlaceContainedLiquid(World world, int x, int y, int z) {
        if (fluid == null)
            return false;

        Material material = world.getBlock(x, y, z).getMaterial();
        boolean flag = !material.isSolid();

        if (!flag && !world.isAirBlock(x, y, z))
            return false;

        int lavaTemp = FluidRegistry.LAVA.getTemperature();

        if (world.provider.isHellWorld && fluid.getTemperature() < lavaTemp) {
            spawnBurnEffects(world, x, y, z);
        } else {
            if (!world.isRemote && flag && !material.isLiquid())
                world.func_147480_a(x, y, z, true);

            world.setBlock(x, y, z, fluid.getBlock(), 0, 3);
            world.notifyBlockOfNeighborChange(x, y, z, Blocks.air);
        }

        return true;
    }

    private void spawnBurnEffects(World world, int x, int y, int z) {
        world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

        for (int l = 0; l < 8; ++l)
            world.spawnParticle("largesmoke", x + Math.random(), y + Math.random(), z + Math.random(), 0.0D, 0.0D, 0.0D);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(getEmptyBucket());
        list.add(getFilledBucket());
    }

    @Override
    protected void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information) {
    }

    public static ItemStack getEmptyBucket() {
        return new ItemStack(ModItems.woodenBucket, 1, 0);
    }

    public static ItemStack getFilledBucket() {
        return new ItemStack(ModItems.woodenBucket, 1, 1);
    }

    @Override
    public List<String> getNames() {
        return names;
    }
}
