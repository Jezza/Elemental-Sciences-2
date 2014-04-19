package me.jezzadabomb.es2.common.core.utils;

import me.jezzadabomb.es2.common.core.interfaces.IDismantleable;
import me.jezzadabomb.es2.common.core.interfaces.IMasterable;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import me.jezzadabomb.es2.common.tileentity.multi.TileAtomicShredderCore;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Identifier {

    public static boolean hasTileEntity(IBlockAccess world, int x, int y, int z) {
        if (world == null)
            return false;
        return world.getTileEntity(x, y, z) != null;
    }

    public static boolean isConsole(IBlockAccess world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && tileEntity instanceof TileConsole;
    }

    public static boolean isConstructor(IBlockAccess world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && tileEntity instanceof TileAtomicConstructor;
    }

    public static boolean isScanner(IBlockAccess world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && tileEntity instanceof TileInventoryScanner;
    }

    public static boolean isDismantable(IBlockAccess world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && tileEntity instanceof IDismantleable;
    }

    public static boolean isIInventory(IBlockAccess world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && (tileEntity instanceof IInventory || tileEntity instanceof ISidedInventory);
    }

    public static boolean isDroneBay(IBlockAccess world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && tileEntity instanceof TileDroneBay;
    }

    public static boolean isAtomicShredderCore(IBlockAccess world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && tileEntity instanceof TileAtomicShredderCore;
    }

    public static boolean isMasterable(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && tileEntity instanceof IMasterable;
    }
}
