package me.jezzadabomb.es2.common.core.utils;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class TargetTracker {

    World world;
    int x,y,z;
    Entity entity;
    Class<? extends Entity> clazz;
    
    public TargetTracker(World world, int x, int y, int z, Class<? extends Entity> clazz) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.clazz = clazz;
    }
    
    public Entity getNewTarget(){
        List<Entity> entities = world.getEntitiesWithinAABB(clazz, AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1.0F, y + 1.0F, z + 1.0F));
        entity = entities.get(new Random().nextInt(entities.size()));
        return entity;
    }
    
    public boolean hasTarget(){
        return entity != null;
    }
    
    public Entity getTarget(){
        return entity;
    }

}
