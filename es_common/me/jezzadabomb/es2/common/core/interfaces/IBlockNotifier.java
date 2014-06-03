package me.jezzadabomb.es2.common.core.interfaces;

import net.minecraft.world.World;

public interface IBlockNotifier {

    public void onBlockRemoval(World world, int x, int y, int z);

    public void onBlockAdded(World world, int x, int y, int z);

}
