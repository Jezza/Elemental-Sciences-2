package me.jezzadabomb.es2.common.tileentity;

import net.minecraft.tileentity.TileEntity;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;

public class TileCrystalObelisk extends TileES {

    private int renderType = 0;

    @Override
    public void updateEntity() {
        checkBelow();
    }

    public void checkBelow() {
        TileEntity tileEntity = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        if (tileEntity instanceof TileCrystalObelisk) {
            TileCrystalObelisk obelisk = (TileCrystalObelisk) tileEntity;
            renderType = obelisk.getRenderType() + 1;
            if(renderType >= 3)
                renderType = 0;
        } else {
            renderType = 0;
        }
    }

    public void checkAbove() {

    }

    public int getRenderType() {
        return renderType;
    }

}
