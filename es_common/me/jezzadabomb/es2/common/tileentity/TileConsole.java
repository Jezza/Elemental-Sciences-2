package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.containers.ContainerConsole;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.interfaces.IDismantleable;
import me.jezzadabomb.es2.common.core.interfaces.IRotatable;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.drone.DroneBayTracker;
import me.jezzadabomb.es2.common.gui.GuiConsole;
import me.jezzadabomb.es2.common.gui.GuiConsoleTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileConsole extends TileES implements IDismantleable, IRotatable {

    ArrayList<TileAtomicConstructor> constructorList;

    public DroneBayTracker droneBayTracker;
    public GuiConsoleTracker guiTracker;

    public int randomHoverSeed;

    BitSet renderCables;
    int direction, timeTicked;

    // Relative to the console.
    float guiX, guiY, guiZ;
    boolean notified;

    public TileConsole() {
        constructorList = new ArrayList<TileAtomicConstructor>();
        droneBayTracker = new DroneBayTracker();
        guiTracker = new GuiConsoleTracker();

        renderCables = new BitSet(4);
        direction = timeTicked = 0;
        notified = false;
        randomHoverSeed = new Random().nextInt(100);

        updateRenderCables();
        droneBayMaintenance();
    }

    public void openGui(World world, EntityPlayer player) {
        guiTracker.openGui(world, player);
    }

    public void closeGui(World world, EntityPlayer player) {
        guiTracker.closeGui(world, player);
    }

    public boolean isGuiOpened() {
        return !guiTracker.getPlayerList().isEmpty();
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1.0F, yCoord + 2.0F, zCoord + 1.0F);
    }

    @Override
    public void updateEntity() {
        if (!notified && worldObj != null) {
            notifyNearbyConstructors();
            notified = true;
        }
        // notifyNearbyConstructors();
        if (!droneBayTracker.hasMaster())
            droneBayTracker.setMaster(this);

        atomicMaintenance();

        if (constructorList.isEmpty()) {
            timeTicked = 0;
            return;
        }

        // if (++timeTicked > UtilMethods.getTimeInTicks(0, 0, 1, 0)) {
        droneBayMaintenance();
        // timeTicked = 0;
        // }
    }

    private void droneBayMaintenance() {
        if (worldObj != null && !worldObj.isRemote)
            droneBayTracker.updateTick();
    }

    private void notifyNearbyConstructors() {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (i == 0 && j == 0 && k == 0)
                        continue;
                    if (UtilMethods.isConstructor(worldObj, xCoord + i, yCoord + j, zCoord + k)) {
                        TileAtomicConstructor atomic = (TileAtomicConstructor) worldObj.getTileEntity(xCoord + i, yCoord + j, zCoord + k);
                        if (atomic.hasMaster())
                            continue;
                        atomic.setMaster(this);
                    }
                }
            }
        }
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

        int result = droneBayTracker.sendDronesToXYZ(1, new CoordSet(tileAtomic).toCoordSetD());
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
        stringBuilder.append(droneBayTracker.toString());

        return stringBuilder.toString();
    }

    @Override
    public Object getGui(int id, Side side, EntityPlayer player) {
        if (side.isClient()) {
            return new GuiConsole(player.inventory, this);
        }
        if (side.isServer()) {
            return new ContainerConsole(player.inventory, this);
        }
        return null;
    }
}
