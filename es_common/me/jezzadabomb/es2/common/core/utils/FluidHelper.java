package me.jezzadabomb.es2.common.core.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class FluidHelper {

    public static void removeFluid(ItemStack itemStack) {
        confirm(itemStack);
        itemStack.getTagCompound().removeTag("Tag");
    }

    public static void storeFluid(ItemStack itemStack, FluidStack fluidStack) {
        confirm(itemStack);
        fluidStack.writeToNBT(itemStack.getTagCompound());
    }

    public static FluidStack getStoredFluid(ItemStack itemStack) {
        confirm(itemStack);
        return FluidStack.loadFluidStackFromNBT(itemStack.getTagCompound());
    }

    public static void confirm(ItemStack itemStack) {
        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());
    }
}
