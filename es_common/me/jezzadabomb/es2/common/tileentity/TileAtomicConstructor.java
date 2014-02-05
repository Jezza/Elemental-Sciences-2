package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;
import java.util.Random;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.TimeTracker;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.entities.EntityDrone;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cofh.api.block.IDismantleable;
import cofh.api.energy.IEnergyHandler;

public class TileAtomicConstructor extends TileES implements IDismantleable {

    boolean[] renderMatrix;
    TileConsole tileConsole;
    boolean registered = false;
    TimeTracker timeTracker;
    boolean marked;
    int timeTicked = 0;

    public TileAtomicConstructor() {
        timeTracker = new TimeTracker();
        marked = false;
    }

    @Override
    public void updateEntity() {
        if (renderMatrix == null || ++timeTicked >= 100) {
            constructRenderMatrix();
            timeTicked = 0;
        }
        if (!marked) {
            marked = true;
            timeTracker.markTime(worldObj);
        }
        if (tileConsole == null) {
            if (timeTracker.hasDelayPassed(worldObj, 50 + new Random().nextInt(50))) {
                findNewConsole();
                marked = false;
            }
            return;
        }
        if (tileConsole.isInvalid()) {
            resetState();
            return;
        }
        if (!registered && !tileConsole.isInvalid()) {
            registered = tileConsole.registerAtomicConstructor(this);
        }
    }

    public ArrayList<EntityDrone> getAllDrones() {
        ArrayList<EntityDrone> droneList = new ArrayList<EntityDrone>();
        droneList.addAll(worldObj.getEntitiesWithinAABB(EntityDrone.class, AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1.0F, yCoord + 1.0F, zCoord + 1.0F)));

        return droneList;
    }

    public void resetState() {
        tileConsole = null;
        registered = false;
    }

    private boolean findNewConsole() {
        if (!findConsoleFromNearbyConstructors())
            findConsoleNearby();
        return tileConsole != null;
    }

    private boolean findConsoleFromNearbyConstructors() {
        for (int i = -1; i < 2; i++)
            for (int j = -1; j < 2; j++)
                for (int k = -1; k < 2; k++)
                    if (!(i == 0 && j == 0 && k == 0) && UtilMethods.isConstructor(worldObj, xCoord + i, yCoord + j, zCoord + k)) {
                        TileAtomicConstructor tile = (TileAtomicConstructor) worldObj.getBlockTileEntity(xCoord + i, yCoord + j, zCoord + k);
                        if (tile.hasConsole()) {
                            tileConsole = tile.getConsole();
                            return true;
                        }
                    }
        return false;
    }

    private boolean findConsoleNearby() {
        for (int i = -1; i < 2; i++)
            for (int j = -1; j < 2; j++)
                for (int k = -1; k < 2; k++)
                    if (!(i == 0 && j == 0 && k == 0) && UtilMethods.isConsole(worldObj, xCoord + i, yCoord + j, zCoord + k)) {
                        TileConsole tile = (TileConsole) worldObj.getBlockTileEntity(xCoord + i, yCoord + j, zCoord + k);
                        tileConsole = tile;
                        return true;
                    }
        return false;
    }

    public boolean hasConsole() {
        return tileConsole != null;
    }

    public TileConsole getConsole() {
        return tileConsole;
    }

    public void markForUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
    }

    @Override
    public void onDataPacket(INetworkManager networkManager, Packet132TileEntityData packet) {
        readFromNBT(packet.data);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);
    }

    public boolean[] getRenderMatrix() {
        return renderMatrix;
    }

    @Override
    public void onNeighbourBlockChange(CoordSet coordSet) {
        constructRenderMatrix();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if (i == 0 && j == 0 && k == 0 || !worldObj.blockHasTileEntity(xCoord + i, yCoord + j, zCoord + k))
                        continue;
                    if (worldObj.getBlockTileEntity(xCoord + i, yCoord + j, zCoord + k) instanceof TileAtomicConstructor)
                        ((TileAtomicConstructor) worldObj.getBlockTileEntity(xCoord + i, yCoord + j, zCoord + k)).constructRenderMatrix();
                }
            }
        }
    }

    public boolean[] constructRenderMatrix() {
        Boolean[] localArray = new Boolean[26];
        int index = 0;
        for (int i = -1; i < 2; i++)
            for (int j = -1; j < 2; j++)
                for (int k = -1; k < 2; k++) {
                    if (i == 0 && j == 0 && k == 0)
                        continue;
                    localArray[index++] = worldObj.getBlockId(xCoord + i, yCoord + j, zCoord + k) == ModBlocks.atomicConstructor.blockID;
                }
        return renderMatrix = new boolean[] { (!(localArray[10])), (!(localArray[0] && localArray[1] && localArray[3] && localArray[4] && localArray[9] && localArray[10] && localArray[12])), (!(localArray[9] && localArray[10] && localArray[12] && localArray[17] && localArray[18] && localArray[20] && localArray[21])), (!(localArray[11] && localArray[13] && localArray[22] && localArray[19] && localArray[21] && localArray[18] && localArray[10])), (!(localArray[10] && localArray[11] && localArray[13] && localArray[2] && localArray[5] && localArray[1] && localArray[4])), (!(localArray[12] && localArray[14] && localArray[15])), (!(localArray[4] && localArray[7] && localArray[15])), (!(localArray[15] && localArray[24] && localArray[21])), (!(localArray[15] && localArray[16] && localArray[13])), (!(localArray[21] && localArray[22] && localArray[13])), (!(localArray[12] && localArray[4] && localArray[3])), (!(localArray[4] && localArray[5] && localArray[13])), (!(localArray[12] && localArray[21] && localArray[20])), (!(localArray[15] && localArray[7] && localArray[4] && localArray[5] && localArray[13] && localArray[16] && localArray[8])), (!(localArray[4] && localArray[7] && localArray[15] && localArray[12] && localArray[3] && localArray[6] && localArray[14])), (!(localArray[15] && localArray[16] && localArray[13] && localArray[22] && localArray[25] && localArray[24] && localArray[21])), (!(localArray[15] && localArray[14] && localArray[12] && localArray[21] && localArray[20] && localArray[23] && localArray[24])), (!(localArray[10] && localArray[9] && localArray[12])), (!(localArray[10] && localArray[21] && localArray[18])), (!(localArray[10] && localArray[11] && localArray[13])), (!(localArray[10] && localArray[1] && localArray[4])) };
    }

    public boolean registerDrone(EntityDrone drone) {
        if (hasConsole())
            return getConsole().registerDrone(drone);
        return false;
    }

    public boolean isPartRendering(int pos) {
        if (renderMatrix != null)
            return renderMatrix[pos];
        return false;
    }

    @Override
    public String toString() {
        return "Constructor" + new CoordSet(xCoord, yCoord, zCoord);
    }

    @Override
    public ItemStack dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnBlock) {
        world.setBlockToAir(x, y, z);
        if (!world.isRemote && returnBlock)
            world.spawnEntityInWorld(new EntityItem(world, x + 0.5F, y + 0.1F, z + 0.5F, new ItemStack(ModBlocks.atomicConstructor)));
        return null;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z) {
        return true;
    }
}
