package me.jezzadabomb.es2.common.entities;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.ESLogger;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityDrone extends EntityES {

    int timer;
    boolean dropToGround;

    public EntityDrone(World world) {
        super(world);
        timer = 0;
        dropToGround = false;
        setSize(0.1F, 0.1F);
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void updateEntity() {
        applyFriction();
        if (dropToGround) {
            if (!worldObj.isRemote)
                if (dropToGround())
                    dropToGround = false;
        }

        dropToGround = !isWithinBlockID(ModBlocks.atomicConstructor.blockID);
    }

    public boolean dropToGround() {
        if (!hitGround()) {
            addVelocity(0.0F, -0.01F, 0.0F);
            if (motionY < 0)
                motionY *= 1.1F;
        } else {
            motionY = 0.0F;
        }
        moveEntity();
        return hitGround();
    }

    private boolean isWithinBlockID(int blockID) {
        return worldObj.getBlockId((int) Math.floor(posX), (int) Math.floor(posY), (int) Math.floor(posZ)) == blockID;
    }

    private boolean hitGround() {
        return worldObj.getBlockId((int) Math.floor(posX), (int) (Math.floor(posY - 0.15F)), (int) Math.floor(posZ)) != 0;
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return entity.boundingBox;
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {

    }

}
