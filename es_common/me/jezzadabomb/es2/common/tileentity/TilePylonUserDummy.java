package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.IPylonRegistry;
import me.jezzadabomb.es2.common.core.interfaces.IPylonReceiver;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;

public class TilePylonUserDummy extends TileES implements IPylonReceiver {

    boolean powered = false;

    @Override
    public void updateEntity() {
        ESLogger.info(powered);
    }

    @Override
    public void pylonNotifyUpdate() {
        powered = IPylonRegistry.isPowered(worldObj, getCoordSet()) >= 0;
    }

}
