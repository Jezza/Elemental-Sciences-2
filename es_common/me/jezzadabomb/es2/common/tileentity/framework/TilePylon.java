package me.jezzadabomb.es2.common.tileentity.framework;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.IPylonRegistry;
import me.jezzadabomb.es2.common.core.interfaces.IBlockNotifier;
import me.jezzadabomb.es2.common.core.interfaces.IPylon;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import net.minecraft.world.World;

public abstract class TilePylon extends TileES implements IPylon, IBlockNotifier {

    private boolean registered = false;

    @Override
    public void updateEntity() {
        if (worldObj == null)
            return;

        if (!registered)
            registered = IPylonRegistry.registerPylon(worldObj, getCoordSet());
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        registered = IPylonRegistry.registerPylon(worldObj, getCoordSet());
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        IPylonRegistry.removePylon(worldObj, getCoordSet());
    }

    @Override
    public boolean isPowering(CoordSet coordSet) {
        int range = getPowerLevel() * 16;
        return coordSet.getDistanceSq(getCoordSet()) < (range * range);
    }
}
