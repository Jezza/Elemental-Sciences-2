package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;

import cpw.mods.fml.client.ExtendedServerListData;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.PacketDispatcher;
import me.jezzadabomb.es2.common.core.utils.CoordSetF;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.drone.DroneSpawningTracker;
import me.jezzadabomb.es2.common.drone.DroneTracker;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.interfaces.IDismantleable;
import me.jezzadabomb.es2.common.network.packet.server.DroneBayDoorPacket;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileDroneBay extends TileES implements IDismantleable {

    public DroneTracker droneTracker;

    private boolean opening, closing, changed, registeredSpawn, planToClose;
    private float doorProgress;
    public float doorStepAmount = 0.05F;

    public int totalSpawnableDrones, timeTicked, currentTickTime, planTickTime;

    public TileDroneBay() {
        droneTracker = new DroneTracker();

        opening = closing = registeredSpawn = planToClose = false;

        doorProgress = 1.0F;
        totalSpawnableDrones = planTickTime = currentTickTime = 0;
        timeTicked = 20;
    }

    @Override
    public void updateEntity() {
        if (worldObj == null)
            return;

        if (!UtilMethods.isIInventory(worldObj, xCoord, yCoord - 1, zCoord) && !worldObj.isRemote) {
            worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, new ItemStack(ModBlocks.droneBay)));
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
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
            droneTracker.setMaster(this);
            registeredSpawn = droneTracker.hasMaster();
        }

        if (!worldObj.isRemote)
            if (++timeTicked >= UtilMethods.getTimeInTicks(0, 0, 0, 10)) {
                droneTracker.updateTick();
                timeTicked = 0;
            }
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
        ESLogger.info("Recalling drones");
        droneTracker.recallDrones(count);
    }

    public void planToClose(int ticks) {
        planTickTime = ticks;
        planToClose = true;
    }

    public boolean processSpawnedDrone(EntityConstructorDrone drone) {
        return droneTracker.processSpawnedDrone(drone);
    }

    public int addDroneToSpawnList(int dronesToSpawn, CoordSetF coordSetF) {
        if (dronesToSpawn <= 0)
            return 0;

        return droneTracker.addDrones(dronesToSpawn, coordSetF);
    }

    public int getItemDroneCount() {
        return 0;
    }

    public int removeItemDrones(int count) {
        return 0;
    }

    public boolean addDroneToChest(EntityConstructorDrone drone) {
        ItemStack itemStack = ModItems.getPlaceHolderStack("constructorDrone");

        IInventory inventory = (IInventory) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        boolean flag = UtilMethods.addItemStackToIInventory(inventory, itemStack);

        return flag;
    }

    public void stepDoor() {
        if (opening)
            doorProgress -= doorStepAmount;
        if (closing)
            doorProgress += doorStepAmount;
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
