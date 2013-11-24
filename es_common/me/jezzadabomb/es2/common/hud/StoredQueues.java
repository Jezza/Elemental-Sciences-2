package me.jezzadabomb.es2.common.hud;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;

public class StoredQueues {

    private static final StoredQueues instance = new StoredQueues();

    private ArrayList<InventoryInstance> inventories = new ArrayList<InventoryInstance>();
    private ArrayList<InventoryInstance> tempInv = new ArrayList<InventoryInstance>();
    private ArrayList<InventoryInstance> requestedList = new ArrayList<InventoryInstance>();

    public StoredQueues() {
    }

    public static StoredQueues instance() {
        return instance;
    }

    public ArrayList<InventoryInstance> getPlayer() {
        return inventories;
    }
    
    public void putInventory(String name, TileEntity inventory, int x, int y, int z) {
        inventories.add(new InventoryInstance(name, inventory, x, y, z));
    }

    public void retainInventories(ArrayList<InventoryInstance> map){
        inventories.retainAll(map);
    }
    
    public void putInventory(InventoryInstance inventory) {
        inventories.add(inventory);
    }

    public boolean isAlreadyInQueue(InventoryInstance inventory) {
        return inventories.contains(inventory);
    }

    public boolean isXYZInventory(int x, int y, int z) {
        for(InventoryInstance i : inventories){
            if(i.getX() == x && i.getY() == y && i.getZ() == z){
                return true;
            }
        }
        return false;
    }
    
    public boolean isAtXYZ(int x, int y, int z){
        for(InventoryInstance i : inventories){
            if(i.getX() == x && i.getY() == y && i.getZ() == z){
                return true;
            }
        }
        return false;
    }
    
    public InventoryInstance getAtXYZ(int x, int y, int z){
        for(InventoryInstance i : inventories){
            if(i.getX() == x && i.getY() == y && i.getZ() == z){
                return i;
            }
        }
        return null;
    }
    
    public void replaceAtXYZ(int x, int y, int z, InventoryInstance inventory){
        if(isXYZInventory(x, y, z)){
            for(InventoryInstance i : inventories){
                if(i.getX() == x && i.getY() == y && i.getZ() == z){
                    inventories.remove(i);
                    inventories.add(i);
                }
            }
        }
    }

    public void removeXYZInventory(String player, int x, int y, int z) {
        for(InventoryInstance i : inventories){
            if(i.getX() == x && i.getY() == y && i.getZ() == z){
                inventories.remove(i);
            }
        }
    }
    
    public void clearTempInv(){
        tempInv.clear();
    }
    
    public ArrayList<InventoryInstance> getTempInv(){
        return tempInv;
    }
    
    public void putTempInventory(InventoryInstance inventory){
        tempInv.add(inventory);
    }
    
    public void clearPacketInv(){
        requestedList.clear();
    }
    
    public ArrayList<InventoryInstance> getRequestList(){
        return requestedList;
    }
    
    public void putRequestedInv(InventoryInstance inventory){
        requestedList.add(inventory);
    }
    
    public void setLists(){
        requestedList = tempInv;
    }
    
    public void removeTemp(){
        tempInv.removeAll(requestedList);
    }
}
