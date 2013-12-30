package me.jezzadabomb.es2.client.utils;

import me.jezzadabomb.es2.common.packets.InventoryPacket;

public class CoordSet {

    private int x, y, z;

    public CoordSet(double x, double y, double z) {
        this.x = (int)x;
        this.y = (int)y;
        this.z = (int)z;
    }

    public CoordSet(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CoordSet(float x, float y, float z) {
        this.x = (int)x;
        this.y = (int)y;
        this.z = (int)z;
    }
    
    public boolean isPacket(InventoryPacket p){
    	CoordSet tempSet = p.coordSet;
        return x == tempSet.getX() && y == tempSet.getY() && z == tempSet.getZ();
    }
    
    public int getX(){
    	return x;
    }

    public int getY(){
    	return y;
    }
    
    public int getZ(){
    	return z;
    }
    
    public boolean isAtXYZ(int x, int y, int z){
    	return this.x == x && this.y == y && this.z == z;
    }
    
    @Override
    public boolean equals(Object other){
        if(other == null)return false;
        if(!(other instanceof CoordSet))return false;
        CoordSet coordSet = (CoordSet)other;
        return(coordSet.x == x && coordSet.y == y && coordSet.z == z);
    }
}
