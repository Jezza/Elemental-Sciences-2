package me.jezzadabomb.es2.common.core.utils;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class TargetTracker {

    World world;
    int x, y, z;
    Entity entity;
    Class<? extends Entity> clazz;
    float minX, minY, minZ, maxX, maxY, maxZ;

    public TargetTracker(World world, int x, int y, int z, Class<? extends Entity> clazz) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.clazz = clazz;
        minX = minY = minZ = 0.0F;
        maxX = maxY = maxZ = 1.0F;
    }

    public TargetTracker setBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;

        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;

        return this;
    }

    public Entity getNewTarget() {
        List<Entity> entities = world.getEntitiesWithinAABB(clazz, AxisAlignedBB.getAABBPool().getAABB(x + minX, y + minY, z + minZ, x + maxX, y + maxY, z + maxZ));
        entity = entities.get(new Random().nextInt(entities.size()));
        return entity;
    }

    public boolean hasTarget() {
        return entity != null;
    }

    public Entity getTarget() {
        return entity;
    }

}
