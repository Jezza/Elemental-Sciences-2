package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.tileentity.framework.TileES;

public class TileObelisk extends TileES {

    public int getRenderType() {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    }

}
