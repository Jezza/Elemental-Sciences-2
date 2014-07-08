package me.jezzadabomb.es2.common.core.interfaces;

import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;

public interface IPylon {
    public int getPowerLevel();

    public boolean isPowering(CoordSet coordSet);

    public CoordSet getCoordSet();
}
