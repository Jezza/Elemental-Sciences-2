package me.jezzadabomb.es2.common.entities;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityDrone extends EntityLiving {

    // 1 - idle
    // 2 - working
    int timer, mode;
    boolean withinConstructor, pathed;

    public EntityDrone(World world) {
        super(world);
        timer = 0;
        withinConstructor = true;
        pathed = false;
        setAIMoveSpeed(0.5F);
        setSize(0.05F, 0.05F);
    }

    @Override
    protected void entityInit() {
        super.entityInit();

    }

    @Override
    protected boolean isAIEnabled() {
        return true;
    }

    public boolean isIdle() {
        return mode == 1;
    }

    public boolean isWorking() {
        return mode == 2;
    }

    public void setIdle() {
        mode = 1;
    }

    public void setWorking() {
        mode = 2;
    }

    @Override
    public void onUpdate() {
        withinConstructor = isWithinBlockID(ModBlocks.atomicConstructor.blockID);
        if (withinConstructor)
            pathed = false;
        if (!withinConstructor && !pathed)
            pathToNewConstructor();

    }

    private void pathToNewConstructor() {
        ArrayList<TileAtomicConstructor> atomicList = new ArrayList<TileAtomicConstructor>();

        for (int i = -5; i < 6; i++)
            for (int j = -5; j < 6; j++)
                for (int k = -5; k < 6; k++)
                    if (UtilMethods.isConstructor(worldObj, (int) Math.floor(posX) + i, (int) Math.floor(posY) + j, (int) Math.floor(posZ) + k))
                        atomicList.add((TileAtomicConstructor) worldObj.getBlockTileEntity((int) Math.floor(posX) + i, (int) Math.floor(posY) + j, (int) Math.floor(posZ) + k));

        for (TileAtomicConstructor atomic : atomicList) {
            if (getNavigator().tryMoveToXYZ(atomic.xCoord, atomic.yCoord, atomic.zCoord, getAIMoveSpeed())) {
                pathed = true;
                break;
            }
        }
    }

    private boolean isWithinBlockID(int blockID) {
        return worldObj.getBlockId((int) Math.floor(posX), (int) Math.floor(posY), (int) Math.floor(posZ)) == blockID;
    }

    private boolean hitGround() {
        return worldObj.getBlockId((int) Math.floor(posX), (int) (Math.floor(posY - 0.15F)), (int) Math.floor(posZ)) != 0;
    }
    
    @Override
    public float getCollisionBorderSize() {
        return 0.0F;
    }
}
