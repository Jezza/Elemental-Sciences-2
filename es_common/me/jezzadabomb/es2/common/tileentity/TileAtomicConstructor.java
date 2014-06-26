package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.interfaces.IMasterable;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileAtomicConstructor extends TileES implements IMasterable {

    boolean[] renderMatrix;
    TileConsole tileConsole;
    boolean registered = false;
    int renderTimeTicked = 0;
    boolean didSearch, canConstructRenderMatrix;

    public TileAtomicConstructor() {
        didSearch = false;
        canConstructRenderMatrix = true;
    }

    @Override
    public void updateEntity() {
        if (canConstructRenderMatrix && ++renderTimeTicked <= 5) {
            constructRenderMatrix();
        } else {
            canConstructRenderMatrix = false;
        }

        if (!didSearch && tileConsole == null && worldObj != null)
            if (!findNewConsole()) {
                didSearch = true;
                return;
            }

        if (tileConsole == null)
            return;

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
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                for (int k = -1; k <= 1; k++) {
                    CoordSet coordSet = new CoordSet(xCoord + i, yCoord + j, zCoord + k);
                    if (!(i == 0 && j == 0 && k == 0) && coordSet.isConstructor(worldObj)) {
                        TileAtomicConstructor tile = (TileAtomicConstructor) coordSet.getTileEntity(worldObj);
                        if (tile.hasMaster()) {
                            setMaster(tile.getMaster(), worldObj);
                            return true;
                        }
                    }
                }
        return false;
    }

    private boolean findConsoleNearby() {
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                for (int k = -1; k <= 1; k++) {
                    CoordSet coordSet = new CoordSet(xCoord + i, yCoord + j, zCoord + k);
                    if (!(i == 0 && j == 0 && k == 0) && coordSet.isConsole(worldObj)) {
                        TileConsole tile = (TileConsole) coordSet.getTileEntity(worldObj);
                        setMaster(tile.getCoordSet(), worldObj);
                        return true;
                    }
                }
        return false;
    }

    public void propogateMaster() {
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                for (int k = -1; k <= 1; k++) {
                    CoordSet coordSet = new CoordSet(xCoord + i, yCoord + j, zCoord + k);
                    if (!(i == 0 && j == 0 && k == 0) && coordSet.isConstructor(worldObj)) {
                        TileAtomicConstructor atomic = (TileAtomicConstructor) coordSet.getTileEntity(worldObj);
                        if (atomic.hasMaster())
                            continue;
                        atomic.setMaster(tileConsole.getCoordSet(), worldObj);
                    }
                }
    }

    public boolean[] getRenderMatrix() {
        return renderMatrix;
    }

    @Override
    public void onNeighbourBlockChange(CoordSet coordSet) {
        constructRenderMatrix();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
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
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                for (int k = -1; k <= 1; k++) {
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
    public void setMaster(CoordSet coordSet, World world) {
        if (coordSet.isConsole(world)) {
            tileConsole = (TileConsole) coordSet.getTileEntity(world);
            propogateMaster();
        }
    }

    @Override
    public boolean hasMaster() {
        return tileConsole != null && !tileConsole.isInvalid();
    }

    @Override
    public CoordSet getMaster() {
        return tileConsole.getCoordSet();
    }
}
