package me.jezzadabomb.es2.common.tileentity.framework;

import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileMulti extends TileES {
    protected CoordSet masterSet = null;

    @Override
    public void updateEntity() {
        tickWorldUnsafe();

        if (worldObj == null)
            return;

        tickWorldSafe();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        boolean flag = tag.getBoolean("masterSetCoord");

        if (flag)
            masterSet = CoordSet.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        boolean flag = masterSet != null;

        tag.setBoolean("masterSetCoord", flag);

        if (flag)
            masterSet.writeToNBT(tag);
    }

    public CoordSet getMaster() {
        return masterSet;
    }

    public void setMaster(CoordSet coordSet) {
        masterSet = coordSet;
    }

    public boolean hasMaster() {
        return masterSet != null;
    }

    public void tickWorldUnsafe() {
    }

    public void tickWorldSafe() {
    }
}
