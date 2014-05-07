package me.jezzadabomb.es2.common.tileentity.multi;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.BlockState;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Layer;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Row;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;
import net.minecraft.tileentity.TileEntity;

public class TileAtomicShredderDummyCore extends TileES {

    int ticked = 0;
    DimensionalPattern dimensionalPattern;

    public TileAtomicShredderDummyCore() {
        Row row1 = DimensionalPattern.createRow("###");
        Row row2 = DimensionalPattern.createRow("#G#");
        Row row3 = DimensionalPattern.createRow("GCG");

        Layer layer1 = DimensionalPattern.createLayer(row1, row2, row1);
        Layer layer2 = DimensionalPattern.createLayer(row2, row3, row2);

        BlockState shredderFrame = DimensionalPattern.createBlockState(Character.valueOf('#'), ModBlocks.atomicShredderDummyCore);
        BlockState shredderGlass = DimensionalPattern.createBlockState(Character.valueOf('G'), ModBlocks.atomicShredderDummy, 1);
        BlockState shredderDummyCore = DimensionalPattern.createBlockState(Character.valueOf('C'), ModBlocks.atomicShredderDummyCore, 1);

        dimensionalPattern = DimensionalPattern.createPattern("AtomicShredder", layer1, layer2, layer1, shredderFrame, shredderGlass, shredderDummyCore);
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote)
            return;

        if (++ticked > UtilMethods.getTimeInTicks(0, 0, 5, 0)) {
            if (dimensionalPattern.hasFormed(worldObj, xCoord - 1, yCoord - 1, zCoord - 1)) {
                worldObj.setBlock(xCoord, yCoord, zCoord, ModBlocks.atomicShredder);
                TileEntity tileEntity = worldObj.getTileEntity(xCoord, yCoord, zCoord);
                if (tileEntity instanceof TileAtomicShredderCore)
                    ((TileAtomicShredderCore) tileEntity).convert();
            }
            ticked = 0;
        }
    }
}