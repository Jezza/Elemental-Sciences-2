package me.jezzadabomb.es2.common.entities;

import io.netty.buffer.ByteBuf;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.PacketUtils;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.CoordSetF;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.interfaces.IMasterable;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.TileES;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityDrone extends EntityES implements IEntityAdditionalSpawnData, IMasterable {

    boolean reachedTarget, reachedFinalTarget, moving, working;
    // CoordSetF targetSet;
    LinkedBlockingQueue<CoordSetF> targetSetQueue;
    double xSpeed, ySpeed, zSpeed;
    TileDroneBay droneBay;
    int[] droneBayCoords;

    public EntityDrone(World world) {
        super(world);
        setSize(8F / 16F, 8F / 16F);
        noClip = true;

        targetSetQueue = new LinkedBlockingQueue<CoordSetF>();
        setWorking(false);
        reachedTarget = reachedFinalTarget = true;
        moving = working = false;
        setSpeed(0.05F);
    }

    @Override
    protected void updateEntity() {
    }

    @Override
    protected void updateTick() {
        if (worldObj != null && droneBayCoords != null && droneBay == null) {
            droneBay = (TileDroneBay) worldObj.getTileEntity(droneBayCoords[0], droneBayCoords[1], droneBayCoords[2]);
            droneBayCoords = null;
        }

        moveDrone();
    }

    public void moveDrone() {
        motionX = motionY = motionZ = 0.0F;
        if (targetSetQueue.isEmpty()) {
            if (isMoving())
                setMoving(false);
            return;
        }

        CoordSetF targetSet = targetSetQueue.element();

        double xDisplace = targetSet.getX() - posX;
        double yDisplace = targetSet.getY() - posY;
        double zDisplace = targetSet.getZ() - posZ;

        if (!MathHelper.withinRange(xDisplace, 0.0F, xSpeed)) {
            if (xDisplace < 0) {
                motionX = -xSpeed;
            } else if (xDisplace > 0) {
                motionX = xSpeed;
            }
        }
        if (!MathHelper.withinRange(yDisplace, 0.0F, ySpeed)) {
            if (yDisplace < 0) {
                motionY = -ySpeed;
            } else if (yDisplace > 0) {
                motionY = ySpeed;
            }
        }
        if (!MathHelper.withinRange(zDisplace, 0.0F, zSpeed)) {
            if (zDisplace < 0) {
                motionZ = -zSpeed;
            } else if (zDisplace > 0) {
                motionZ = zSpeed;
            }
        }
        reachedTarget = motionX == 0.0F && motionY == 0.0F && motionZ == 0.0F;
        if (reachedTarget)
            targetSetQueue.remove();
    }

    public CoordSetF getCurrentPos() {
        return new CoordSetF((float) posX, (float) posY, (float) posZ);
    }

    public void addTargetCoords(CoordSetF targetSet) {
        reachedFinalTarget = false;
        try {
            targetSetQueue.put(targetSet);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setMoving(true);
    }

    public void addTargetCoords(CoordSet coordSet) {
        if (coordSet == null)
            return;
        addTargetCoords(coordSet.toCoordSetF());
    }

    public void pathToXYZ(int x, int y, int z) {
        addTargetCoords(new CoordSetF(x + 0.5F, y + 0.5F, z + 0.5F));
    }

    public boolean hasReachedTarget() {
        return reachedTarget;
    }

    public LinkedBlockingQueue<CoordSetF> getTargetCoords() {
        return targetSetQueue;
    }

    public boolean isWorking() {
        return working;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
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
    protected void addDataWatchers() {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
        tag.setBoolean("moving", moving);
        tag.setBoolean("working", working);

        boolean flag = droneBay != null;
        tag.setBoolean("hasDroneBay", flag);
        if (flag)
            tag.setString("droneLoc", UtilMethods.getLocFromXYZ(droneBay.xCoord, droneBay.yCoord, droneBay.zCoord));

        flag = !targetSetQueue.isEmpty();
        tag.setBoolean("hasTargetSet", flag);
        if (flag) {
            UtilMethods.writeQueueToNBT(targetSetQueue, tag);
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        moving = tag.getBoolean("moving");
        working = tag.getBoolean("working");

        boolean flag = tag.getBoolean("hasDroneBay");
        flag = false;
        droneBay = null;
        if (flag)
            droneBayCoords = UtilMethods.getArrayFromString(tag.getString("droneLoc"));

        flag = tag.getBoolean("hasTargetSet");
        if (flag)
            targetSetQueue = UtilMethods.readQueueFromNBT(tag);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        boolean flag = droneBay != null;

        buffer.writeBoolean(flag);

        if (flag) {
            int x = droneBay.xCoord;
            int y = droneBay.yCoord;
            int z = droneBay.zCoord;

            PacketUtils.writeStringByteBuffer(buffer, UtilMethods.getLocFromXYZ(x, y, z));
        }

        flag = !(targetSetQueue == null || targetSetQueue.isEmpty());
        buffer.writeBoolean(flag);
        if (flag)
            UtilMethods.writeQueueToBuffer(targetSetQueue, buffer);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        boolean flag = additionalData.readBoolean();

        if (flag) {
            String loc = PacketUtils.readStringByteBuffer(additionalData);
            int[] coords = UtilMethods.getArrayFromString(loc);

            droneBay = (TileDroneBay) worldObj.getTileEntity(coords[0], coords[1], coords[2]);
        }

        flag = additionalData.readBoolean();
        if (flag)
            targetSetQueue = UtilMethods.readQueueFromBuffer(additionalData);
    }

    @Override
    public void setMaster(TileES tileES) {
        if (tileES instanceof TileDroneBay)
            droneBay = (TileDroneBay) tileES;
    }

    @Override
    public TileES getMaster() {
        return droneBay;
    }

    @Override
    public boolean hasMaster() {
        return droneBay != null;
    }

}
