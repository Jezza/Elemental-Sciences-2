package me.jezzadabomb.es2.common.drone;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.interfaces.IMasterable;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSetD;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DroneSpawningTracker implements IMasterable {

    TileDroneBay droneBay;
    World world;
    int xCoord, yCoord, zCoord, spawnTimer, droneCount;
    ArrayList<EntityConstructorDrone> spawnList, utilList, spawnedList;

    public DroneSpawningTracker() {
        spawnList = new ArrayList<EntityConstructorDrone>();
        spawnedList = new ArrayList<EntityConstructorDrone>();
        utilList = new ArrayList<EntityConstructorDrone>();

        spawnTimer = 0;
    }

    /**
     * Do this at some point.
     */
    public void processingTick() {

    }

    public int addDronesToSpawnList(int dronesToSpawn, CoordSetD... coordSetD) {
        updateDroneCountFromInventory();

        if (droneCount < dronesToSpawn)
            dronesToSpawn = droneCount;

        if (world.isRemote)
            return dronesToSpawn;

        for (int i = 0; i < dronesToSpawn; i++) {
            EntityConstructorDrone drone = new EntityConstructorDrone(world);

            drone.addCoordSetDToQueue(coordSetD);

            spawnList.add(drone);
            droneBay.removeItemDrones(1);
            droneCount--;
        }

        return dronesToSpawn;
    }

    public void updateDroneCountFromInventory() {
        droneCount = droneBay.getItemDroneCount();
    }

    public void spawnDronesFromList() {
        if (++spawnTimer <= 15)
            return;

        ArrayList<EntityConstructorDrone> utilList = new ArrayList<EntityConstructorDrone>();
        utilList.addAll(spawnList);

        for (EntityConstructorDrone drone : utilList) {
            drone.posX = xCoord + 0.5F;
            drone.posY = yCoord - 0.5F;
            drone.posZ = zCoord + 0.5F;

            drone.addCoordSetDToHead(droneBay.getCoordSet().toCoordSetD());
            drone.setMaster(droneBay.getCoordSet(), world);

            if (world.spawnEntityInWorld(drone)) {
                spawnList.remove(drone);
                spawnedList.add(drone);
                spawnTimer = 0;
                break;
            }
        }
        if (spawnList.isEmpty())
            droneBay.planToClose(UtilMethods.getTimeInTicks(0, 0, 1, 10));
    }

    public boolean processSpawnedDrone(EntityConstructorDrone drone) {
        if (spawnedList.remove(drone)) {
            return true;
        } else {
            ESLogger.severe("TRYING TO REGISTER A DRONE ALREADY SPAWNED!");
            drone.setDead();
        }
        return false;
    }

    public boolean needsToSpawn() {
        return !spawnList.isEmpty();
    }

    @Override
    public void setMaster(CoordSet coordSet, World world) {
        this.world = world;
        xCoord = coordSet.getX();
        yCoord = coordSet.getY();
        zCoord = coordSet.getZ();
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
