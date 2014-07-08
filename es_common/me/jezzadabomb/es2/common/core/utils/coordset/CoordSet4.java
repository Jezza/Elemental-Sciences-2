package me.jezzadabomb.es2.common.core.utils.coordset;

import io.netty.buffer.ByteBuf;

public class CoordSet4 extends CoordSet {

    private int i;

    public CoordSet4(int[] array) {
        super(array);
        i = array[3];
    }

    public CoordSet4(CoordSet coordSet, int i) {
        this(coordSet.getX(), coordSet.getY(), coordSet.getZ(), i);
    }

    public CoordSet4(int x, int y, int z, int i) {
        super(x, y, z);
        this.i = i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getI() {
        return i;
    }

    public CoordSet4 addI(int i) {
        this.i += i;
        return this;
    }

    public void writeBytes(ByteBuf bytes) {
        super.writeBytes(bytes);
        bytes.writeInt(i);
    }

    public static CoordSet4 readBytes(ByteBuf bytes) {
        CoordSet coordSet = readBytes(bytes);
        int i = bytes.readInt();

        return new CoordSet4(coordSet, i);
    }

    public CoordSet toCoordSet() {
        return new CoordSet(getX(), getY(), getZ());
    }

    public boolean isAtXYZI(int x, int y, int z, int i) {
        return getX() == x && getY() == y && getZ() == z && this.i == i;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof CoordSet4))
            return false;
        CoordSet4 coordSet4 = (CoordSet4) other;
        return coordSet4.isAtXYZI(getX(), getY(), getZ(), i);
    }

    @Override
    public int hashCode() {
        int hash = getX();
        hash *= 31 + getY();
        hash *= 31 + getZ();
        hash *= 31 + i;
        return hash;
    }

    public String toPacketString() {
        String packetString = super.toPacketString();
        return packetString + ":" + i;
    }
}
