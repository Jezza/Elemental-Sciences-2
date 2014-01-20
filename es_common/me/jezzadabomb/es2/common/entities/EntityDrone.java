package me.jezzadabomb.es2.common.entities;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityDrone extends EntityES {

    // 1 - idle
    // 2 - working
    int timer, mode;
    boolean withinConstructor;

    public EntityDrone(World world) {
        super(world);
        timer = 0;
        withinConstructor = true;
        setSize(2F / 16F, 2F / 16F);
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void updateEntity() {
        withinConstructor = isWithinBlockID(ModBlocks.atomicConstructor.blockID);
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

    private boolean isWithinBlockID(int blockID) {
        return worldObj.getBlockId((int) Math.floor(posX), (int) Math.floor(posY), (int) Math.floor(posZ)) == blockID;
    }

    private boolean hitGround() {
        return worldObj.getBlockId((int) Math.floor(posX), (int) (Math.floor(posY - 0.15F)), (int) Math.floor(posZ)) != 0;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {

    }

    @Override
    protected boolean canApplyFriction() {
        return false;
    }
}
