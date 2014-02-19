package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSetF;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.entities.EntityDrone;
import me.jezzadabomb.es2.common.interfaces.IDismantleable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileDroneBay extends TileES implements IDismantleable {

    private boolean opening, closing, changed;
    private float doorProgress;
    public float doorStepAmount = 0.05F;

    public int totalSpawnableDrones;

    private ArrayList<EntityDrone> spawnList;
    private ArrayList<EntityDrone> spawnedList;
    private ArrayList<EntityDrone> droneList;

    private ArrayList<EntityDrone> removingList;

    public TileDroneBay() {
        doorProgress = 1.0F;
        opening = closing = false;

        spawnList = new ArrayList<EntityDrone>();
        spawnedList = new ArrayList<EntityDrone>();
        droneList = new ArrayList<EntityDrone>();

        removingList = new ArrayList<EntityDrone>();
        totalSpawnableDrones = 0;
    }

    @Override
    public void updateEntity() {
        totalSpawnableDrones = getTotalDronesInInventory();

        if (changed)
            markForUpdate();

        doorProgress = MathHelper.clamp_float(doorProgress, 0.0F, 1.0F);

        if (spawnList.size() > 0 && !opening) {
            openHatch();
        }

        if (!changed && (isOpened() || isClosed()))
            stopDoorMovement();
        else
            changed = false;

        stepDoor();

        if (spawnList.size() > 0 && isOpened())
            spawnDronesFromList();

        ArrayList<EntityDrone> utilList = new ArrayList<EntityDrone>();
        utilList.addAll(spawnedList);
        for (EntityDrone drone : utilList)
            if (drone.hasReachedTarget()) {
                spawnedList.remove(drone);
                droneList.add(drone);
            }

    }

    private void spawnDronesFromList() {
        ArrayList<EntityDrone> utilList = new ArrayList<EntityDrone>();
        utilList.addAll(spawnList);
        for (EntityDrone drone : utilList) {
            drone.posX = xCoord + 0.5F;
            drone.posY = yCoord - 0.5F;
            drone.posZ = zCoord + 0.5F;

            CoordSetF targetSet = new CoordSetF(xCoord + 0.5F, yCoord + 1.5F, zCoord + 0.5F);

            drone.addTargetCoordsToHead(targetSet);
            drone.setMaster(this);

            worldObj.spawnEntityInWorld(drone);

            spawnedList.add(drone);
            spawnList.remove(drone);
        }
    }

    public ArrayList<EntityDrone> getDroneList() {
        return droneList;
    }

    public void stepDoor() {
        if (opening)
            doorProgress -= doorStepAmount;
        if (closing)
            doorProgress += doorStepAmount;
    }

    public ItemStack getDroneFromInventory(boolean remove) {
        if (UtilMethods.isIInventory(worldObj, xCoord, yCoord - 1, zCoord)) {
            IInventory inventory = (IInventory) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack itemStack = inventory.getStackInSlot(i);
                if (itemStack == null)
                    continue;
                if (ItemStack.areItemStacksEqual(itemStack, ModItems.getPlaceHolderStack("constructorDrone"))) {
                    if (remove)
                        inventory.decrStackSize(i, 1);
                    return itemStack;
                }
            }
        }
        return null;
    }

    public ArrayList<ItemStack> getSpawnableDronesFromInventory(boolean remove) {
        ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();

        if (UtilMethods.isIInventory(worldObj, xCoord, yCoord - 1, zCoord)) {
            IInventory inventory = (IInventory) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack itemStack = inventory.getStackInSlot(i);
                if (itemStack != null && ItemStack.areItemStacksEqual(itemStack, ModItems.getPlaceHolderStack("constructorDrone"))) {
                    itemList.add(itemStack);
                    if (remove)
                        inventory.setInventorySlotContents(i, (ItemStack) null);
                }
            }
        }

        return itemList;
    }

    public int getTotalDronesInInventory() {
        return getSpawnableDronesFromInventory(false).size();
    }

    public boolean canSpawnDrone(int num) {
        return getTotalDronesInInventory() >= num;
    }

    public int addDroneToSpawnList(int dronesToSpawn, CoordSetF coordSetF) {
        if (dronesToSpawn <= 0)
            return 0;

        ArrayList<ItemStack> itemList = getSpawnableDronesFromInventory(true);

        if (itemList.size() <= 0)
            return 0;

        if (itemList.size() < dronesToSpawn)
            dronesToSpawn = itemList.size();

        if (worldObj.isRemote)
            return dronesToSpawn;

        int index = 0;
        for (ItemStack item : itemList) {
            EntityDrone drone = new EntityDrone(worldObj);
            if (coordSetF != null)
                drone.addTargetCoords(coordSetF);
            spawnList.add(drone);
            if (++index >= dronesToSpawn)
                break;
        }

        ESLogger.info(spawnList);
        
        return dronesToSpawn;
    }

    public void markForUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void stopDoorMovement() {
        opening = false;
        closing = false;
    }

    public void openHatch() {
        opening = changed = true;
        closing = false;
    }

    public void closeHatch() {
        closing = changed = true;
        opening = false;
    }

    public void toggleDoor() {
        if (closing || isClosed())
            openHatch();
        else if (opening || isOpened())
            closeHatch();
    }

    public boolean isOpening() {
        return opening;
    }

    public boolean isClosing() {
        return closing;
    }

    public float getDoorProgress() {
        return doorProgress;
    }

    public boolean isOpened() {
        return doorProgress <= 0.0F;
    }

    public boolean isClosed() {
        return doorProgress >= 1.0F;
    }

    public boolean isOverChestRenderType() {
        TileEntity te = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        return UtilMethods.isRenderType(te, Blocks.chest.getRenderType());
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

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setFloat("doorProgress", doorProgress);
        tag.setBoolean("closing", closing);
        tag.setBoolean("opening", opening);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        doorProgress = tag.getFloat("doorProgress");
        closing = tag.getBoolean("closing");
        opening = tag.getBoolean("opening");
    }

    @Override
    public ItemStack dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnBlock) {
        world.setBlockToAir(x, y, z);
        if (!world.isRemote && returnBlock)
            world.spawnEntityInWorld(new EntityItem(world, x + 0.5F, y + 0.1F, z + 0.5F, new ItemStack(ModBlocks.droneBay)));
        return null;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z) {
        return true;
    }

}
