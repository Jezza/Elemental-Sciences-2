package me.jezzadabomb.es2.common.hud;

import net.minecraft.tileentity.TileEntity;

public class InventoryInstance {

    private String name;
    private TileEntity tileEntity;
    private int x;
    private int y;
    private int z;

    public InventoryInstance(String name, TileEntity tileEntity, int x, int y, int z) {
        this.name = name;
        this.tileEntity = tileEntity;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public TileEntity getIInventory() {
        return tileEntity;
    }
    
    public boolean isName(String name){
        if(name == null)return false;
        if(this.name == null)return false;
        return this.name.compareTo(name) == 0;
    }
    
    @Override
    public boolean equals(Object other){
        if(other == null)return false;
        if(!(other instanceof InventoryInstance))return false;
        InventoryInstance inventory = (InventoryInstance)other;
        return(inventory.x == x && inventory.y == y && inventory.z == z && isName(inventory.name) && inventory.tileEntity == tileEntity);
    }
    
    @Override
    public String toString() {
        return "Name: " + name + ", TileEntity: " + tileEntity + " X: " + x + " Y:" + y + " Z: " + z;
    }
}
