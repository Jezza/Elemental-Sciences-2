package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.IPylonRegistry;
import me.jezzadabomb.es2.common.core.interfaces.IBlockNotifier;
import me.jezzadabomb.es2.common.core.interfaces.IPylon;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.BlockState;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Flag;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Layer;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Row;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.tileentity.framework.TilePylon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TilePylonCrystal extends TilePylon implements IPylon, IBlockNotifier {

    private boolean registered = false;
    private int tier = 0;

    public TilePylonCrystal(int tier) {
        this.tier = tier;
    }

    public TilePylonCrystal() {
    }

    @Override
    public void updateEntity() {
        if (worldObj == null)
            return;

        if (!registered)
            registered = IPylonRegistry.registerPylon(worldObj, getCoordSet(), tier);
    }

    public int getTier() {
        return tier;
    }

    @Override
    public void onBlockRemoval() {
        getStrengthenedIronPattern().convert(worldObj, xCoord, yCoord - 3, zCoord, Flag.IGNORE);
        worldObj.setBlock(xCoord, yCoord, zCoord, ModBlocks.pylonCrystal, tier + 3, 3);
        registered = !IPylonRegistry.removePylon(worldObj, getCoordSet(), tier);
    }

    @Override
    public void onBlockAdded() {

    }
}
