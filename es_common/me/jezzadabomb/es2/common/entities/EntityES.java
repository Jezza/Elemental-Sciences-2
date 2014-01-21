package me.jezzadabomb.es2.common.entities;

import cofh.api.energy.IEnergyHandler;
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
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        updateTick();

        moveEntity();
    }

    protected abstract void updateTick();

    protected void moveEntity() {
        if (canApplyFriction())
            applyFriction();
        moveEntity(motionX, motionY, motionZ);
//         setPosition(posX + motionX, posY + motionY, posZ + motionZ);
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

    @Override
    protected void entityInit() {
        if (canAddDataToWatcher())
            addDataToWatcher();
    }

    protected void addDataToWatcher() {

    }

    protected abstract boolean canApplyFriction();

    protected abstract void updateEntity();

    protected abstract boolean canAddDataToWatcher();

}
