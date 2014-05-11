package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.BlockState;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Flag;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Layer;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Row;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;

public class TilePylonDummyCrystal extends TileES {

    private int tier = 0;

    private DimensionalPattern strengthenedIronPattern;
    private DimensionalPattern pylonObeliskPattern;

    public TilePylonDummyCrystal(int tier) {
        this.tier = tier;

        Row row1 = DimensionalPattern.createRow("#");
        Row row2 = DimensionalPattern.createRow("*");

        Layer layer1 = DimensionalPattern.createLayer(row1);
        Layer layer2 = DimensionalPattern.createLayer(row2);

        BlockState strengthenedIronBlock = DimensionalPattern.createBlockState('#', ModBlocks.strengthenedIronBlock);
        BlockState pylonObelisk = DimensionalPattern.createBlockState('#', ModBlocks.crystalObelisk);

        strengthenedIronPattern = DimensionalPattern.createPattern("PylonCrystalObeliskRecipe", layer1, layer1, layer1, layer2, strengthenedIronBlock);
        pylonObeliskPattern = DimensionalPattern.createPattern("PylonCrystalObelisk", layer1, layer1, layer1, layer2, pylonObelisk);
    }

//    public TilePylonDummyCrystal() {
//    }

    @Override
    public void updateEntity() {
        if (worldObj == null)
            return;

        if (strengthenedIronPattern.hasFormed(worldObj, xCoord, yCoord - 3, zCoord)) {
            pylonObeliskPattern.convert(worldObj, xCoord, yCoord - 3, zCoord, Flag.IGNORE);
            worldObj.setBlock(xCoord, yCoord, zCoord, ModBlocks.pylonCrystal, tier, 3);
        }
    }
}
