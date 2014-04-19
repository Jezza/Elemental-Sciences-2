package me.jezzadabomb.es2.common.tileentity.multi;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.core.utils.helpers.DimensionalPatternHelper;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;
import net.minecraft.tileentity.TileEntity;

public class TileAtomicShredderDummyCore extends TileES {

    int ticked = 0;
    DimensionalPattern dimensionalPattern;

    public TileAtomicShredderDummyCore() {
        dimensionalPattern = DimensionalPatternHelper.getAtomicShredder();
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