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

    // TODO Revisit with new circle object, easier projection and locating.
    
    private static HashMap<Integer, HashSet<CoordSet4>> pylonMap = new HashMap<Integer, HashSet<CoordSet4>>();

    public static boolean registerPylon(World world, CoordSet coordSet, int tier) {
        int dimID = world.provider.dimensionId;
        confirm(dimID);

        CoordSet4 coordSet4 = new CoordSet4(coordSet, tier);
        pylonMap.get(dimID).add(coordSet4);
//        broadcastPylonUpdate(world, coordSet, tier);
        return pylonMap.get(dimID).contains(coordSet4);
    }

    public static boolean registerPylon(World world, int x, int y, int z, int tier) {
        return registerPylon(world, new CoordSet(x, y, z), tier);
    }

    public static boolean removePylon(World world, CoordSet coordSet, int tier) {
        int dimID = world.provider.dimensionId;
        confirm(dimID);
        
        CoordSet4 coordSet4 = new CoordSet4(coordSet, tier);
        pylonMap.get(dimID).remove(coordSet4);
//        broadcastPylonUpdate(world, coordSet, tier);
        return !pylonMap.get(dimID).contains(coordSet4);
    }

    public static boolean removePylon(World world, int x, int y, int z, int tier) {
        return removePylon(world, new CoordSet(x, y, z), tier);
    }

    private static void broadcastPylonUpdate(World world, CoordSet coordSet, int tier) {
        int range = (tier + 1) * Reference.PYLON_POWER_RANGE;
        for (int i = -range; i <= range; i++)
            for (int j = -range; j <= range; j++)
                for (int k = -range; k <= range; k++) {
                    int x = coordSet.getX() + i;
                    int y = coordSet.getY() + j;
                    int z = coordSet.getZ() + k;

                    if (world.isAirBlock(x, y, z))
                        continue;

                    TileEntity tileEntity = world.getTileEntity(x, y, z);
                    if (tileEntity instanceof IPylonReceiver)
                        ((IPylonReceiver) tileEntity).pylonNotifyUpdate();
                }
    }

    public static int isPowered(World world, CoordSet coordSet) {
        // pylonMap.clear();
        int dimID = world.provider.dimensionId;
        confirm(dimID);

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

    private static void confirm(int dimID) {
        if (!pylonMap.containsKey(dimID))
            pylonMap.put(dimID, new HashSet<CoordSet4>());
    }
}
