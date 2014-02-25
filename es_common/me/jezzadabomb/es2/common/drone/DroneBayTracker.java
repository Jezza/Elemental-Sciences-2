package me.jezzadabomb.es2.common.drone;

import java.util.ArrayList;
import java.util.Random;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.CoordSetF;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.interfaces.IMasterable;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.TileES;
import net.minecraft.world.World;

public class DroneBayTracker implements IMasterable {

    ArrayList<TileDroneBay> droneBayList, spawnableBayList;

    ArrayList<EntityConstructorDrone> droneList, workingList, movingList, controllableList, spawnableDroneList;
    private TileConsole console;
    private World world;
    private int x, y, z, totalSpawnableDrones;

    public DroneBayTracker() {
        // List of all found drone bays.
        droneBayList = new ArrayList<TileDroneBay>();
        // List of all drone bays capable of spawning drones.
        spawnableBayList = new ArrayList<TileDroneBay>();

        // All drones deployed.
        droneList = new ArrayList<EntityConstructorDrone>();
        // All drones currently working.
        workingList = new ArrayList<EntityConstructorDrone>();
        // All drones currently moving.
        movingList = new ArrayList<EntityConstructorDrone>();
        // All drones currently idle.
        controllableList = new ArrayList<EntityConstructorDrone>();

        totalSpawnableDrones = 0;
    }

    /**
     * Basically used to manage the lists of the drones. Called from the tick of the console that owns this object. Haven't decided how often this should get called, as it's going to be very intensive.
     */
    public void updateTick() {
        // Drone bay and Drone management
        updateDroneAndDroneBays();

        workingList.clear();
        movingList.clear();
        controllableList.clear();

        for (EntityConstructorDrone drone : droneList) {
            // if (drone.isWorking())
            // workingList.add(drone);
            if (drone.isMoving())
                movingList.add(drone);
        }

        controllableList.addAll(droneList);
        controllableList.removeAll(workingList);
        controllableList.removeAll(movingList);
    }

    public void updateDroneAndDroneBays() {
        droneList.clear();
        droneBayList.clear();
        spawnableBayList.clear();
        totalSpawnableDrones = 0;
        
        int width = 10;

        for (int i = -width; i < width + 1; i++)
            for (int j = -width; j < width + 1; j++)
                for (int k = -width; k < width + 1; k++)
                    if (UtilMethods.isDroneBay(world, x + i, y + j, z + k)) {
                        TileDroneBay droneBay = (TileDroneBay) world.getTileEntity(x + i, y + j, z + k);
                        droneBayList.add(droneBay);
                        if (droneBay.droneTracker.canSpawn()) {
                            spawnableBayList.add(droneBay);
                            droneList.addAll(droneBay.droneTracker.droneList);
                        }
                        totalSpawnableDrones += droneBay.droneTracker.getTotalSpawnableDrones();
                    }
    }

    public int spawnDrone(int num, CoordSetF coordSetF) {
        if (num <= 0)
            return 0;
        TileDroneBay droneBay = getDroneBay();
        if (droneBay != null)
            return droneBay.addDroneToSpawnList(num, coordSetF);
        return 0;
    }

    public int sendDronesToXYZ(int count, CoordSetF coordSetF) {
        if (count <= 0)
            return 0;

        updateTick();

        int dronesToSpawn = count - controllableList.size();

        if (world.isRemote)
            return dronesToSpawn;

        spawnDrone(dronesToSpawn, coordSetF);

        ArrayList<EntityConstructorDrone> utilList = new ArrayList<EntityConstructorDrone>();
        utilList.addAll(controllableList);

        int index = 0;
        for (EntityConstructorDrone drone : utilList) {
            drone.addCoordSetFToQueue(coordSetF);
            if (++index >= count)
                break;
        }

        return dronesToSpawn;
    }

    public int sendDronesToXYZ(int count, int x, int y, int z) {
        return sendDronesToXYZ(count, new CoordSet(x, y, z).toCoordSetF());
    }

    public TileDroneBay getDroneBay() {
        if (droneBayList.isEmpty() || spawnableBayList.isEmpty())
            return null;

        return spawnableBayList.get(new Random().nextInt(spawnableBayList.size()));
    }

    public int getControllableCount() {
        return controllableList.size();
    }

    @Override
    public void setMaster(TileES tileES) {
        if (!(tileES instanceof TileConsole))
            return;
        this.console = (TileConsole) tileES;
        world = console.getWorldObj();
        x = console.xCoord;
        y = console.yCoord;
        z = console.zCoord;
    }

    @Override
    public boolean hasMaster() {
        return console != null && world != null;
    }

    @Override
    public TileES getMaster() {
        return console;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Master Console: " + console.getCoordSet() + System.lineSeparator());
        stringBuilder.append("Total Drone Bays: " + droneBayList.size() + System.lineSeparator());
        stringBuilder.append("Spawnable Drone Bays: " + spawnableBayList.size() + System.lineSeparator());
        stringBuilder.append("Total Drone Count: " + droneList.size() + System.lineSeparator());
        stringBuilder.append("Working Drone Count: " + workingList.size() + System.lineSeparator());
        stringBuilder.append("Moving Drone Count: " + movingList.size() + System.lineSeparator());
        stringBuilder.append("Total Controllable Count: " + controllableList.size() + System.lineSeparator());
        stringBuilder.append("Total Spawnable Drones: " + totalSpawnableDrones);

        return stringBuilder.toString();
    }
}
