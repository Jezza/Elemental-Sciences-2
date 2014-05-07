package me.jezzadabomb.es2.common.core.utils;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Arrays;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSetD;
import me.jezzadabomb.es2.common.items.debug.ItemDebugTool;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.WavefrontObject;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class UtilMethods {

    public static void addChatMessage(EntityPlayer player, String string) {
        if (player == null)
            return;
        player.addChatMessage(new ChatComponentText(string));
    }

    public static int getTimeInTicks(int hours, int minutes, int seconds, int ticks) {
        int tempHours = hours * 60;
        minutes += tempHours;
        int tempMinutes = minutes * 60;
        seconds += tempMinutes;
        int tempSeconds = seconds * 20;
        ticks += tempSeconds;

        return ticks;
    }

    public static void listAllParts(IModelCustom customModel) {
        if (customModel instanceof WavefrontObject) {
            WavefrontObject waveFrontObject = (WavefrontObject) customModel;
            for (GroupObject object : waveFrontObject.groupObjects)
                ESLogger.info(object.name);
        }
    }

    public static boolean hasPressedShift() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public static boolean[] readBooleanArrayFromNBT(NBTTagCompound compound) {
        int length = compound.getInteger("length");
        boolean[] localArray = new boolean[length];
        Arrays.fill(localArray, true);
        for (int i = 0; i < length; i++) {
            localArray[i] = compound.getBoolean("booleanArray" + i);
        }
        return localArray;
    }

    public static void writeBooleanArrayToNBT(NBTTagCompound compound, boolean[] array) {
        if (array == null)
            return;
        compound.setInteger("length", array.length);
        for (int i = 0; i < array.length; i++) {
            compound.setBoolean("booleanArray" + i, array[i]);
        }
    }

    public static int getDebugString(String mode) {
        return ((ItemDebugTool) ModItems.debugItem).getDebugPos(mode);
    }

    public static int getSecondsFromTicks(int ticks) {
        return ticks / 20;
    }

    public static boolean isRenderType(Block block, int type) {
        return block != null && block.getRenderType() == type;
    }

    public static boolean isRenderType(World world, int x, int y, int z, int type) {
        Block block = world.getBlock(x, y, z);
        return block != null && isRenderType(block, type);
    }

    public static boolean isRenderType(IBlockAccess world, int x, int y, int z, int type) {
        Block block = world.getBlock(x, y, z);
        return block != null && isRenderType(block, type);
    }

    public static void destroyBlock(World world, int x, int y, int z, Block block, int meta) {
        world.setBlockToAir(x, y, z);
        if (world.isRemote)
            Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(x, y, z, block, meta);
    }

    public static void breakBlock(World world, int x, int y, int z, Block block, int meta, boolean drop) {
        if (drop) {
            ArrayList<ItemStack> drops = block.getDrops(world, x, y, z, meta, 0);
            for (ItemStack is : drops)
                world.spawnEntityInWorld(new EntityItem(world, x + 0.5D, y + 0.5D, z + 0.5D, is));
        }
        destroyBlock(world, x, y, z, block, meta);
    }

    public static String getLocFromXYZ(int x, int y, int z) {
        return x + ":" + y + ":" + z;
    }

    public static String getLocFromArray(CoordSet coordSet) {
        return coordSet.getX() + ":" + coordSet.getY() + ":" + coordSet.getZ();
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

    public static void writeQueueToNBT(ArrayList<CoordSetD> targetSetQueue, NBTTagCompound tag) {
        tag.setInteger("queueSize", targetSetQueue.size());

        int index = 0;
        for (CoordSetD coordSet : targetSetQueue) {
            tag.setDouble("coordSetX" + index, coordSet.getX());
            tag.setDouble("coordSetY" + index, coordSet.getY());
            tag.setDouble("coordSetZ" + index, coordSet.getZ());
            index++;
        }
    }

    public static ArrayList<CoordSetD> readQueueFromNBT(NBTTagCompound tag) {
        int length = tag.getInteger("queueSize");

        ArrayList<CoordSetD> coordSetQueue = new ArrayList<CoordSetD>(length);

        for (int i = 0; i < length; i++) {
            double x = tag.getDouble("coordSetX" + i);
            double y = tag.getDouble("coordSetY" + i);
            double z = tag.getDouble("coordSetZ" + i);

            coordSetQueue.add(new CoordSetD(x, y, z));
        }

        return coordSetQueue;
    }

    public static void writeQueueToBuffer(ArrayList<CoordSetD> targetSetQueue, ByteBuf buffer) {
        buffer.writeInt(targetSetQueue.size());

        for (CoordSetD coordSet : targetSetQueue) {
            buffer.writeDouble(coordSet.getX());
            buffer.writeDouble(coordSet.getY());
            buffer.writeDouble(coordSet.getZ());
        }
    }

    public static ArrayList<CoordSetD> readQueueFromBuffer(ByteBuf buffer) {
        int length = buffer.readInt();

        ArrayList<CoordSetD> coordSetQueue = new ArrayList<CoordSetD>(length);

        for (int i = 0; i < length; i++) {
            double x = buffer.readDouble();
            double y = buffer.readDouble();
            double z = buffer.readDouble();

            coordSetQueue.add(new CoordSetD(x, y, z));
        }

        return coordSetQueue;
    }

    public static boolean isEntityWithin(EntityLivingBase entity, CoordSet coordSet, double value) {
        return entity.getDistanceSq(coordSet.getX(), coordSet.getY(), coordSet.getZ()) < (value * value);
    }

    public static String getLocFromXYZ(CoordSet coordSet) {
        return getLocFromXYZ(coordSet.getX(), coordSet.getY(), coordSet.getZ());
    }
}