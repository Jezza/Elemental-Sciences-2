package me.jezzadabomb.es2.common.tileentity;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.interfaces.IDismantleable;
import me.jezzadabomb.es2.common.interfaces.IMasterable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class TileAtomicConstructor extends TileES implements IDismantleable, IMasterable {

    boolean[] renderMatrix;
    TileConsole tileConsole;
    boolean registered = false;
    int renderTimeTicked = 0;
    int timeTicked = 0;
    boolean canSearch, canConstructRenderMatrix;

    public TileAtomicConstructor() {
        canSearch = true;
        canConstructRenderMatrix = true;
    }

    @Override
    public void updateEntity() {
        if (canConstructRenderMatrix && ++renderTimeTicked <= 5) {
            constructRenderMatrix();
        } else {
            canConstructRenderMatrix = false;
        }

        if (canSearch && tileConsole == null) {
            if (!findNewConsole()) {
                canSearch = false;
                return;
            }
        } else {
            canSearch = ++timeTicked >= UtilMethods.getTimeInTicks(0, 0, 5, 0);
            return;
        }

        if (tileConsole.isInvalid()) {
            resetState();
            return;
        }
        if (!registered && !tileConsole.isInvalid())
            registered = tileConsole.registerAtomicConstructor(this);
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
                        TileAtomicConstructor tile = (TileAtomicConstructor) worldObj.getTileEntity(xCoord + i, yCoord + j, zCoord + k);
                        if (tile.hasMaster()) {
                            tileConsole = ((TileConsole) tile.getMaster());
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
                        TileConsole tile = (TileConsole) worldObj.getTileEntity(xCoord + i, yCoord + j, zCoord + k);
                        tileConsole = tile;
                        return true;
                    }
        return false;
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
                    TileEntity tileEntity = worldObj.getTileEntity(xCoord + i, yCoord + j, zCoord + k);
                    if (i == 0 && j == 0 && k == 0 || tileEntity == null)
                        continue;
                    if (tileEntity instanceof TileAtomicConstructor)
                        ((TileAtomicConstructor) tileEntity).constructRenderMatrix();
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
                    // Yoda conditions! No null check, because effort.
                    localArray[index++] = ModBlocks.atomicConstructor.equals(worldObj.getBlock(xCoord + i, yCoord + j, zCoord + k));
                }
        // @formatter:off
        return renderMatrix = new boolean[] {
                (!(localArray[10])),
                (!(localArray[0] && localArray[1] && localArray[3] && localArray[4] && localArray[9] && localArray[10] && localArray[12])),
                (!(localArray[9] && localArray[10] && localArray[12] && localArray[17] && localArray[18] && localArray[20] && localArray[21])),
                (!(localArray[11] && localArray[13] && localArray[22] && localArray[19] && localArray[21] && localArray[18] && localArray[10])),
                (!(localArray[10] && localArray[11] && localArray[13] && localArray[2] && localArray[5] && localArray[1] && localArray[4])),
                (!(localArray[12] && localArray[14] && localArray[15])),
                (!(localArray[4] && localArray[7] && localArray[15])), 
                (!(localArray[15] && localArray[24] && localArray[21])),
                (!(localArray[15] && localArray[16] && localArray[13])),
                (!(localArray[21] && localArray[22] && localArray[13])),
                (!(localArray[12] && localArray[4] && localArray[3])),
                (!(localArray[4] && localArray[5] && localArray[13])),
                (!(localArray[12] && localArray[21] && localArray[20])),
                (!(localArray[15] && localArray[7] && localArray[4] && localArray[5] && localArray[13] && localArray[16] && localArray[8])),
                (!(localArray[4] && localArray[7] && localArray[15] && localArray[12] && localArray[3] && localArray[6] && localArray[14])),
                (!(localArray[15] && localArray[16] && localArray[13] && localArray[22] && localArray[25] && localArray[24] && localArray[21])),
                (!(localArray[15] && localArray[14] && localArray[12] && localArray[21] && localArray[20] && localArray[23] && localArray[24])),
                (!(localArray[10] && localArray[9] && localArray[12])),
                (!(localArray[10] && localArray[21] && localArray[18])),
                (!(localArray[10] && localArray[11] && localArray[13])),
                (!(localArray[10] && localArray[1] && localArray[4]))
        };
        // @formatter:on
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

    @Override
    public void setMaster(TileES tileES) {
        if (tileES instanceof TileConsole)
            tileConsole = (TileConsole) tileES;
    }

    @Override
    public boolean hasMaster() {
        return tileConsole != null;
    }

    @Override
    public TileES getMaster() {
        return tileConsole;
    }
}
