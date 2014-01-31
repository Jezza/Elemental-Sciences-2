package me.jezzadabomb.es2.common.core.events;

import java.util.BitSet;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.Event;

public class MobArmourEvent extends Event {

    EntityLiving entityLiving;
    BitSet updateChunk;

    public MobArmourEvent(EntityLiving entityLiving, BitSet updateChunk) {
        this.entityLiving = entityLiving;
        this.updateChunk = updateChunk;
    }
    
    public boolean isBootsChanged() {
        return updateChunk.get(0);
    }

    public boolean isLeggingsChanged() {
        return updateChunk.get(1);
    }

    public boolean isChestPlateChanged() {
        return updateChunk.get(2);
    }

    public boolean isHelmetChanged() {
        return updateChunk.get(3);
    }

    public ItemStack getBoots() {
        return entityLiving.getCurrentItemOrArmor(0);
    }

    public ItemStack getLeggings() {
        return entityLiving.getCurrentItemOrArmor(1);
    }

    public ItemStack getChestPlate() {
        return entityLiving.getCurrentItemOrArmor(2);
    }

    public ItemStack getHelmet() {
        return entityLiving.getCurrentItemOrArmor(3);
    }

}
