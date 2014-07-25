package me.jezzadabomb.es2.common.items.equipment;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.utils.FluidHelper;
import me.jezzadabomb.es2.common.items.framework.ItemESMeta;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.ArrayList;
import java.util.List;

public class ItemWoodenBucket extends ItemESMeta implements IFluidContainerItem {

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
                Fluid fluid = FluidRegistry.lookupFluidForBlock(world.getBlock(x, y, z));

                if (fluid == null)
                    return itemStack;

                if (!canStore(itemStack, fluid)) {
                    spawnBurnEffects(world, x, y, z);
                    if (!player.capabilities.isCreativeMode)
                        itemStack.stackSize = 0;
                    return itemStack;
                }

                world.setBlockToAir(x, y, z);
                return getFilledBucket(new FluidStack(fluid, 1000));
            }

            ForgeDirection direction = ForgeDirection.getOrientation(mop.sideHit);

            x += direction.offsetX;
            y += direction.offsetY;
            z += direction.offsetZ;

            if (!player.canPlayerEdit(x, y, z, mop.sideHit, itemStack))
                return itemStack;

            if (tryPlaceContainedLiquid(itemStack, world, x, y, z) && !player.capabilities.isCreativeMode)
                return getEmptyBucket();
        }

        return itemStack;
    }

    public boolean tryPlaceContainedLiquid(ItemStack itemStack, World world, int x, int y, int z) {
        Material material = world.getBlock(x, y, z).getMaterial();
        boolean flag = !material.isSolid();

        if (!flag && !world.isAirBlock(x, y, z))
            return false;

        FluidStack fluidStack = FluidHelper.getStoredFluid(itemStack);
        if (fluidStack == null)
            return false;
        Fluid fluid = fluidStack.getFluid();

        if (world.provider.isHellWorld && fluid.getTemperature() <= FluidRegistry.WATER.getTemperature()) {
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
        list.add(getWaterBucket());
    }

    public static ItemStack getEmptyBucket() {
        ItemStack itemStack = new ItemStack(ModItems.woodenBucket, 1, 0);
        return itemStack;
    }

    public static ItemStack getWaterBucket() {
        ItemStack itemStack = new ItemStack(ModItems.woodenBucket, 1, 1);
        FluidHelper.storeFluid(itemStack, new FluidStack(FluidRegistry.WATER, 1000));
        return itemStack;
    }

    public static ItemStack getFilledBucket(FluidStack fluidStack) {
        ItemStack itemStack = new ItemStack(ModItems.woodenBucket, 1, 1);
        FluidHelper.storeFluid(itemStack, fluidStack);
        return itemStack;
    }

    private boolean canStore(ItemStack itemStack, Fluid fluid) {
        return fluid != null && fluid.getTemperature() < 372 && FluidHelper.getStoredFluid(itemStack) == null;
    }

    @Override
    public List<String> getNames() {
        return names;
    }

    @Override
    public FluidStack getFluid(ItemStack itemStack) {
        return FluidHelper.getStoredFluid(itemStack);
    }

    @Override
    public int getCapacity(ItemStack itemStack) {
        return FluidHelper.getStoredFluid(itemStack) == null ? 1000 : 0;
    }

    @Override
    public int fill(ItemStack itemStack, FluidStack resource, boolean doFill) {
        if (!canStore(itemStack, resource.getFluid()))
            return 0;
        if (doFill)
            FluidHelper.storeFluid(itemStack, resource);
        return 1000;
    }

    @Override
    public FluidStack drain(ItemStack itemStack, int maxDrain, boolean doDrain) {
        if (getCapacity(itemStack) == 0)
            return null;
        FluidStack fluidStack = FluidHelper.getStoredFluid(itemStack);
        if (doDrain) {
            FluidHelper.removeFluid(itemStack);
        }
        return fluidStack;
    }
}
