package me.jezzadabomb.es2.common.drone;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSetF;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.interfaces.IMasterable;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.TileES;
import net.minecraft.world.World;

public class DroneRemovingTracker implements IMasterable {

    TileDroneBay droneBay;
    World world;

    public ArrayList<EntityConstructorDrone> removingList;
    private ArrayList<EntityConstructorDrone> waitingList;
    private ArrayList<EntityConstructorDrone> utilList;

    public DroneRemovingTracker() {
        removingList = new ArrayList<EntityConstructorDrone>();
        waitingList = new ArrayList<EntityConstructorDrone>();
        utilList = new ArrayList<EntityConstructorDrone>();
    }

    public void processingTick() {
        utilList.clear();
        utilList.addAll(removingList);

        for (EntityConstructorDrone drone : utilList) {
            if (drone.reachedFinalTarget()) {
                droneBay.openHatch();
                drone.addCoordSetFToHead(droneBay.getCoordSet().toCoordSetF().subtractY(0.5F));
                removingList.remove(drone);
                waitingList.add(drone);
            }
        }

        utilList.clear();
        utilList.addAll(waitingList);

        for (EntityConstructorDrone drone : utilList) {
            if (drone.reachedFinalTarget()) {
                drone.setDead();
                waitingList.remove(drone);
                droneBay.addDroneToChest(drone);
            }
            if (removingList.isEmpty() && waitingList.isEmpty())
                droneBay.planToClose(UtilMethods.getTimeInTicks(0, 0, 1, 10));
        }
    }

    public void removeAllDrones() {
        removeDrones(Integer.MAX_VALUE);
    }

    public void removeDrones(int count) {
        removingList.clear();

        ArrayList<EntityConstructorDrone> utilList = new ArrayList<EntityConstructorDrone>();
        utilList.addAll(droneBay.droneTracker.droneList);

        ESLogger.info(utilList);

        if (utilList.isEmpty())
            return;

        CoordSetF droneSet = droneBay.getCoordSet().toCoordSetF();

        int index = 0;
        for (EntityConstructorDrone drone : utilList) {
            drone.replaceCoordSetQueue(droneSet.addY(1.5F));
            removingList.add(drone);
            if (++index >= count)
                break;
        }
    }

    @Override
    public void setMaster(TileES tileES) {
        if (tileES instanceof TileDroneBay) {
            droneBay = (TileDroneBay) tileES;
            world = droneBay.getWorldObj();
        }
    }

    @Override
    public boolean hasMaster() {
        return droneBay != null && world != null;
    }

    @Override
    public TileES getMaster() {
        return droneBay;
    }

}
