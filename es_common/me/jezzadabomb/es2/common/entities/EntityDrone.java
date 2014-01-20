package me.jezzadabomb.es2.common.entities;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.CoordSetF;
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
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityDrone extends EntityES {

    // 1 - idle
    // 2 - working
    // 3 - pathing to new
    // 4 - cooling
    int mode;
    boolean withinConstructor, reachedTarget, marked;
    TimeTracker timeTracker;
    CoordSetF targetSet;

    public EntityDrone(World world) {
        super(world);
        withinConstructor = true;
        reachedTarget = marked = false;
        setSize(2F / 16F, 2F / 16F);
        timeTracker = new TimeTracker();
        setIdle();
    }

    @Override
    protected void updateEntity() {
        withinConstructor = isWithinBlockID(ModBlocks.atomicConstructor.blockID);
        if (isWorking() && !withinConstructor)
            setIdle();
        moveDrone();
        if (isCooling()) {
            if (!marked) {
                timeTracker.markTime(worldObj);
                marked = true;
            }
            if (timeTracker.hasDelayPassed(worldObj, 450)) {
                setIdle();
                marked = false;
            }
        }
    }

    public void moveDrone() {
        if (targetSet != null) {
            moveTo(targetSet.getX(), targetSet.getY(), targetSet.getZ());
        }
    }

    public void moveTo(double xCoord, double yCoord, double zCoord) {
        float xDisplace = (float) (xCoord - posX);
        float yDisplace = (float) (yCoord - posY);
        float zDisplace = (float) (zCoord - posZ);
        float xSpeed = 0.1F;
        float ySpeed = 0.1F;
        float zSpeed = 0.1F;
        boolean moved = false;
        motionX = motionY = motionZ = 0.0F;

        if (!me.jezzadabomb.es2.common.core.utils.MathHelper.withinRange(xDisplace, 0.0F, xSpeed)) {
            if (xDisplace < 0) {
                motionX = -xSpeed;
            } else if (xDisplace > 0) {
                motionX = xSpeed;
            }
            moved = true;
        }
        if (!me.jezzadabomb.es2.common.core.utils.MathHelper.withinRange(yDisplace, 0.0F, ySpeed)) {
            if (yDisplace < 0) {
                motionY = -ySpeed;
            } else if (yDisplace > 0) {
                motionY = ySpeed;
            }
            moved = true;
        }
        if (!me.jezzadabomb.es2.common.core.utils.MathHelper.withinRange(zDisplace, 0.0F, zSpeed)) {
            if (zDisplace < 0) {
                motionZ = -zSpeed;
            } else if (zDisplace > 0) {
                motionZ = zSpeed;
            }
            moved = true;
        }
        if (!moved)
            setIdle();
        reachedTarget = !moved;
    }

    public boolean getReachedTarget() {
        return reachedTarget;
    }

    public void moveTo(int x, int y, int z) {
        moveTo((double) x + 0.5F, (double) y + 0.5F, (double) z + 0.5F);
    }

    public void pathToNewConstructor(TileAtomicConstructor atomic) {
        pathToXYZ(atomic.xCoord, atomic.yCoord, atomic.zCoord);
    }

    public void pathToXYZ(int x, int y, int z) {
        targetSet = new CoordSetF(x + 0.5F, y + 0.5F, z + 0.5F);
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

    public boolean isPathing() {
        return mode == 3;
    }

    public boolean isCooling() {
        return mode == 4;
    }

    public void setIdle() {
        mode = 1;
    }

    public void setWorking() {
        mode = 2;
    }

    public void setPathing() {
        mode = 3;
    }

    public void setCooling() {
        mode = 4;
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

    @Override
    protected boolean canAddDataToWatcher() {
        return false;
    }
}
