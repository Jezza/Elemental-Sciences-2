package me.jezzadabomb.es2.common.core.utils.coordset;

import io.netty.buffer.ByteBuf;
import me.jezzadabomb.es2.common.core.interfaces.IMasterable;
import me.jezzadabomb.es2.common.core.interfaces.IPylon;
import me.jezzadabomb.es2.common.core.interfaces.IPylonReceiver;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
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

    public CoordSet addX(int x) {
        this.x += x;
        return this;
    }

    public CoordSet addY(int y) {
        this.y += y;
        return this;
    }

    public CoordSet addZ(int z) {
        this.z += z;
        return this;
    }

    public boolean isAtXYZ(int x, int y, int z) {
        return this.x == x && this.y == y && this.z == z;
    }

    public boolean withinRange(CoordSet tempSet, int range) {
        return getDistanceSq(tempSet) <= (range * range);
    }

    public double getDistanceSq(CoordSet tempSet) {
        double x2 = x - tempSet.x;
        double y2 = y - tempSet.y;
        double z2 = z - tempSet.z;
        return x2 * x2 + y2 * y2 + z2 * z2;
    }

    public float getDistance(CoordSet tempSet) {
        return net.minecraft.util.MathHelper.sqrt_double(getDistanceSq(tempSet));
    }

    public CoordSet addForgeDirection(ForgeDirection direction) {
        x += direction.offsetX;
        y += direction.offsetY;
        z += direction.offsetZ;
        return this;
    }

    public boolean isAdjacent(CoordSet coordSet) {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            CoordSet tempSet = coordSet.copy().addForgeDirection(direction);
            if (tempSet.isAtXYZ(x, y, z))
                return true;
        }
        return false;
    }

    public Block getBlock(IBlockAccess world) {
        return world.getBlock(x, y, z);
    }

    public TileEntity getTileEntity(IBlockAccess world) {
        return world.getTileEntity(x, y, z);
    }

    public boolean hasTileEntity(IBlockAccess world) {
        return getTileEntity(world) != null;
    }

    public boolean setBlockToAir(World world) {
        return world.setBlockToAir(x, y, z);
    }

    public boolean isAirBlock(World world) {
        return world.isAirBlock(x, y, z);
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setIntArray("coordSet", asArray());
    }

    public static CoordSet readFromNBT(NBTTagCompound tag) {
        return new CoordSet(tag.getIntArray("coordSet"));
    }

    public static CoordSet getArrayFromString(String loc) {
        CoordSet coordSet = new CoordSet(0, 0, 0);
        if (!loc.matches("-?\\d*:-?\\d*:-?\\d*"))
            return coordSet;
        String[] strs = loc.split(":");

        try {
            coordSet.setX(Integer.parseInt(strs[0]));
            coordSet.setY(Integer.parseInt(strs[1]));
            coordSet.setZ(Integer.parseInt(strs[2]));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return coordSet;
        }

        return coordSet;
    }

    public void writeBytes(ByteBuf bytes) {
        bytes.writeInt(x);
        bytes.writeInt(y);
        bytes.writeInt(z);
    }

    public static CoordSet readBytes(ByteBuf bytes) {
        int x = bytes.readInt();
        int y = bytes.readInt();
        int z = bytes.readInt();

        return new CoordSet(x, y, z);
    }

    public int[] asArray() {
        return new int[] { x, y, z };
    }

    public CoordSetF toCoordSetF() {
        return new CoordSetF(x + 0.5F, y + 0.5F, z + 0.5F);
    }

    public CoordSetD toCoordSetD() {
        return new CoordSetD(x + 0.5D, y + 0.5D, z + 0.5D);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof CoordSet))
            return false;
        return ((CoordSet) other).isAtXYZ(x, y, z);
    }

    @Override
    public int hashCode() {
        int hash = this.x;
        hash *= 31 + this.y;
        hash *= 31 + this.z;
        return hash;
    }

    public String toPacketString() {
        return x + ":" + y + ":" + z;
    }

    @Override
    public String toString() {
        return " @ " + toPacketString();
    }

    public CoordSet copy() {
        return new CoordSet(x, y, z);
    }

    public static CoordSet createFromMinecraftTag(NBTTagCompound tag) {
        int x = tag.getInteger("x");
        int y = tag.getInteger("y");
        int z = tag.getInteger("z");
        return new CoordSet(x, y, z);
    }

    public boolean isConsole(IBlockAccess world) {
        return getTileEntity(world) instanceof TileConsole;
    }

    public boolean isConstructor(IBlockAccess world) {
        return getTileEntity(world) instanceof TileAtomicConstructor;
    }

    public boolean isScanner(IBlockAccess world) {
        return getTileEntity(world) instanceof TileInventoryScanner;
    }

    public boolean isIInventory(IBlockAccess world) {
        return getTileEntity(world) instanceof IInventory;
    }

    public boolean isDroneBay(IBlockAccess world) {
        return getTileEntity(world) instanceof TileDroneBay;
    }

    public boolean isMasterable(World world) {
        return getTileEntity(world) instanceof IMasterable;
    }

    public boolean isPylon(World world) {
        return getTileEntity(world) instanceof IPylon;
    }

    public boolean isPylonReciever(World world) {
        return getTileEntity(world) instanceof IPylonReceiver;
    }
}
