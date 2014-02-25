package me.jezzadabomb.es2.common.drone;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.core.utils.CoordSetF;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.interfaces.IMasterable;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.TileES;

public class DroneTracker implements IMasterable {

    private TileDroneBay droneBay;

    public DroneSpawningTracker spawningTracker;
    public DroneRemovingTracker removingTracker;

    public ArrayList<EntityConstructorDrone> droneList;
    public ArrayList<EntityConstructorDrone> utilList;

    public DroneTracker() {
        spawningTracker = new DroneSpawningTracker();
        removingTracker = new DroneRemovingTracker();

        droneList = new ArrayList<EntityConstructorDrone>(100);
        utilList = new ArrayList<EntityConstructorDrone>(100);
    }

    @Override
    public void setMaster(TileES tileES) {
        if (tileES instanceof TileDroneBay) {
            TileDroneBay droneBay = (TileDroneBay) tileES;

            spawningTracker.setMaster(droneBay);
            removingTracker.setMaster(droneBay);
        }
    }

    public void updateTick() {
        spawningTracker.processingTick();
        removingTracker.processingTick();

        utilList.clear();
        utilList.addAll(droneList);

        for (EntityConstructorDrone drone : utilList)
            if (drone.isDead)
                droneList.remove(drone);
    }

    public void recallDrones(int count) {
        if (count < 0) {
            removingTracker.removeAllDrones();
        } else if (count > 0) {
            removingTracker.removeDrones(count);
        }
    }

    public boolean processSpawnedDrone(EntityConstructorDrone drone) {
        boolean flag = spawningTracker.processSpawnedDrone(drone);
        if (flag)
            droneList.add(drone);
        return flag;
    }

    public int addDrones(int dronesToSpawn, CoordSetF coordSetF) {
        return spawningTracker.addDronesToSpawnList(dronesToSpawn, coordSetF);
    }

    public int getTotalSpawnableDrones() {
        return spawningTracker.itemList.size();
    }

    public void spawnDrones() {
        spawningTracker.spawnDronesFromList();
    }

    public boolean needsToSpawn() {
        return spawningTracker.spawnList.size() > 0;
    }

    public boolean canSpawn() {
        return !spawningTracker.itemList.isEmpty();
    }

    @Override
    public boolean hasMaster() {
        return droneBay != null && droneBay.getWorldObj() != null;
    }

    @Override
    public TileES getMaster() {
        return droneBay;
    }

}
