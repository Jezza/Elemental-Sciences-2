package me.jezzadabomb.es2.common.hud;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.core.utils.CoordSet;

public class StoredQueues {

    private static StoredQueues INSTANCE;

    // Previously stored blocks from the last tick. Used with tempInv.
    private ArrayList<InventoryInstance> inventories;

    // The current set of inventories from this tick, used to flush all old inventories.
    private ArrayList<InventoryInstance> tempInv;

    // This is the list that contains all inventories to have their inventories requested.
    private ArrayList<InventoryInstance> requestedList;

    public StoredQueues() {
        inventories = new ArrayList<InventoryInstance>();
        tempInv = new ArrayList<InventoryInstance>();
        requestedList = new ArrayList<InventoryInstance>();
        INSTANCE = this;
    }

    public static StoredQueues getInstance() {
        return INSTANCE;
    }

    public void putInventory(InventoryInstance inventory) {
        inventories.add(inventory);
    }

    public boolean isAlreadyInQueue(InventoryInstance inventory) {
        return inventories.contains(inventory);
    }

    public boolean isAtXYZ(int x, int y, int z) {
        return getAtXYZ(x, y, z) != null;
    }
    
    public boolean isAtXYZ(CoordSet coordSet){
        return isAtXYZ(coordSet.getX(), coordSet.getY(), coordSet.getZ());
    }

    public InventoryInstance getAtXYZ(int x, int y, int z) {
        for (InventoryInstance i : inventories)
            if (i.isAtXYZ(x, y, z))
                return i;
        return null;
    }

    public void replaceAtXYZ(int x, int y, int z, InventoryInstance inventory) {
        InventoryInstance tempInstance = getAtXYZ(x, y, z);
        if (tempInstance == null)
            return;
        inventories.set(inventories.indexOf(tempInstance), inventory);
    }

    public void putTempInventory(InventoryInstance inventory) {
        tempInv.add(inventory);
    }

    public ArrayList<InventoryInstance> getRequestList() {
        return requestedList;
    }

    /**
     * The inventories list is flushed with the current inventory list, so all that remains is the ones still around this scan process. All packets we sent last tick, we'll remove from this round, because no point sending them so quickly. NOTE: That this means it's once a second by default.
     * 
     * We clear the packetRequest list, so we can begin anew. Add all the inventories we want to view. Clear the temp list, because we have no need of it, until next tick.
     */
    public void setLists() {
        inventories.retainAll(tempInv);
        tempInv.removeAll(requestedList);
        requestedList.clear();
        requestedList.addAll(tempInv);
        tempInv.clear();
    }
}
