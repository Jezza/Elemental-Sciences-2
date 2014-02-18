package me.jezzadabomb.es2.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class EntityES extends Entity {

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

    protected void moveEntity() {
        setPosition(posX + motionX, posY + motionY, posZ + motionZ);
    }

    @Override
    protected void entityInit() {
        addDataWatchers();
    }

    protected abstract void addDataWatchers();

    protected abstract void updateTick();

    protected abstract void updateEntity();

}
