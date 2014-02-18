package me.jezzadabomb.es2.common.core.utils;

import io.netty.buffer.ByteBuf;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import net.minecraft.nbt.NBTTagCompound;

public class CoordSetF {

    private double x, y, z;

    public CoordSetF(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public CoordSetF(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CoordSetF(CoordSet coordSet) {
        this.x = coordSet.getX();
        this.y = coordSet.getY();
        this.z = coordSet.getZ();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public CoordSetF addX(float displaceX) {
        x += displaceX;
        return this;
    }

    public CoordSetF subtractX(float displaceX) {
        x -= displaceX;
        return this;
    }

    public CoordSetF addY(float displaceY) {
        y += displaceY;
        return this;
    }

    public CoordSetF subtractY(float displaceY) {
        y -= displaceY;
        return this;
    }

    public CoordSetF addZ(float displaceZ) {
        z += displaceZ;
        return this;
    }

    public CoordSetF subtractZ(float displaceZ) {
        z -= displaceZ;
        return this;
    }

    public CoordSetF addXYZ(float x, float y, float z) {
        return addX(x).addY(y).addZ(z);
    }

    public CoordSetF subtractXYZ(float x, float y, float z) {
        return subtractX(x).subtractY(y).subtractZ(z);
    }

    public CoordSetF setX(float x) {
        this.x = x;
        return this;
    }

    public CoordSetF setY(float y) {
        this.y = y;
        return this;
    }

    public CoordSetF setZ(float z) {
        this.z = z;
        return this;
    }

    public void writeToStream(ByteBuf out) {
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
    }

    public static CoordSetF readFromStream(ByteBuf in) {
        double x = in.readDouble();
        double y = in.readDouble();
        double z = in.readDouble();
        return new CoordSetF(x, y, z);
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setDouble("coordX", x);
        tag.setDouble("coordY", y);
        tag.setDouble("coordZ", z);
    }

    public static CoordSetF readFromNBT(NBTTagCompound tag) {
        double x = tag.getDouble("coordX");
        double y = tag.getDouble("coordY");
        double z = tag.getDouble("coordZ");
        return new CoordSetF(x, y, z);
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y:" + y + ", Z:" + z;
    }
}
