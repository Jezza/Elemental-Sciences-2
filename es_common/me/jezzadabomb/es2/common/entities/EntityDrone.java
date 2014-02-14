package me.jezzadabomb.es2.common.entities;

import io.netty.buffer.ByteBuf;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSetF;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityDrone extends EntityES implements IEntityAdditionalSpawnData {

    boolean withinConstructor, reachedTarget, pathed, moving, working;
    CoordSetF targetSet;
    double xSpeed, ySpeed, zSpeed;
    TileDroneBay droneBay;

    public EntityDrone(World world) {
        super(world);
        setSize(2F / 16F, 2F / 16F);
        noClip = true;
        setWorking(false);
        reachedTarget = withinConstructor = true;
        pathed = moving = working = false;
        setSpeed(0.1F);
    }

    public EntityDrone setDroneBay(TileDroneBay droneBay) {
        this.droneBay = droneBay;
        return this;
    }

    @Override
    protected void updateEntity() {
        if (!worldObj.isRemote)
            ESLogger.info("HALP");
        moveDrone();
    }

    @Override
    protected void updateTick() {
    }

    public void moveDrone() {
        if (targetSet == null) {
            setMoving(false);
            return;
        }
        double xDisplace = targetSet.getX() - posX;
        double yDisplace = targetSet.getY() - posY;
        double zDisplace = targetSet.getZ() - posZ;
        motionX = motionY = motionZ = 0.0F;

        if (!MathHelper.withinRange(xDisplace, 0.0F, xSpeed / 2)) {
            if (xDisplace < 0) {
                motionX = -xSpeed;
            } else if (xDisplace > 0) {
                motionX = xSpeed;
            }
        }
        if (!MathHelper.withinRange(yDisplace, 0.0F, ySpeed / 2)) {
            if (yDisplace < 0) {
                motionY = -ySpeed;
            } else if (yDisplace > 0) {
                motionY = ySpeed;
            }
        }
        if (!MathHelper.withinRange(zDisplace, 0.0F, zSpeed / 2)) {
            if (zDisplace < 0) {
                motionZ = -zSpeed;
            } else if (zDisplace > 0) {
                motionZ = zSpeed;
            }
        }
        reachedTarget = motionX == 0.0F && motionY == 0.0F && motionZ == 0.0F;
        if (reachedTarget) {
            setMoving(false);
            targetSet = null;
        }
    }

    public CoordSetF getCurrentBlock() {
        return new CoordSetF((float) posX, (float) posY, (float) posZ);
    }

    public void setTargetCoords(CoordSetF targetSet) {
        reachedTarget = false;
        pathed = true;
        this.targetSet = targetSet;
        setMoving(true);
    }

    public void pathToNewConstructor(TileAtomicConstructor atomic) {
        if (atomic == null)
            return;
        pathToXYZ(atomic.xCoord, atomic.yCoord, atomic.zCoord);
    }

    public void pathToXYZ(int x, int y, int z) {
        setTargetCoords(new CoordSetF(x + 0.5F, y + 0.5F, z + 0.5F));
    }

    public boolean hasReachedTarget() {
        return reachedTarget;
    }

    public CoordSetF getTargetCoords() {
        return targetSet;
    }

    public boolean isWithinConstructor() {
        return withinConstructor;
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

        boolean flag = targetSet != null;
        tag.setBoolean("hasTargetSet", flag);
        if (flag) {
            tag.setFloat("targetX", targetSet.getX());
            tag.setFloat("targetY", targetSet.getY());
            tag.setFloat("targetZ", targetSet.getZ());
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        moving = tag.getBoolean("moving");
        working = tag.getBoolean("working");

        boolean flag = tag.getBoolean("hasTargetSet");
        if (flag) {
            float x = tag.getFloat("targetX");
            float y = tag.getFloat("targetY");
            float z = tag.getFloat("targetZ");

            targetSet = new CoordSetF(x, y, z);
        } else {
            targetSet = null;
        }
    }

    @Override
    protected boolean canApplyFriction() {
        return false;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        int x = droneBay.xCoord;
        int y = droneBay.yCoord;
        int z = droneBay.zCoord;

        byte[] loc = UtilMethods.getLocFromXYZ(x, y, z).getBytes();
        buffer.writeInt(loc.length);
        buffer.writeBytes(loc);

        boolean flag = targetSet != null;
        buffer.writeBoolean(flag);
        if (flag)
            targetSet.writeToStream(buffer);

    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        // TODO Keep an eye on this.
        int length = additionalData.readInt();
        ByteBuf buf = additionalData.readBytes(length);
        String loc = additionalData.toString();
        ESLogger.info(loc);
        int[] coords = UtilMethods.getArrayFromString(loc);

        droneBay = (TileDroneBay) worldObj.getTileEntity(coords[0], coords[1], coords[2]);

        boolean flag = additionalData.readBoolean();
        if (flag)
            targetSet = CoordSetF.readFromStream(additionalData);

    }

}
