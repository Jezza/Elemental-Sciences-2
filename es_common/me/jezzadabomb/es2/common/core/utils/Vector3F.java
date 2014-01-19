package me.jezzadabomb.es2.common.core.utils;

import net.minecraft.nbt.NBTTagCompound;

public class Vector3F {

    private float x, y, z;

    public Vector3F(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void addX(float motionX) {
        x += motionX;
    }

    public void substractX(float motionX) {
        x -= motionX;
    }

    public void addY(float motionY) {
        y += motionY;
    }

    public void substractY(float motionY) {
        y -= motionY;
    }

    public void addZ(float motionZ) {
        z += motionZ;
    }

    public void substractZ(float motionZ) {
        z -= motionZ;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void writeToNBT(NBTTagCompound tag) {

    }

    public static Vector3F readFromNBT(NBTTagCompound tag) {
        return null;
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y:" + y + ", Z:" + z;
    }
}
