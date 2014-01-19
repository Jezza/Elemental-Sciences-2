package me.jezzadabomb.es2.common.core.utils;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.packets.InventoryPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class Vector3I {

    private int x, y, z;

    public Vector3I(EntityPlayer player) {
        this((int) Math.floor(player.posX), (int) Math.floor(player.posY), (int) Math.floor(player.posZ));
    }

    public Vector3I(int[] array) {
        x = array[0];
        y = array[1];
        z = array[2];
    }

    public Vector3I(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean isPacket(InventoryPacket p) {
        Vector3I tempSet = p.coordSet;
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

    public static Vector3I readFromStream(ByteArrayDataInput in) {
        int x = in.readInt();
        int y = in.readInt();
        int z = in.readInt();
        return new Vector3I(x, y, z);
    }

    public int distanceFrom(Vector3I tempSet) {
        return MathHelper.pythagoras(MathHelper.pythagoras(x - tempSet.x, y - tempSet.y), z - tempSet.z);
    }

    public boolean isAdjacent(Vector3I tempSet) {
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

    public Vector3I subtractCoords(Vector3I tempSet) {
        return new Vector3I(x - tempSet.getX(), y - tempSet.getY(), z - tempSet.getZ());
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setIntArray("coords", new int[] { x, y, z });
    }

    public static Vector3I readFromNBT(NBTTagCompound tag) {
        return new Vector3I(tag.getIntArray("coords"));
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
        if (!(other instanceof Vector3I))
            return false;
        Vector3I coordSet = (Vector3I) other;
        return (coordSet.x == x && coordSet.y == y && coordSet.z == z);
    }

    @Override
    public String toString() {
        return " @ X: " + x + ", Y: " + y + ", Z: " + z;
    }
}
