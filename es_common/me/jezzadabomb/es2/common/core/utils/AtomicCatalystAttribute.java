package me.jezzadabomb.es2.common.core.utils;

import net.minecraft.nbt.NBTTagCompound;

public class AtomicCatalystAttribute {

    int strength, speed, fortune;

    public AtomicCatalystAttribute(int strength, int speed, int fortune) {
        this.strength = strength;
        this.speed = speed;
        this.fortune = fortune;
    }

    public int getFortune() {
        return fortune;
    }

    public int getSpeed() {
        return speed;
    }

    public int getStrength() {
        return strength;
    }

    public void updateValues(int fortune, int speed, int strength) {
        this.fortune = fortune;
        this.speed = speed;
        this.strength = strength;
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger("ACstrength", strength);
        tag.setInteger("ACspeed", speed);
        tag.setInteger("ACfortune", fortune);
    }

    public static AtomicCatalystAttribute readFromNBT(NBTTagCompound tag) {
        int strength = tag.getInteger("ACstrength");
        int speed = tag.getInteger("ACspeed");
        int fortune = tag.getInteger("ACfortune");
        return new AtomicCatalystAttribute(strength, speed, fortune);
    }
}
