package me.jezzadabomb.es2.common.tileentity.multi;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.tileentity.framework.TileMulti;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileAtomicShredderDummy extends TileMulti {
    @Override
    public void updateEntity() {
        super.updateEntity();
    }

    public void informCore() {
        TileAtomicShredderCore core = getCore();
        if (core != null)
            core.inform(xCoord, yCoord, zCoord);
    }

    public TileAtomicShredderCore getCore() {
        if (masterSet == null || worldObj == null)
            return null;
        TileEntity tileEntity = worldObj.getTileEntity(masterSet.getX(), masterSet.getY(), masterSet.getZ());
        if (tileEntity instanceof TileAtomicShredderCore)
            return (TileAtomicShredderCore) tileEntity;
        return null;
    }

    public boolean processBlockActivation(EntityPlayer player) {
        TileAtomicShredderCore core = getCore();
        if (core != null) {
            ESLogger.info("Found core");
            return core.processBlockActivation(player);
        }
        ESLogger.info("Cannot find core!");
        return false;
    }
}
