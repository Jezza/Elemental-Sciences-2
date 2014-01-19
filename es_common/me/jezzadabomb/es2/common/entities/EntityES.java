package me.jezzadabomb.es2.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class EntityES extends Entity {

    float friction = 0.9F;

    public EntityES(World par1World) {
        super(par1World);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        updateEntity();

        moveEntity();
    }

    protected void moveEntity() {
        setPosition(posX + motionX, posY + motionY, posZ + motionZ);
    }

    protected void setFriction(float friction) {
        this.friction = friction;
    }

    protected float getFriction() {
        return friction;
    }

    protected void applyFriction() {
        motionX *= friction;
        motionY *= friction;
        motionZ *= friction;
    }

    protected abstract void updateEntity();

    @Override
    protected abstract void entityInit();

    @Override
    protected abstract void readEntityFromNBT(NBTTagCompound nbttagcompound);

    @Override
    protected abstract void writeEntityToNBT(NBTTagCompound nbttagcompound);
}
