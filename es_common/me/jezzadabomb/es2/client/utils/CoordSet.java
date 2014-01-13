package me.jezzadabomb.es2.client.utils;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.packets.InventoryPacket;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class CoordSet {

    private int x, y, z;

    public CoordSet(EntityPlayer player) {
        x = (int) Math.floor(player.posX);
        y = (int) Math.floor(player.posY);
        z = (int) Math.floor(player.posZ);
        ESLogger.info(this);
    }

    public CoordSet(double x, double y, double z) {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
    }

    public CoordSet(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CoordSet(float x, float y, float z) {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
    }

    public boolean isPacket(InventoryPacket p) {
        CoordSet tempSet = p.coordSet;
        return x == tempSet.getX() && y == tempSet.getY() && z == tempSet.getZ();
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

    public boolean isAtXYZ(int x, int y, int z) {
        return this.x == x && this.y == y && this.z == z;
    }

    public void writeToStream(ByteArrayDataOutput out) {
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(z);
    }

    public static CoordSet readFromStream(ByteArrayDataInput in) {
        int x = in.readInt();
        int y = in.readInt();
        int z = in.readInt();
        return new CoordSet(x, y, z);
    }

    public int distanceFrom(CoordSet tempSet) {
        return MathHelper.pythagoras(MathHelper.pythagoras(x - tempSet.x, y - tempSet.y), z - tempSet.z);
    }

    public boolean isAdjacent(CoordSet tempSet) {
        if (MathHelper.withinRange(x, tempSet.getX(), 1) && MathHelper.withinRange(y, tempSet.getY(), 1)) {
            return true;
        }
        if (MathHelper.withinRange(x, tempSet.getX(), 1) && MathHelper.withinRange(z, tempSet.getZ(), 1)) {
            return true;
        }
        if (MathHelper.withinRange(y, tempSet.getY(), 1) && MathHelper.withinRange(z, tempSet.getZ(), 1)) {
            return true;
        }
        return false;
    }

    /*
     * Truncated.
     */
    public int distanceFrom(int x, int y, int z) {
        return MathHelper.pythagoras(MathHelper.pythagoras(x, y), z);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (!(other instanceof CoordSet))
            return false;
        CoordSet coordSet = (CoordSet) other;
        return (coordSet.x == x && coordSet.y == y && coordSet.z == z);
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y + ", Z: " + z;
    }
}
