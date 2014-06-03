package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.api.multiblock.DimensionalPattern.Flag;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.IPylonRegistry;
import me.jezzadabomb.es2.common.core.interfaces.IBlockNotifier;
import me.jezzadabomb.es2.common.core.interfaces.IPylon;
import me.jezzadabomb.es2.common.tileentity.framework.TilePylon;
import net.minecraft.world.World;

public class TilePylonCrystal extends TilePylon implements IPylon, IBlockNotifier {

    private boolean registered = false;
    private int tier = 0;

    public TilePylonCrystal(int tier) {
        this.tier = tier;
    }

    public TilePylonCrystal() {
    }

    @Override
    public void updateEntity() {
        if (worldObj == null)
            return;

        if (!registered)
            registered = IPylonRegistry.registerPylon(worldObj, getCoordSet(), tier);
    }

    public int getTier() {
        return tier;
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        // Fix this
        getStrengthenedIronPattern().convert(worldObj, xCoord, yCoord - 3, zCoord, Flag.IGNORE);
        for (int i = 1; i <= 3; i++)
            worldObj.func_147480_a(xCoord, yCoord - i, zCoord, false);
        getStrengthenedIronPattern().convert(worldObj, xCoord, yCoord - 3, zCoord, Flag.IGNORE);
        worldObj.setBlock(xCoord, yCoord, zCoord, ModBlocks.pylonCrystal, tier + 3, 3);
        registered = !IPylonRegistry.removePylon(worldObj, getCoordSet(), tier);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {

    }
}
