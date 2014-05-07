package me.jezzadabomb.es2.client.hud;

import java.util.ArrayList;
import java.util.HashSet;

import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;

public class StoredQueues {

    private static StoredQueues INSTANCE = new StoredQueues();

    // Previously stored blocks from the last tick. Used with tempInv.
    private HashSet<CoordSet> inventories = new HashSet<CoordSet>();

    // The current set of inventories from this tick, used to flush all old inventories.
    private HashSet<CoordSet> tempInv = new HashSet<CoordSet>();

    // This is the list that contains all inventories to have their inventories requested.
    private HashSet<CoordSet> requestedList = new HashSet<CoordSet>();

    public void putInventory(CoordSet coordSet) {
        inventories.add(coordSet);
        tempInv.add(coordSet);
    }

    public boolean isAtXYZ(CoordSet coordSet) {
        return inventories.contains(coordSet);
    }

    public HashSet<CoordSet> getRequestList() {
        return requestedList;
    }

    public boolean canSendRequestList() {
        return !requestedList.isEmpty();
    }

    /**
     * The inventories list is flushed with the current inventory list, so all that remains is the ones still around this scan process. All packets we sent last tick, we'll remove from this round, because no point sending them so quickly. NOTE: This means it's once a second by default.
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

    public static StoredQueues getInstance() {
        return INSTANCE;
    }
}
