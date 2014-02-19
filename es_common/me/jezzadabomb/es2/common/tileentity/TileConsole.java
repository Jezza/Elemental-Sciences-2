package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.drone.DroneTracker;
import me.jezzadabomb.es2.common.interfaces.IDismantleable;
import me.jezzadabomb.es2.common.interfaces.IRotatable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileConsole extends TileES implements IDismantleable, IRotatable {

    ArrayList<TileAtomicConstructor> constructorList;

    public DroneTracker droneTracker;

    BitSet renderCables;
    int direction, timeTicked;

    public TileConsole() {
        constructorList = new ArrayList<TileAtomicConstructor>();
        droneTracker = new DroneTracker();

        renderCables = new BitSet(4);
        direction = timeTicked = 0;

        updateRenderCables();
        droneBayMaintenance();
    }

    @Override
    public void updateEntity() {
        if (!droneTracker.hasMaster())
            droneTracker.setMaster(this);

        atomicMaintenance();

        if (constructorList.isEmpty()) {
            timeTicked = 0;
            return;
        }

        if (++timeTicked > UtilMethods.getTimeInTicks(0, 0, 30, 0)) {
            droneBayMaintenance();
            timeTicked = 0;
        }
    }

    private void droneBayMaintenance() {
        if (worldObj != null && !worldObj.isRemote)
            droneTracker.updateTick();
    }

    public TileAtomicConstructor getRandomConstructor() {
        if (constructorList.isEmpty())
            return null;
        return constructorList.get(new Random().nextInt(constructorList.size()));
    }

    private void atomicMaintenance() {
        for (TileAtomicConstructor atomic : constructorList)
            if (atomic.isInvalid()) {
                disconnectAll();
                break;
            }
    }

    public void disconnectAll() {
        for (TileAtomicConstructor atomic : constructorList)
            if (!atomic.isInvalid())
                atomic.resetState();
        constructorList.clear();
    }

    public void testDatShit(TileAtomicConstructor tileAtomic) {
        if (constructorList.isEmpty())
            return;
        droneBayMaintenance();

        int result = droneTracker.sendDronesToXYZ(5, new CoordSet(tileAtomic));
        // ESLogger.info(result);
    }

    public void updateRenderCables() {
        renderCables.clear();
        renderCables.set(0, isMatch(xCoord - 1, yCoord, zCoord));
        renderCables.set(1, isMatch(xCoord + 1, yCoord, zCoord));
        renderCables.set(2, isMatch(xCoord, yCoord, zCoord - 1));
        renderCables.set(3, isMatch(xCoord, yCoord, zCoord + 1));
    }

    public boolean registerAtomicConstructor(TileAtomicConstructor atomic) {
        if (!constructorList.contains(atomic))
            constructorList.add(atomic);
        return constructorList.contains(atomic);
    }

    private boolean isMatch(int x, int y, int z) {
        return worldObj != null && (UtilMethods.isConsole(worldObj, x, y, z) || UtilMethods.isConstructor(worldObj, x, y, z));
    }

    public BitSet getRenderCables() {
        return renderCables;
    }

    @Override
    public void onNeighbourBlockChange(CoordSet coordSet) {
        updateRenderCables();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger("direction", direction);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        direction = tag.getInteger("direction");

        updateRenderCables();
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Pos " + getCoordSet() + System.lineSeparator());
        stringBuilder.append("Connected Constructors: " + constructorList.size() + System.lineSeparator());
        stringBuilder.append(droneTracker.toString());

        return stringBuilder.toString();
    }
}
