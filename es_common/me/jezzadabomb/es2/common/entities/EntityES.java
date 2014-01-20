package me.jezzadabomb.es2.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class EntityES extends Entity {

    float friction = 0.9F;

    public EntityES(World world) {
        super(world);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        updateEntity();

        moveEntity();
    }

    protected void moveEntity() {
        if (canApplyFriction())
            applyFriction();
        setPosition(posX + motionX, posY + motionY, posZ + motionZ);
    }

    protected void applyFriction() {
        motionX *= friction;
        motionY *= friction;
        motionZ *= friction;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    protected abstract boolean canApplyFriction();

    protected abstract void updateEntity();

}
