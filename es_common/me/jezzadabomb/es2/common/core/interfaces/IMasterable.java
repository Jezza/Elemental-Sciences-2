package me.jezzadabomb.es2.common.core.interfaces;

import net.minecraft.world.World;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;

public interface IMasterable {

    public void setMaster(CoordSet coordSet, World world);

    public boolean hasMaster();

    public CoordSet getMaster();

}
