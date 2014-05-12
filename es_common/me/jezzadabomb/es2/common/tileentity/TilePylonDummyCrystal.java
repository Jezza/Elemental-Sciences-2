package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.BlockState;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Flag;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Layer;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Row;
import me.jezzadabomb.es2.common.tileentity.framework.TilePylon;

public class TilePylonDummyCrystal extends TilePylon {

    private int tier = 0;

    public TilePylonDummyCrystal(int tier) {
        this.tier = tier;
    }

    public TilePylonDummyCrystal() {
    }

    @Override
    public void updateEntity() {
        if (worldObj == null)
            return;

        if (getStrengthenedIronPattern().hasFormed(worldObj, xCoord, yCoord - 3, zCoord)) {
            getPylonObeliskPattern().convert(worldObj, xCoord, yCoord - 3, zCoord, Flag.IGNORE);
            for (int i = 1; i <= 3; i++) {

            }
            worldObj.setBlock(xCoord, yCoord, zCoord, ModBlocks.pylonCrystal, tier, 3);
        }
    }

    public int getTier() {
        return tier;
    }
}
