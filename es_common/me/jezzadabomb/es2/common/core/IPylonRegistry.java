package me.jezzadabomb.es2.common.core;

import java.util.HashMap;
import java.util.HashSet;

import me.jezzadabomb.es2.common.core.interfaces.IPylonReceiver;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet4;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class IPylonRegistry {

    // TODO Revisit with new sphere object, easier projection and locating.

    private static HashMap<Integer, HashSet<CoordSet4>> pylonMap = new HashMap<Integer, HashSet<CoordSet4>>();
    private static HashMap<Integer, HashSet<CoordSet>> userMap = new HashMap<Integer, HashSet<CoordSet>>();

    public static boolean registerPylon(World world, CoordSet coordSet, int tier) {
        int dimID = world.provider.dimensionId;
        confirmPylon(dimID);

        CoordSet4 coordSet4 = new CoordSet4(coordSet, tier);
        pylonMap.get(dimID).add(coordSet4);
        broadcastPylonUpdate(world);
        return pylonMap.get(dimID).contains(coordSet4);
    }

    public static boolean registerPylon(World world, int x, int y, int z, int tier) {
        return registerPylon(world, new CoordSet(x, y, z), tier);
    }

    public static boolean removePylon(World world, CoordSet coordSet, int tier) {
        int dimID = world.provider.dimensionId;
        confirmPylon(dimID);

        CoordSet4 coordSet4 = new CoordSet4(coordSet, tier);
        pylonMap.get(dimID).remove(coordSet4);
        broadcastPylonUpdate(world);
        return !pylonMap.get(dimID).contains(coordSet4);
    }

    public static boolean removePylon(World world, int x, int y, int z, int tier) {
        return removePylon(world, new CoordSet(x, y, z), tier);
    }

    public static boolean registerUser(World world, CoordSet coordSet) {
        int dimID = world.provider.dimensionId;
        confirmUser(dimID);

        userMap.get(dimID).add(coordSet);
        return userMap.get(dimID).contains(coordSet);
    }

    public static boolean registerUser(World world, int x, int y, int z) {
        return registerUser(world, new CoordSet(x, y, z));
    }

    public static boolean removeUser(World world, CoordSet coordSet) {
        int dimID = world.provider.dimensionId;
        confirmUser(dimID);

        userMap.get(dimID).remove(coordSet);
        return !userMap.get(dimID).contains(coordSet);
    }

    public static boolean removeUser(World world, int x, int y, int z) {
        return removeUser(world, new CoordSet(x, y, z));
    }

    private static void broadcastPylonUpdate(World world) {
        int dimID = world.provider.dimensionId;
        confirmUser(dimID);
        for (CoordSet coordSet : userMap.get(dimID)) {
            TileEntity tileEntity = world.getTileEntity(coordSet.getX(), coordSet.getY(), coordSet.getZ());
            if (tileEntity instanceof IPylonReceiver)
                ((IPylonReceiver) tileEntity).notifyPylonUpdate();
        }
    }

    public static int isPowered(World world, CoordSet coordSet) {
        int dimID = world.provider.dimensionId;
        confirmPylon(dimID);
        
        

        int highest = -1;
        for (CoordSet4 coordSet4 : pylonMap.get(dimID)) {
            if (coordSet.withinRange(coordSet4.toCoordSet(), (coordSet4.getI() + 1) * Reference.PYLON_POWER_RANGE))
                if (highest < coordSet4.getI())
                    highest = coordSet4.getI();
        }
        return highest;
    }

    public static int isPowered(World world, int x, int y, int z) {
        return isPowered(world, new CoordSet(x, y, z));
    }

    private static void confirmPylon(int dimID) {
        if (!pylonMap.containsKey(dimID))
            pylonMap.put(dimID, new HashSet<CoordSet4>());
    }

    private static void confirmUser(int dimID) {
        if (!userMap.containsKey(dimID))
            userMap.put(dimID, new HashSet<CoordSet>());
    }
}
