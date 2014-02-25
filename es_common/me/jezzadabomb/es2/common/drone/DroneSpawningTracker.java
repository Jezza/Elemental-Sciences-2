package me.jezzadabomb.es2.common.drone;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSetF;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.interfaces.IMasterable;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.TileES;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DroneSpawningTracker implements IMasterable {

    TileDroneBay droneBay;
    World world;
    int xCoord, yCoord, zCoord, spawnTimer;
    public ArrayList<ItemStack> itemList;
    ArrayList<EntityConstructorDrone> spawnList, utilList, spawnedList;

    public DroneSpawningTracker() {
        itemList = new ArrayList<ItemStack>();

        spawnList = new ArrayList<EntityConstructorDrone>();
        spawnedList = new ArrayList<EntityConstructorDrone>();
        utilList = new ArrayList<EntityConstructorDrone>();

        spawnTimer = 0;
    }

    /**
     * Do this at some point.
     */
    public void processingTick() {
        if (itemList.size() < 32)
            getDronesFromInventory();
    }

    public int addDronesToSpawnList(int dronesToSpawn, CoordSetF... coordSetF) {
        if (itemList.isEmpty())
            return 0;

        if (itemList.size() < dronesToSpawn)
            dronesToSpawn = itemList.size();

        if (world.isRemote)
            return dronesToSpawn;

        ArrayList<ItemStack> utilList = new ArrayList<ItemStack>();
        utilList.addAll(itemList);

        int index = 0;
        for (ItemStack item : utilList) {
            EntityConstructorDrone drone = new EntityConstructorDrone(world);
            if (coordSetF.length > 0)
                for (CoordSetF coordSet : coordSetF)
                    drone.addCoordSetFToQueue(coordSet);

            spawnList.add(drone);
            itemList.remove(0);
            if (++index >= dronesToSpawn)
                break;
        }

        return index;
    }

    public void getDronesFromInventory() {
        ArrayList<ItemStack> tempList = new ArrayList<ItemStack>();
        tempList.addAll(itemList);

        IInventory inventory = (IInventory) world.getTileEntity(xCoord, yCoord - 1, zCoord);
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (itemStack != null && ItemStack.areItemStacksEqual(itemStack, ModItems.getPlaceHolderStack("constructorDrone"))) {
                tempList.add(itemStack);
                // inventory.setInventorySlotContents(i, (ItemStack) null);
            }
        }

        inventory.markDirty();

        itemList.clear();
        itemList.addAll(tempList);
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

            drone.addCoordSetFToHead(droneBay.getCoordSet().toCoordSetF());
            drone.setMaster(droneBay);

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
    public void setMaster(TileES tileES) {
        if (tileES instanceof TileDroneBay) {
            droneBay = (TileDroneBay) tileES;
            world = droneBay.getWorldObj();
            xCoord = droneBay.xCoord;
            yCoord = droneBay.yCoord;
            zCoord = droneBay.zCoord;
        }
    }

    @Override
    public boolean hasMaster() {
        return droneBay != null && droneBay.getWorldObj() != null;
    }

    @Override
    public TileES getMaster() {
        return droneBay;
    }
}
