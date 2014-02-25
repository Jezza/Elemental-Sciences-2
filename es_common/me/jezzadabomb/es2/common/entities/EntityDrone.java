package me.jezzadabomb.es2.common.entities;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Arrays;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSetF;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class EntityDrone extends EntityES implements IEntityAdditionalSpawnData {

    protected ArrayList<CoordSetF> targetQueue = new ArrayList<CoordSetF>();
    protected double xSpeed, ySpeed, zSpeed;
    protected boolean moving;

    public EntityDrone(World par1World) {
        super(par1World);

        setSize(0.15F, 0.15F);
        noClip = true;
    }

    @Override
    protected void updateTick() {
        preTick();

        droneTick();

        moveDrone();

        postTick();
    }

    public void moveDrone() {
        if (targetQueue.isEmpty()) {
            if (moving) {
                motionX = motionY = motionZ = 0.0F;
                moving = false;
            }
            return;
        }

        if (!moving)
            moving = true;

        CoordSetF targetSet = targetQueue.get(0);

        double xDisplace = targetSet.getX() - posX;
        double yDisplace = targetSet.getY() - posY;
        double zDisplace = targetSet.getZ() - posZ;

        if (!MathHelper.withinRange(xDisplace, xSpeed)) {
            if (xDisplace < 0) {
                motionX = -xSpeed;
            } else if (xDisplace > 0) {
                motionX = xSpeed;
            } else {
                motionX = 0.0F;
            }
        } else {
            motionX = 0.0F;
        }
        if (!MathHelper.withinRange(yDisplace, ySpeed)) {
            if (yDisplace < 0) {
                motionY = -ySpeed;
            } else if (yDisplace > 0) {
                motionY = ySpeed;
            } else {
                motionY = 0.0F;
            }
        } else {
            motionY = 0.0F;
        }
        if (!MathHelper.withinRange(zDisplace, zSpeed)) {
            if (zDisplace < 0) {
                motionZ = -zSpeed;
            } else if (zDisplace > 0) {
                motionZ = zSpeed;
            } else {
                motionZ = 0.0F;
            }
        } else {
            motionZ = 0.0F;
        }
        boolean reachedTarget = motionX == 0.0F && motionY == 0.0F && motionZ == 0.0F;
        if (reachedTarget) {
            targetQueue.remove(0);
            reachedTarget(targetQueue.isEmpty());
        }
    }

    public void replaceCoordSetQueue(CoordSetF... coordSets) {
        targetQueue.clear();
        targetQueue.addAll(Arrays.asList(coordSets));
    }

    public boolean addCoordSetFToQueue(CoordSetF coordSet, int pos) {
        if (coordSet == null)
            return false;

        targetQueue.add(pos, coordSet);
        return targetQueue.contains(coordSet);
    }

    public boolean addCoordSetFToHead(CoordSetF coordSet) {
        return addCoordSetFToQueue(coordSet, 0);
    }

    public boolean addCoordSetFToQueue(CoordSetF coordSet) {
        if (coordSet == null)
            return false;

        targetQueue.add(coordSet);
        return targetQueue.contains(coordSet);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        boolean flag = tag.getBoolean("hasTargetSet");
        if (flag)
            targetQueue = UtilMethods.readQueueFromNBT(tag);

        readDroneFromNBT(tag);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
        boolean flag = !targetQueue.isEmpty();
        tag.setBoolean("hasTargetSet", flag);
        if (flag)
            UtilMethods.writeQueueToNBT(targetQueue, tag);

        writeDroneToNBT(tag);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        boolean flag = additionalData.readBoolean();
        if (flag)
            targetQueue = UtilMethods.readQueueFromBuffer(additionalData);

        readDroneSpawnData(additionalData);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        boolean flag = !(targetQueue == null || targetQueue.isEmpty());
        buffer.writeBoolean(flag);
        if (flag)
            UtilMethods.writeQueueToBuffer(targetQueue, buffer);

        writeDroneSpawnData(buffer);
    }

    public boolean reachedFinalTarget() {
        return targetQueue.isEmpty();
    }

    public boolean isMoving() {
        return moving;
    }

    public void setSpeed(float speed) {
        xSpeed = ySpeed = zSpeed = speed;
    }

    public void setSpeed(float xSpeed, float ySpeed, float zSpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double par1) {
        double d1 = this.boundingBox.getAverageEdgeLength();
        d1 *= 64.0D * this.renderDistanceWeight;
        return par1 < 103 * d1;
    }

    public abstract void preTick();

    public abstract void droneTick();

    public abstract void postTick();

    public abstract void reachedTarget(boolean finalTarget);

    public abstract void readDroneSpawnData(ByteBuf additionalData);

    public abstract void writeDroneSpawnData(ByteBuf buffer);

    public abstract void readDroneFromNBT(NBTTagCompound tag);

    public abstract void writeDroneToNBT(NBTTagCompound tag);
}
