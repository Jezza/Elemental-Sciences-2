package me.jezzadabomb.es2.common.tileentity.framework;

import net.minecraft.world.World;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.IPylonRegistry;
import me.jezzadabomb.es2.common.core.interfaces.IBlockNotifier;
import me.jezzadabomb.es2.common.core.interfaces.IPylon;
import me.jezzadabomb.es2.common.core.interfaces.IPylonReceiver;

public abstract class TilePylonUser extends TileES implements IPylonReceiver, IBlockNotifier {

    private IPylon powered = null;
    private boolean registered = false;

    private int timeTicked = -1;

    /**
     * Call this, or no power for you.
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
        powered = IPylonRegistry.isPowered(worldObj, getCoordSet());
    }

    /**
     * Called to check if it's powered by a pylon, gets updated with notifyPylonUpdate()
     */
    public boolean isPowered() {
        return powered != null;
    }

    /**
     * Called to get the pylon it's powered by, gets updated with notifyPylonUpdate()
     */
    public IPylon getPylon() {
        return powered;
    }

    /**
     * Used to remove itself from IPylonRegistry.
     */
    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        IPylonRegistry.removeUser(worldObj, getCoordSet());
    }

    /**
     * Used to check to see if it's powered from IPylonRegistry.
     */
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        notifyPylonUpdate();
    }

}
