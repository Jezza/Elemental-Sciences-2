package me.jezzadabomb.es2.common.core.utils;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;

public class CoordSetD {
    private double x, y, z;

    public CoordSetD(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CoordSetD(CoordSet coordSet) {
        this.x = coordSet.getX();
        this.y = coordSet.getY();
        this.z = coordSet.getZ();
    }

    public CoordSetD(EntityPlayer player) {
        this.x = player.posX;
        this.y = player.posY;
        this.z = player.posZ;
    }

    public CoordSetD(Vec3 vec3) {
        x = vec3.xCoord;
        y = vec3.yCoord;
        z = vec3.zCoord;
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

    public CoordSetD addX(double displaceX) {
        x += displaceX;
        return this;
    }

    public CoordSetD subtractX(double displaceX) {
        x -= displaceX;
        return this;
    }

    public CoordSetD addY(double displaceY) {
        y += displaceY;
        return this;
    }

    public CoordSetD subtractY(double displaceY) {
        y -= displaceY;
        return this;
    }

    public CoordSetD addZ(double displaceZ) {
        z += displaceZ;
        return this;
    }

    public CoordSetD subtractZ(double displaceZ) {
        z -= displaceZ;
        return this;
    }

    public CoordSetD addXYZ(double x, double y, double z) {
        return addX(x).addY(y).addZ(z);
    }

    public CoordSetD subtractXYZ(double x, double y, double z) {
        return subtractX(x).subtractY(y).subtractZ(z);
    }

    public CoordSetD setX(double x) {
        this.x = x;
        return this;
    }

    public CoordSetD setY(double y) {
        this.y = y;
        return this;
    }

    public CoordSetD setZ(double z) {
        this.z = z;
        return this;
    }

    public void writeToStream(ByteBuf out) {
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
    }

    public static CoordSetD readFromStream(ByteBuf in) {
        double x = in.readDouble();
        double y = in.readDouble();
        double z = in.readDouble();
        return new CoordSetD(x, y, z);
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setDouble("coordX", x);
        tag.setDouble("coordY", y);
        tag.setDouble("coordZ", z);
    }

    public static CoordSetD readFromNBT(NBTTagCompound tag) {
        double x = tag.getDouble("coordX");
        double y = tag.getDouble("coordY");
        double z = tag.getDouble("coordZ");
        return new CoordSetD(x, y, z);
    }

    public CoordSet toCoordSet() {
        return new CoordSet((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y:" + y + ", Z:" + z;
    }

    public double distanceTo(CoordSetD otherSet) {
        double dX = otherSet.x - x;
        double dY = otherSet.y - y;
        double dZ = otherSet.z - z;

        return MathHelper.pythagoras(MathHelper.pythagoras(dX, dY), dZ);
    }

    public double distanceSqTo(CoordSetD coordSetD) {
        double x = this.x - coordSetD.x;
        double y = this.y - coordSetD.y;
        double z = this.z - coordSetD.z;
        return x * x + y * y + z * z;
    }

    public void merge(CoordSetD other) {
        x += other.x;
        y += other.y;
        z += other.z;
    }
}
