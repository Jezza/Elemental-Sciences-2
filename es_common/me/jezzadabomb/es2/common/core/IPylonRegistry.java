package me.jezzadabomb.es2.common.core;

import java.util.HashMap;
import java.util.HashSet;

import me.jezzadabomb.es2.common.core.interfaces.IPylon;
import me.jezzadabomb.es2.common.core.interfaces.IPylonReceiver;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class IPylonRegistry {

    private static HashMap<Integer, HashSet<IPylon>> pylonMap = new HashMap<Integer, HashSet<IPylon>>();
    private static HashMap<Integer, HashSet<CoordSet>> userMap = new HashMap<Integer, HashSet<CoordSet>>();

    public static boolean registerPylon(World world, IPylon pylon) {
        int dimID = world.provider.dimensionId;
        confirmPylon(dimID);

        pylonMap.get(dimID).add(pylon);
        broadcastPylonUpdate(world);
        return pylonMap.get(dimID).contains(pylon);
    }

    public static boolean removePylon(World world, IPylon pylon) {
        int dimID = world.provider.dimensionId;
        confirmPylon(dimID);

        pylonMap.get(dimID).remove(pylon);
        broadcastPylonUpdate(world);
        return !pylonMap.get(dimID).contains(pylon);
    }

    public static boolean registerUser(World world, CoordSet coordSet) {
        int dimID = world.provider.dimensionId;
        confirmUser(dimID);

        userMap.get(dimID).add(coordSet);
        return userMap.get(dimID).contains(coordSet);
    }

    public static boolean removeUser(World world, CoordSet coordSet) {
        int dimID = world.provider.dimensionId;
        confirmUser(dimID);

        userMap.get(dimID).remove(coordSet);
        return !userMap.get(dimID).contains(coordSet);
    }

    private static void broadcastPylonUpdate(World world) {
        int dimID = world.provider.dimensionId;
        confirmUser(dimID);
        for (CoordSet coordSet : userMap.get(dimID)) {
            TileEntity tileEntity = coordSet.getTileEntity(world);
            if (tileEntity instanceof IPylonReceiver)
                ((IPylonReceiver) tileEntity).notifyPylonUpdate();
        }
    }

    public static IPylon isPowered(World world, CoordSet coordSet) {
        int dimID = world.provider.dimensionId;
        confirmPylon(dimID);

        if (pylonMap.get(dimID).isEmpty())
            return null;

        for (IPylon pylon : pylonMap.get(dimID))
            if (pylon.isPowering(coordSet))
                return pylon;
        return null;
    }

    private static void confirmPylon(int dimID) {
        if (!pylonMap.containsKey(dimID))
            pylonMap.put(dimID, new HashSet<IPylon>());
    }

    private static void confirmUser(int dimID) {
        if (!userMap.containsKey(dimID))
            userMap.put(dimID, new HashSet<CoordSet>());
    }
}
