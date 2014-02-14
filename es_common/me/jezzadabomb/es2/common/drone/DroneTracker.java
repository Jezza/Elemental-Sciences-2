package me.jezzadabomb.es2.common.drone;

import java.util.ArrayList;
import java.util.Random;

import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import net.minecraft.world.World;

public class DroneTracker {

    ArrayList<TileDroneBay> droneBayList;
    TileConsole console;
    World world;
    int x, y, z;

    public DroneTracker(TileConsole console) {
        this.console = console;
        world = console.getWorldObj();
        x = console.xCoord;
        y = console.yCoord;
        z = console.zCoord;
        droneBayList = new ArrayList<TileDroneBay>();
    }

    public void findNewDroneBays() {
        droneBayList.clear();
        int width = 10;
        for (int i = -width; i < width + 1; i++)
            for (int j = -width; j < width + 1; j++)
                for (int k = -width; k < width + 1; k++) {
                    if (UtilMethods.isDroneBay(world, x + i, y + j, z + k)) {
                        TileDroneBay droneBay = (TileDroneBay) world.getTileEntity(x + i, y + j, z + k);
                        if (!droneBayList.contains(droneBay))
                            droneBayList.add(droneBay);
                    }
                }
    }

    public boolean canSpawnDrone() {
        for (TileDroneBay droneBay : droneBayList)
            if (droneBay.checkForDrone())
                return true;
        return false;
    }

    public void spawnDrone() {
        TileDroneBay droneBay = getDroneBay();
        if(droneBay == null)
            return;
        droneBay.spawnDrone();
    }

    public TileDroneBay getDroneBay() {
        for (TileDroneBay droneBay : droneBayList)
            if (droneBay.checkForDrone())
                return droneBay;
        return null;
    }
}
