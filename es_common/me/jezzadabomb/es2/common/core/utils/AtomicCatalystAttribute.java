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

    public AtomicCatalystAttribute setFortune(int fortune) {
        this.fortune = fortune;
        return this;
    }

    public AtomicCatalystAttribute setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public AtomicCatalystAttribute setStrength(int strength) {
        this.strength = strength;
        return this;
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
