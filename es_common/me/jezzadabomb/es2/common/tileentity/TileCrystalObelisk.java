package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.interfaces.IBlockNotifier;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class TileCrystalObelisk extends TileES implements IBlockNotifier {

    private int renderType = 2;
    private boolean ticked = false;

    @Override
    public void updateEntity() {
        if (!ticked) {
            renderType = 2;
            for (int i = 1; i <= 3; i++)
                if (ModBlocks.pylonCrystal.equals(worldObj.getBlock(xCoord, yCoord + i, zCoord)))
                    renderType -= (i - 1);
            ticked = true;
        }
    }

    public int getRenderType() {
        return renderType;
    }

    @Override
    public void onBlockRemoval() {
        for (int i = 1; i <= 3; i++) {
            TileEntity tileEntity = worldObj.getTileEntity(xCoord, yCoord + i, zCoord);
            if (tileEntity instanceof TilePylonCrystal) {
                ((TilePylonCrystal) tileEntity).onBlockRemoval();
                break;
            }
        }
    }

    @Override
    public void onBlockAdded() {

    }

}
