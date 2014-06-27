package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.tickers.QuantumBombTicker;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileQuantumStateDisruptor extends TileES {

    private boolean registered, placed;

    public TileQuantumStateDisruptor() {
        registered = placed = false;
    }

    @Override
    public void updateEntity() {
        invalidate();
        if (worldObj == null)
            return;
        if (worldObj != null && !worldObj.isRemote && !registered) {
            registered = true;
            QuantumBombTicker.addToWatchList(this);
        }

        boolean hasAirBelow = worldObj.isAirBlock(xCoord, yCoord - 1, zCoord);

        if (hasAirBelow) {
            placed = true;
            worldObj.setBlock(xCoord, yCoord - 1, zCoord, Blocks.bedrock);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord, zCoord - 1, xCoord + 2, yCoord, zCoord + 2);
    }

    public void removeSelf() {
        // Yoda conditions! No null check for me. :P
        if (ModBlocks.quantumStateDisrupter.equals(worldObj.getBlock(xCoord, yCoord, zCoord))) {
            worldObj.removeTileEntity(xCoord, yCoord, zCoord);
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        }
        if (placed && Blocks.bedrock.equals(worldObj.getBlock(xCoord, yCoord - 1, zCoord)))
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        invalidate();
        worldObj.createExplosion(null, xCoord, yCoord, zCoord, 2.5F, true);
    }
}
