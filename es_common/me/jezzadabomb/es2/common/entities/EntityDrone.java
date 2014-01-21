package me.jezzadabomb.es2.common.entities;

import java.util.ArrayList;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

import cpw.mods.fml.common.registry.GameRegistry;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.CoordSetF;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.core.utils.TimeTracker;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class EntityDrone extends EntityES implements IEnergyHandler {

    // 1 - idle
    // 2 - working
    // 4 - moving
    int mode;
    boolean withinConstructor, reachedTarget, marked;
    TimeTracker timeTracker;
    CoordSetF targetSet;
    double xSpeed, ySpeed, zSpeed;
    private EnergyStorage storage = new EnergyStorage(10000);

    public EntityDrone(World world) {
        super(world);
        reachedTarget = withinConstructor = true;
        marked = false;
        setSize(2F / 16F, 2F / 16F);
        timeTracker = new TimeTracker();
        noClip = true;
        setIdle();
    }

    @Override
    protected void updateEntity() {
    }

    @Override
    protected void updateTick() {
        withinConstructor = isWithinBlockID(ModBlocks.atomicConstructor.blockID);

        if (storage.getEnergyStored() == 0)
            return;
        ESLogger.info("Moving");
        moveDrone();
        storage.modifyEnergyStored(-1);
    }

    public void moveDrone() {
        if (targetSet == null)
            return;
        if (!isMoving())
            setMoving();
        double xDisplace = targetSet.getX() - posX;
        double yDisplace = targetSet.getY() - posY;
        double zDisplace = targetSet.getZ() - posZ;
        boolean moved = false;
        motionX = motionY = motionZ = 0.0F;

        if (!MathHelper.withinRange(xDisplace, 0.0F, xSpeed)) {
            if (xDisplace < 0) {
                motionX = -xSpeed;
            } else if (xDisplace > 0) {
                motionX = xSpeed;
            }
            moved = true;
        }
        if (!MathHelper.withinRange(yDisplace, 0.0F, ySpeed)) {
            if (yDisplace < 0) {
                motionY = -ySpeed;
            } else if (yDisplace > 0) {
                motionY = ySpeed;
            }
            moved = true;
        }
        if (!MathHelper.withinRange(zDisplace, 0.0F, zSpeed)) {
            if (zDisplace < 0) {
                motionZ = -zSpeed;
            } else if (zDisplace > 0) {
                motionZ = zSpeed;
            }
            moved = true;
        }
        reachedTarget = !moved;
        if (reachedTarget) {
            targetSet = null;
            setIdle();
        }
    }

    public boolean hasReachedTarget() {
        return reachedTarget;
    }

    public CoordSetF getTargetCoords() {
        return targetSet;
    }

    public CoordSetF getCurrentBlock() {
        return new CoordSetF((float) posX, (float) posY, (float) posZ);
    }

    public void setTargetCoords(CoordSetF targetSet) {
        reachedTarget = false;
        this.targetSet = targetSet;
    }

    public void pathToNewConstructor(TileAtomicConstructor atomic) {
        pathToXYZ(atomic.xCoord, atomic.yCoord, atomic.zCoord);
    }

    public void pathToXYZ(int x, int y, int z) {
        setTargetCoords(new CoordSetF(x + 0.5F, y + 0.5F, z + 0.5F));
    }

    public boolean isWithinConstructor() {
        return withinConstructor;
    }

    public boolean isIdle() {
        return mode == 1;
    }

    public boolean isWorking() {
        return mode == 2;
    }

    public boolean isMoving() {
        return mode == 4;
    }

    public void setIdle() {
        setSpeed(0.0F);
        mode = 1;
    }

    public void setWorking() {
        setSpeed(0.03F);
        mode = 2;
    }

    public void setMoving() {
        setSpeed(0.008F);
        mode = 4;
    }

    private boolean isWithinBlockID(int blockID) {
        return worldObj.getBlockId((int) Math.floor(posX), (int) Math.floor(posY), (int) Math.floor(posZ)) == blockID;
    }

    private boolean hitGround() {
        return worldObj.getBlockId((int) Math.floor(posX), (int) (Math.floor(posY - (height / 2))), (int) Math.floor(posZ)) != 0;
    }

    public void setSpeed(float speed) {
        xSpeed = ySpeed = zSpeed = speed;
    }

    public void setSpeed(float xSpeed, float ySpeed, float zSpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
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

    @Override
    protected boolean canAddDataToWatcher() {
        return false;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        return storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public boolean canInterface(ForgeDirection from) {
        return true;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return storage.getMaxEnergyStored();
    }

}
