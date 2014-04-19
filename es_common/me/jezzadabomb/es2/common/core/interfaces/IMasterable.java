package me.jezzadabomb.es2.common.core.interfaces;

import me.jezzadabomb.es2.common.tileentity.framework.TileES;

public interface IMasterable {

    public void setMaster(TileES object);
    
    public boolean hasMaster();
    
    public TileES getMaster();
    
}
