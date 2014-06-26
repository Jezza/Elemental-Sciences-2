package me.jezzadabomb.es2.common.drone;

import java.util.ArrayList;

import net.minecraft.world.World;

import me.jezzadabomb.es2.common.core.interfaces.IMasterable;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSetD;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;

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
    public void setMaster(CoordSet coordSet, World world) {
        spawningTracker.setMaster(coordSet, world);
        removingTracker.setMaster(coordSet, world);
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

    public int addDrones(int dronesToSpawn, CoordSetD coordSetD) {
        return spawningTracker.addDronesToSpawnList(dronesToSpawn, coordSetD);
    }

    public void spawnDrones() {
        spawningTracker.spawnDronesFromList();
    }

    public boolean needsToSpawn() {
        return spawningTracker.spawnList.size() > 0;
    }

    @Override
    public boolean hasMaster() {
        return droneBay != null && droneBay.getWorldObj() != null;
    }

    @Override
    public CoordSet getMaster() {
        return droneBay.getCoordSet();
    }

}
