package me.jezzadabomb.es2.common.core;

import java.util.HashMap;
import java.util.HashSet;

import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet4;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.world.World;

public class IPylonRegistry {

    private static HashMap<Integer, HashSet<CoordSet4>> pylonMap = new HashMap<Integer, HashSet<CoordSet4>>();

    public static boolean registerPylon(World world, CoordSet coordSet, int tier) {
        int dimID = world.provider.dimensionId;
        confirm(dimID);

        pylonMap.get(dimID).add(new CoordSet4(coordSet, tier));
        return pylonMap.get(dimID).contains(coordSet);
    }

    public static boolean registerPylon(World world, int x, int y, int z, int tier) {
        return registerPylon(world, new CoordSet(x, y, z), tier);
    }

    public static boolean removePylon(World world, CoordSet coordSet) {
        int dimID = world.provider.dimensionId;
        confirm(dimID);

        pylonMap.get(dimID).remove(coordSet);
        return !pylonMap.get(dimID).contains(coordSet);
    }

    public static boolean removePylon(World world, int x, int y, int z) {
        return removePylon(world, new CoordSet(x, y, z));
    }

    public static int isPowered(World world, CoordSet coordSet) {
        int dimID = world.provider.dimensionId;
        confirm(dimID);

        int highest = -1;

        for (CoordSet4 coordSet4 : pylonMap.get(dimID)) {
            if (coordSet.withinRangeDEBUG(coordSet4.toCoordSet(), (coordSet4.getI() + 1) * Reference.PYLON_POWER_RANGE))
                if (highest < coordSet4.getI())
                    highest = coordSet4.getI();
        }
        return highest;
    }

    public static int isPowered(World world, int x, int y, int z) {
        return isPowered(world, new CoordSet(x, y, z));
    }

    private static void confirm(int dimID) {
        if (!pylonMap.containsKey(dimID))
            pylonMap.put(dimID, new HashSet<CoordSet4>());
    }
}
