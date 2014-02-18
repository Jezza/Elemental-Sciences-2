package me.jezzadabomb.es2.common.interfaces;

import me.jezzadabomb.es2.common.tileentity.TileES;

public interface IMasterable {

    public void setMaster(TileES tileES);
    
    public boolean hasMaster();
    
    public TileES getMaster();
    
}
