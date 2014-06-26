package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.network.PacketDispatcher;
import me.jezzadabomb.es2.common.core.network.packet.server.DroneBayDoorPacket;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSetD;
import me.jezzadabomb.es2.common.core.utils.helpers.InventoryHelper;
import me.jezzadabomb.es2.common.drone.DroneTracker;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class TileDroneBay extends TileES {

    public DroneTracker droneTracker;

    private boolean opening, closing, changed, registeredSpawn, planToClose;
    private float doorProgress;
    public float doorStepAmount = 0.05F;

    public int timeTicked, currentTickTime, planTickTime;

    public TileDroneBay() {
        droneTracker = new DroneTracker();

        opening = closing = registeredSpawn = planToClose = false;

        doorProgress = 1.0F;
        planTickTime = currentTickTime = 0;
        timeTicked = 20;
    }

    @Override
    public void updateEntity() {
        if (worldObj == null)
            return;

        if (!hasInventory() && !worldObj.isRemote) {
            worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, new ItemStack(ModBlocks.droneBay)));
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            worldObj.removeTileEntity(xCoord, yCoord, zCoord);
            return;
        }

        trackerMaintenance();

        doorMaintenance();

        if (droneTracker.needsToSpawn() && isOpened())
            droneTracker.spawnDrones();

        if (planToClose) {
            if (++currentTickTime >= planTickTime) {
                closeHatch();
                planTickTime = currentTickTime = 0;
                planToClose = false;
            }
        }
    }

    private void trackerMaintenance() {
        if (!registeredSpawn && !droneTracker.hasMaster()) {
            droneTracker.setMaster(getCoordSet(), worldObj);
            registeredSpawn = droneTracker.hasMaster();
        }

        if (!worldObj.isRemote)
            // if (++timeTicked >= UtilMethods.getTimeInTicks(0, 0, 0, 10)) {
            droneTracker.updateTick();
        // timeTicked = 0;
        // }
    }

    private void doorMaintenance() {
        doorProgress = MathHelper.clamp_float(doorProgress, 0.0F, 1.0F);

        if (droneTracker.needsToSpawn() && !opening) {
            openHatch();
        }

        if (!changed && (isOpened() || isClosed()))
            stopDoorMovement();
        else
            changed = false;

        stepDoor();
    }

    public void recallDrones(int count) {
        droneTracker.recallDrones(count);
    }

    public void planToClose(int ticks) {
        planTickTime = ticks;
        planToClose = true;
    }

    public boolean processSpawnedDrone(EntityConstructorDrone drone) {
        return droneTracker.processSpawnedDrone(drone);
    }

    public int addDroneToSpawnList(int dronesToSpawn, CoordSetD coordSetD) {
        if (dronesToSpawn <= 0)
            return 0;

        return droneTracker.addDrones(dronesToSpawn, coordSetD);
    }

    public boolean hasInventory() {
        return getInventory() != null;
    }

    public IInventory getInventory() {
        return (IInventory) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
    }

    public int getItemDroneCount() {
        IInventory inventory = getInventory();

        if (inventory == null)
            return 0;

        int count = 0;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (itemStack == null || !(InventoryHelper.areItemStacksEqual(itemStack, ModItems.getPlaceHolderStack("constructorDrone"))))
                continue;
            count++;
        }

        return count;
    }

    public int removeItemDrones(int count) {
        return InventoryHelper.removeItemStackFromIInventory(getInventory(), ModItems.getPlaceHolderStack("constructorDrone"), count);
    }

    public boolean addDroneToChest(EntityConstructorDrone drone) {
        ItemStack itemStack = ModItems.getPlaceHolderStack("constructorDrone");

        IInventory inventory = getInventory();
        boolean flag = InventoryHelper.addItemStackToIInventory(inventory, itemStack);

        return flag;
    }

    public void stepDoor() {
        if (opening)
            doorProgress -= doorStepAmount;
        if (closing)
            doorProgress += doorStepAmount;
    }

    public void stopDoorMovement() {
        opening = false;
        closing = false;
    }

    public void openHatch() {
        opening = changed = true;
        closing = false;
        if (!worldObj.isRemote)
            PacketDispatcher.sendToAllAround(new DroneBayDoorPacket(this, opening), new TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 60));
    }

    public void closeHatch() {
        closing = changed = true;
        opening = false;
        if (!worldObj.isRemote)
            PacketDispatcher.sendToAllAround(new DroneBayDoorPacket(this, opening), new TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 60));
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
        Block block = worldObj.getBlock(xCoord, yCoord - 1, zCoord);
        return UtilMethods.isRenderType(block, Blocks.chest.getRenderType());
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
}
