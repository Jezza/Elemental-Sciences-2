package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.entities.EntityDrone;
import me.jezzadabomb.es2.common.interfaces.IDismantleable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;

public class TileConsole extends TileES implements IDismantleable {

    ArrayList<TileAtomicConstructor> constructorList;
    ArrayList<EntityDrone> droneList;
    ArrayList<TileDroneBay> droneBayList;
    int direction, timeTicked;
    BitSet renderCables;

    public TileConsole() {
        constructorList = new ArrayList<TileAtomicConstructor>();
        droneList = new ArrayList<EntityDrone>();
        droneBayList = new ArrayList<TileDroneBay>();
        direction = 0;
        timeTicked = 0;
        renderCables = new BitSet(4);
        updateRenderCables();
        droneBayMaintenance();
    }

    @Override
    public void updateEntity() {
        atomicMaintenance();
        if (++timeTicked > 40)
            droneBayMaintenance();
    }

    private void atomicMaintenance() {
        ArrayList<TileAtomicConstructor> utilList = new ArrayList<TileAtomicConstructor>();

        for (TileAtomicConstructor atomic : constructorList)
            if (atomic.isInvalid())
                utilList.add(atomic);

        if (utilList.size() > 0) {
            constructorList.removeAll(utilList);
            disconnectAll();
        }
    }

    private void droneBayMaintenance() {

    }

    public TileAtomicConstructor getRandomConstructor() {
        if (constructorList.isEmpty())
            return null;
        return constructorList.get(new Random().nextInt(constructorList.size()));
    }

    public void disconnectAll() {
        for (TileAtomicConstructor atomic : constructorList)
            atomic.resetState();
        constructorList.clear();
        droneList.clear();
    }

    public ArrayList<EntityDrone> getDroneList() {
        return droneList;
    }

    @Override
    public void onNeighbourBlockChange(CoordSet coordSet) {
        updateRenderCables();
        for (int i = -1; i < 2; i++)
            for (int j = -1; j < 2; j++)
                for (int k = -1; k < 2; k++) {
                    if (i == 0 && j == 0 && k == 0)
                        continue;
                    if (UtilMethods.isConsole(worldObj, xCoord + i, yCoord + j, zCoord + k))
                        ((TileConsole) worldObj.getTileEntity(xCoord + i, yCoord + j, zCoord + k)).updateRenderCables();
                }
    }

    public int getDroneSize() {
        return droneList.size();
    }

    public boolean addDrone(EntityDrone drone) {
        droneList.add(drone);
        return droneList.contains(drone);
    }

    public EntityDrone getRandomDrone() {
        return droneList.get(new Random().nextInt(droneList.size()));
    }

    public void markForUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void updateRenderCables() {
        renderCables.clear();
        renderCables.set(0, isMatch(xCoord - 1, yCoord, zCoord));
        renderCables.set(1, isMatch(xCoord + 1, yCoord, zCoord));
        renderCables.set(2, isMatch(xCoord, yCoord, zCoord - 1));
        renderCables.set(3, isMatch(xCoord, yCoord, zCoord + 1));
    }

    public BitSet getRenderCables() {
        return renderCables;
    }

    private boolean isMatch(int x, int y, int z) {
        return worldObj != null && (UtilMethods.isConsole(worldObj, x, y, z) || UtilMethods.isConstructor(worldObj, x, y, z));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger("direction", direction);

        int[] idArray = new int[droneList.size()];
        int i = 0;
        for (EntityDrone drone : droneList)
            idArray[i++] = drone.getEntityId();

        tag.setIntArray("droneList", idArray);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        direction = tag.getInteger("direction");

        processDroneNBT(tag.getIntArray("droneList"));
        updateRenderCables();
    }

    private void processDroneNBT(int[] idArray) {
        if (idArray == null || worldObj == null)
            return;

        droneList.clear();
        for (Object object : worldObj.loadedEntityList) {
            if (object instanceof EntityDrone) {
                EntityDrone drone = (EntityDrone) object;
                for (int i : idArray)
                    if (drone.getEntityId() == i)
                        addDrone(drone);
            }
        }
    }

    public boolean registerDrone(EntityDrone drone) {
        if (!droneList.contains(drone))
            droneList.add(drone);
        return droneList.contains(drone);
    }

    private void registerDrones(ArrayList<EntityDrone> allDrones) {
        boolean add = true;
        for (EntityDrone drone : allDrones) {
            for (EntityDrone temp : droneList) {
                if (drone.getEntityId() == temp.getEntityId()) {
                    add = false;
                    break;
                }
            }

            if (add)
                droneList.add(drone);
            add = true;
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
    }

    public boolean registerAtomicConstructor(TileAtomicConstructor atomic) {
        if (!constructorList.contains(atomic))
            constructorList.add(atomic);
        registerDrones(atomic.getAllDrones());
        return constructorList.contains(atomic);
    }

    public void setOrientation(int direction) {
        this.direction = direction;
    }

    public int getOrientation() {
        return direction;
    }

    @Override
    public ItemStack dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnBlock) {
        world.setBlockToAir(x, y, z);
        if (!world.isRemote && returnBlock)
            world.spawnEntityInWorld(new EntityItem(world, x + 0.5F, y + 0.1F, z + 0.5F, new ItemStack(ModBlocks.console)));
        return null;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z) {
        return true;
    }
}
