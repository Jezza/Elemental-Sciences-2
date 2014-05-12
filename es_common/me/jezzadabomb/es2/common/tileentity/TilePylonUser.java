package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.IPylonRegistry;
import me.jezzadabomb.es2.common.core.interfaces.IBlockNotifier;
import me.jezzadabomb.es2.common.core.interfaces.IPylonReceiver;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;

public class TilePylonUser extends TileES implements IPylonReceiver, IBlockNotifier {

    private boolean powered = false;
    private boolean registered = false;

    /**
     * call this, or no power for you.
     */
    @Override
    public void updateEntity() {
        if (worldObj == null)
            return;

        if (!registered)
            registered = IPylonRegistry.registerUser(worldObj, getCoordSet());
    }

    /**
     * Called from IPylonRegistery to check if it's powered again.
     */
    @Override
    public void notifyPylonUpdate() {
        powered = IPylonRegistry.isPowered(worldObj, getCoordSet()) >= 0;
    }

    /**
     * Used to remove itself from IPylonRegistry.
     */
    @Override
    public void notifyBlockRemoval() {
        IPylonRegistry.removeUser(worldObj, getCoordSet());
    }

    /**
     * Called to check if it is powered by a pylon, gets updated with notifyBlockRemoval()
     */
    public boolean isPowered() {
        return powered;
    }

    @Override
    public void onBlockRemoval() {
        
    }

    @Override
    public void onBlockAdded() {
        notifyPylonUpdate();
    }

}
