package me.jezzadabomb.es2.client.utils;

import me.jezzadabomb.es2.common.packets.InventoryPacket;

public class CoordSet {

    double x, y, z;

    public CoordSet(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CoordSet(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CoordSet(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public boolean isPacket(InventoryPacket p){
        return x == p.x && y == p.y && z == p.z;
    }
    
    @Override
    public boolean equals(Object other){
        if(other == null)return false;
        if(!(other instanceof CoordSet))return false;
        CoordSet coordSet = (CoordSet)other;
        return(coordSet.x == x && coordSet.y == y && coordSet.z == z);
    }
}
