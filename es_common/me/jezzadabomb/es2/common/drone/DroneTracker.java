package me.jezzadabomb.es2.common.drone;

import java.util.ArrayList;
import java.util.Random;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.CoordSetF;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.entities.EntityDrone;
import me.jezzadabomb.es2.common.interfaces.IMasterable;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.TileES;
import net.minecraft.world.World;

public class DroneTracker implements IMasterable {

    ArrayList<TileDroneBay> droneBayList, spawnableBayList;

    ArrayList<EntityDrone> droneList, workingList, movingList, controllableList;
    private TileConsole console;
    private World world;
    private int x, y, z;

    public DroneTracker() {
        // List of all found drone bays.
        droneBayList = new ArrayList<TileDroneBay>();
        // List of all drone bays capable of spawning drones.
        spawnableBayList = new ArrayList<TileDroneBay>();

        // All drones deployed.
        droneList = new ArrayList<EntityDrone>();
        // All drones currently working.
        workingList = new ArrayList<EntityDrone>();
        // All drones currently moving.
        movingList = new ArrayList<EntityDrone>();
        // All drones currently idle.
        controllableList = new ArrayList<EntityDrone>();
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

        for (EntityDrone drone : droneList) {
            if (drone.isWorking())
                workingList.add(drone);
            if (drone.isMoving())
                movingList.add(drone);
        }

        controllableList.addAll(droneList);
        controllableList.removeAll(workingList);
        controllableList.removeAll(movingList);
    }

    public void updateDroneAndDroneBays() {
        droneBayList.clear();
        int width = 10;
        for (int i = -width; i < width + 1; i++)
            for (int j = -width; j < width + 1; j++)
                for (int k = -width; k < width + 1; k++)
                    if (UtilMethods.isDroneBay(world, x + i, y + j, z + k)) {
                        TileDroneBay droneBay = (TileDroneBay) world.getTileEntity(x + i, y + j, z + k);
                        droneBayList.add(droneBay);
                        if (droneBay.canSpawnDrone()) {
                            spawnableBayList.add(droneBay);
                            droneList.addAll(droneBay.getDroneList());
                        }
                    }
    }

    public int spawnDrone(int num) {
        if (num <= 0)
            return 0;
        TileDroneBay droneBay = getDroneBay();
        if (droneBay != null)
            return droneBay.addDroneToSpawnList(num);
        return 0;
    }

    public int sendDronesToXYZ(int count, CoordSet coordSet) {
        return sendDronesToXYZ(count, coordSet.getX(), coordSet.getY(), coordSet.getZ());
    }

    public int sendDronesToXYZ(int count, int x, int y, int z) {
        count = MathHelper.clipInt(count, droneList.size());

        if (count <= 0)
            return 0;

        int dronesToSpawn = count - controllableList.size();

        if (dronesToSpawn > 0) {
            spawnDrone(dronesToSpawn);
            updateDroneAndDroneBays();
            sendDronesToXYZ(dronesToSpawn, x, y, z);
        }

        ArrayList<EntityDrone> utilList = new ArrayList<EntityDrone>();
        utilList.addAll(controllableList);

        int index = 0;
        for (EntityDrone drone : utilList) {
            if (++index >= count)
                break;
            drone.addTargetCoords(new CoordSetF(x + 0.5F, y + 0.5F, z + 0.5F));
        }

        return index;
    }

    public TileDroneBay getDroneBay() {
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
        return world != null;
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

        return stringBuilder.toString();
    }
}
