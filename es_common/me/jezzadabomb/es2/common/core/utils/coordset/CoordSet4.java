package me.jezzadabomb.es2.common.core.utils.coordset;

import io.netty.buffer.ByteBuf;
import me.jezzadabomb.es2.common.core.utils.helpers.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class CoordSet4 {

    private int x, y, z, i;

    public CoordSet4(int[] array) {
        x = array[0];
        y = array[1];
        z = array[2];
        i = array[3];
    }

    public CoordSet4(CoordSet coordSet, int i) {
        this.x = coordSet.getX();
        this.y = coordSet.getY();
        this.z = coordSet.getZ();
        this.i = i;
    }

    public CoordSet4(int x, int y, int z, int i) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.i = i;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getI() {
        return i;
    }

    public CoordSet4 addX(int x) {
        this.x += x;
        return this;
    }

    public CoordSet4 addY(int y) {
        this.y += y;
        return this;
    }

    public CoordSet4 addZ(int z) {
        this.z += z;
        return this;
    }

    public CoordSet4 addI(int i) {
        this.i += i;
        return this;
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setIntArray("coordSet4", new int[] { x, y, z, i });
    }

    public static CoordSet readFromNBT(NBTTagCompound tag) {
        return new CoordSet(tag.getIntArray("coordSet4"));
    }

    public void writeBytes(ByteBuf bytes) {
        bytes.writeInt(x);
        bytes.writeInt(y);
        bytes.writeInt(z);
        bytes.writeInt(i);
    }

    public static CoordSet4 readBytes(ByteBuf bytes) {
        int x = bytes.readInt();
        int y = bytes.readInt();
        int z = bytes.readInt();
        int i = bytes.readInt();

        return new CoordSet4(x, y, z, i);
    }

    public CoordSet toCoordSet() {
        return new CoordSet(x, y, z);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof CoordSet4))
            return false;
        CoordSet4 coordSet4 = (CoordSet4) other;
        return (coordSet4.x == x && coordSet4.y == y && coordSet4.z == z && coordSet4.i == i);
    }

    @Override
    public int hashCode() {
        int hash = x;
        hash *= 31 + y;
        hash *= 31 + z;
        hash *= 31 + i;
        return hash;
    }
    
    public String toPacketString() {
        return x + ":" + y + ":" + z + ":" + i;
    }

    @Override
    public String toString() {
        return " @ " + toPacketString();
    }
}
