package me.jezzadabomb.es2.common.drone;

import java.util.ArrayList;
import java.util.Random;

import me.jezzadabomb.es2.common.core.interfaces.IMasterable;
import me.jezzadabomb.es2.common.core.utils.Identifier;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSetD;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;
import net.minecraft.world.World;

public class DroneBayTracker implements IMasterable {

    ArrayList<TileDroneBay> droneBayList;

    ArrayList<EntityConstructorDrone> droneList, workingList, movingList, controllableList, spawnableDroneList;
    private TileConsole console;
    private World world;
    private int x, y, z, totalSpawnableDrones;

    public DroneBayTracker() {
        // List of all found drone bays, the ones capable of spawning.
        droneBayList = new ArrayList<TileDroneBay>();

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
        updateDroneBays();

        updateDrones();
    }

    public void updateDroneBays() {
        droneList.clear();
        droneBayList.clear();
        totalSpawnableDrones = 0;

        int width = 10;
        int height = 4;

        for (int i = -width; i < width + 1; i++)
            for (int j = -height; j < height + 1; j++)
                for (int k = -width; k < width + 1; k++)
                    if (Identifier.isDroneBay(world, x + i, y + j, z + k)) {
                        TileDroneBay droneBay = (TileDroneBay) world.getTileEntity(x + i, y + j, z + k);
                        droneBayList.add(droneBay);
                        totalSpawnableDrones += droneBay.getItemDroneCount();
                        droneList.addAll(droneBay.droneTracker.droneList);
                    }
    }

    public void updateDrones() {
        // workingList.clear();
        movingList.clear();
        controllableList.clear();

        for (EntityConstructorDrone drone : droneList) {
            // if (drone.isWorking())
            // workingList.add(drone);
            if (drone.isMoving()) {
                movingList.add(drone);
            } else {
                controllableList.add(drone);
            }
        }
    }

    public int spawnDrone(int num, CoordSetD coordSetD) {
        if (num <= 0)
            return 0;
        TileDroneBay droneBay = getDroneBay();
        if (droneBay != null)
            return droneBay.addDroneToSpawnList(num, coordSetD);
        return 0;
    }

    public int sendDronesToXYZ(int count, CoordSetD coordSetD) {
        updateTick();
        if (count <= 0)
            return 0;

        int dronesToSpawn = count - controllableList.size();

        if (world.isRemote)
            return dronesToSpawn;

        int rt = spawnDrone(dronesToSpawn, coordSetD);

        ArrayList<EntityConstructorDrone> utilList = new ArrayList<EntityConstructorDrone>();
        utilList.addAll(controllableList);

        int index = 0;
        for (EntityConstructorDrone drone : utilList) {
            drone.addCoordSetDToQueue(coordSetD);
            if (++index >= count)
                break;
        }

        return dronesToSpawn;
    }

    public int sendDronesToXYZ(int count, int x, int y, int z) {
        return sendDronesToXYZ(count, new CoordSet(x, y, z).toCoordSetD());
    }

    public TileDroneBay getDroneBay() {
        if (droneBayList.isEmpty())
            return null;

        return droneBayList.get(new Random().nextInt(droneBayList.size()));
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
        stringBuilder.append("Total Drone Count: " + droneList.size() + System.lineSeparator());
        stringBuilder.append("Working Drone Count: " + workingList.size() + System.lineSeparator());
        stringBuilder.append("Moving Drone Count: " + movingList.size() + System.lineSeparator());
        stringBuilder.append("Total Controllable Count: " + controllableList.size() + System.lineSeparator());
        stringBuilder.append("Total Spawnable Drones: " + totalSpawnableDrones);

        return stringBuilder.toString();
    }
}
