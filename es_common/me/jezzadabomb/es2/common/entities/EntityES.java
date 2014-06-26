package me.jezzadabomb.es2.common.entities;

import me.jezzadabomb.es2.common.core.utils.coordset.CoordSetD;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class EntityES extends Entity {

    public EntityES(World world) {
        super(world);
    }

    @Override
    public void onUpdate() {
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

    protected CoordSetD getCurrentBlock() {
        return new CoordSetD(posX, posY, posZ);
    }

    /**
     * Used to init the data watchers.
     */
    protected abstract void addDataWatchers();

    /**
     * Called every tick.
     */
    protected abstract void updateTick();
}
