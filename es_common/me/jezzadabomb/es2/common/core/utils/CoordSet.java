package me.jezzadabomb.es2.common.core.utils;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.packets.InventoryPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class CoordSet {

    private int x, y, z;

    public CoordSet(EntityPlayer player) {
        this((int) Math.floor(player.posX), (int) Math.floor(player.posY), (int) Math.floor(player.posZ));
    }

    public CoordSet(int[] array) {
        x = array[0];
        y = array[1];
        z = array[2];
    }

    public CoordSet(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public CoordSet subtractCoords(CoordSet tempSet) {
        return new CoordSet(x - tempSet.getX(), y - tempSet.getY(), z - tempSet.getZ());
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setIntArray("coords", new int[] { x, y, z });
    }

    public static CoordSet readFromNBT(NBTTagCompound tag) {
        return new CoordSet(tag.getIntArray("coords"));
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

    public String toPacketString(){
        return x + ":" + y + ":" + z;
    }
    
    @Override
    public String toString() {
        return " @ " + toPacketString();
    }
}
