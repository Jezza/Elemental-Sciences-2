package me.jezzadabomb.es2.common.core.utils.coordset;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class CoordSetF {

    private float x, y, z;

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

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
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
        out.writeFloat(x);
        out.writeFloat(y);
        out.writeFloat(z);
    }

    public static CoordSetF readFromStream(ByteBuf in) {
        float x = in.readFloat();
        float y = in.readFloat();
        float z = in.readFloat();
        return new CoordSetF(x, y, z);
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setFloat("coordX", x);
        tag.setFloat("coordY", y);
        tag.setFloat("coordZ", z);
    }

    public static CoordSetF readFromNBT(NBTTagCompound tag) {
        float x = tag.getFloat("coordX");
        float y = tag.getFloat("coordY");
        float z = tag.getFloat("coordZ");
        return new CoordSetF(x, y, z);
    }

    public CoordSet toCoordSet() {
        return new CoordSet((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y:" + y + ", Z:" + z;
    }

    public double distanceToSq(CoordSetF otherSet) {
        float dX = x - otherSet.x;
        float dY = y - otherSet.y;
        float dZ = z - otherSet.z;

        return dX * dX + dY * dY + dZ * dZ;
    }
}
