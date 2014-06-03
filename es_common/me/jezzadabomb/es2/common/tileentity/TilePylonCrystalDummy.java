package me.jezzadabomb.es2.common.tileentity;

import net.minecraft.tileentity.TileEntity;
import me.jezzadabomb.es2.api.multiblock.DimensionalPattern;
import me.jezzadabomb.es2.api.multiblock.DimensionalPattern.BlockState;
import me.jezzadabomb.es2.api.multiblock.DimensionalPattern.Flag;
import me.jezzadabomb.es2.api.multiblock.DimensionalPattern.Layer;
import me.jezzadabomb.es2.api.multiblock.DimensionalPattern.Row;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.tileentity.framework.TilePylon;

public class TilePylonCrystalDummy extends TilePylon {

    private int tier = 0;

    public TilePylonCrystalDummy(int tier) {
        this.tier = tier;
    }

    public TilePylonCrystalDummy() {
    }

    @Override
    public void updateEntity() {
        if (worldObj == null)
            return;

        if (getStrengthenedIronPattern().hasFormed(worldObj, xCoord, yCoord - 3, zCoord)) {

            for (int i = 1; i <= 3; i++)
                worldObj.func_147480_a(xCoord, yCoord - i, zCoord, false);

            getPylonObeliskPattern().convert(worldObj, xCoord, yCoord - 3, zCoord, Flag.IGNORE);
            worldObj.setBlock(xCoord, yCoord, zCoord, ModBlocks.pylonCrystal, tier, 3);
        }
    }

    public int getTier() {
        return tier;
    }
}
