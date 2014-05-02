package me.jezzadabomb.es2.common.core.interfaces;

import net.minecraft.world.World;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;

public interface IMasterable {

    public void setMaster(CoordSet coordSet, World world);

    public boolean hasMaster();

    public CoordSet getMaster();

}
