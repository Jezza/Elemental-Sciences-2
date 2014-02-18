package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.tickers.QuantumBombTicker;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class TileQuantumStateDisruptor extends TileES {

    private boolean registered, placed;

    public TileQuantumStateDisruptor() {
        registered = placed = false;
    }

    @Override
    public void updateEntity() {
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

    public void removeSelf() {
        // Yoda conditions! No null check for me. :P
        if (ModBlocks.quantumStateDisrupter.equals(worldObj.getBlock(xCoord, yCoord, zCoord)))
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        if (placed && Blocks.bedrock.equals(worldObj.getBlock(xCoord, yCoord - 1, zCoord)))
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        invalidate();
        worldObj.createExplosion(null, xCoord, yCoord, zCoord, 2.5F, true);
    }
}
