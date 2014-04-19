package me.jezzadabomb.es2.common.entities;

import io.netty.buffer.ByteBuf;
import me.jezzadabomb.es2.common.core.interfaces.IMasterable;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityConstructorDrone extends EntityDrone implements IMasterable {

    boolean registered;
    TileDroneBay droneBay;
    int[] droneBayCoords;

    public EntityConstructorDrone(World world) {
        super(world);

        registered = false;

        setSpeed(0.07F);
    }

    @Override
    public void setMaster(TileES object) {
        if (object instanceof TileDroneBay)
            droneBay = (TileDroneBay) object;
    }

    @Override
    public TileES getMaster() {
        return droneBay;
    }

    @Override
    public boolean hasMaster() {
        return droneBay != null;
    }

    @Override
    public boolean preTick() {
        return false;
    }

    @Override
    public void droneTick() {
        if (worldObj != null && droneBayCoords != null && droneBay == null) {
            droneBay = (TileDroneBay) worldObj.getTileEntity(droneBayCoords[0], droneBayCoords[1], droneBayCoords[2]);
            droneBayCoords = null;
        }
    }

    @Override
    public void postTick() {

    }

    @Override
    public void reachedTarget(boolean finalTarget) {
        if (!registered && droneBay != null)
            registered = droneBay.processSpawnedDrone(this);
    }

    @Override
    public void readDroneSpawnData(ByteBuf additionalData) {

    }

    @Override
    public void writeDroneSpawnData(ByteBuf buffer) {

    }

    @Override
    public void readDroneFromNBT(NBTTagCompound tag) {

    }

    @Override
    public void writeDroneToNBT(NBTTagCompound tag) {

    }

    @Override
    protected void addDataWatchers() {

    }

    @Override
    public boolean preWorldProcessing() {
        return false;
    }
}
