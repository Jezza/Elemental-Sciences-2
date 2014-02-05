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
import me.jezzadabomb.es2.common.tileentity.TileConsole;
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

public class EntityDrone extends EntityES {

    boolean withinConstructor, reachedTarget, pathed, moving, working;
    CoordSetF targetSet;
    double xSpeed, ySpeed, zSpeed;
    TileConsole console;

    public EntityDrone(World world) {
        super(world);
//        setSize(8F / 16F, 8F / 16F);
        noClip = true;
        setWorking(false);
        reachedTarget = withinConstructor = true;
        pathed = moving = working = false;
    }

    /**
     * Note that it will also register itself with it.
     * 
     * @param console
     */
    public EntityDrone setConsole(TileConsole console) {
        this.console = console;
        console.registerDrone(this);
        return this;
    }

    public TileConsole getConsole() {
        return console;
    }

    @Override
    protected void updateEntity() {
    }

    @Override
    protected void updateTick() {
        withinConstructor = isWithinBlockID(ModBlocks.atomicConstructor.blockID);

        if (console == null || console.isInvalid()) {
            if (withinConstructor) {
                TileAtomicConstructor atomic = getConstructor();
                if(atomic == null || !atomic.hasConsole())
                    return;
                setConsole(atomic.getConsole());
            }
            return;
        }

        if (!withinConstructor && !pathed)
            pathToNewConstructor(console.getRandomConstructor());

        moveDrone();
    }

    // Determines movement and motion.
    public void moveDrone() {
        if (targetSet == null)
            return;
        double xDisplace = targetSet.getX() - posX;
        double yDisplace = targetSet.getY() - posY;
        double zDisplace = targetSet.getZ() - posZ;
        motionX = motionY = motionZ = 0.0F;

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
        reachedTarget = !(motionX != 0.0F || motionY != 0.0F || motionZ != 0.0F);
        setMoving(!reachedTarget);
        if (reachedTarget)
            targetSet = null;
    }

    public CoordSetF getCurrentBlock() {
        return new CoordSetF((float) posX, (float) posY, (float) posZ);
    }

    public void setTargetCoords(CoordSetF targetSet) {
        reachedTarget = false;
        pathed = true;
        this.targetSet = targetSet;
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

    private boolean isWithinBlockID(int blockID) {
        return worldObj.getBlockId((int) Math.floor(posX), (int) Math.floor(posY), (int) Math.floor(posZ)) == blockID;
    }

    private TileAtomicConstructor getConstructor() {
        int x = (int) Math.floor(posX);
        int y = (int) Math.floor(posX);
        int z = (int) Math.floor(posX);
        if (isWithinConstructor())
            return (TileAtomicConstructor) worldObj.getBlockTileEntity(x, y, z);
        return null;
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
    protected void readEntityFromNBT(NBTTagCompound tag) {
        moving = tag.getBoolean("moving");
        working = tag.getBoolean("working");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
        tag.setBoolean("moving", moving);
        tag.setBoolean("working", working);
    }

    @Override
    protected boolean canApplyFriction() {
        return false;
    }

}
