package me.jezzadabomb.es2.common.core.utils;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.interfaces.IDismantleable;
import me.jezzadabomb.es2.common.items.ItemDebugTool;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class UtilMethods {

    public static void addChatMessage(EntityPlayer player, String string) {
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

    public static ItemStack mergeItemStacks(ItemStack itemStack1, ItemStack itemStack2, boolean overflow) {
        if ((itemStack1 == null && itemStack2 == null) || !ItemStack.areItemStacksEqual(itemStack1, itemStack2))
            return null;
        itemStack1.stackSize += itemStack2.stackSize;
        itemStack2.stackSize = 0;
        if (!overflow && itemStack1.stackSize > 64)
            itemStack1.stackSize = 64;
        return itemStack1;
    }

    public static boolean isPlayerWearing(EntityPlayer player, Item item) {
        if (player == null || item == null || !(item instanceof ItemArmor))
            return false;
        int index = getSlotFromIndex(((ItemArmor) item).armorType);
        if (index == -1)
            return false;
        ItemStack tempStack = player.inventory.armorItemInSlot(index);
        if (tempStack != null)
            return item.equals(tempStack.getItem());

        return false;
    }

    public static boolean isPlayerWearing(EntityPlayer player, ItemStack itemStack) {
        return isPlayerWearing(player, itemStack.getItem());
    }

    @SideOnly(Side.CLIENT)
    public static boolean isWearingItem(Item item) {
        return isPlayerWearing(Minecraft.getMinecraft().thePlayer, item);
    }

    @SideOnly(Side.CLIENT)
    public static boolean isWearingItemStack(ItemStack itemStack) {
        return isWearingItem(itemStack.getItem());
    }

    public static ItemStack decrCurrentItem(EntityPlayer player) {
        return player.inventory.decrStackSize(player.inventory.currentItem, 1);
    }

    public static boolean isConsole(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && tileEntity instanceof TileConsole;
    }

    public static boolean isConstructor(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && tileEntity instanceof TileAtomicConstructor;
    }

    public static boolean isScanner(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && tileEntity instanceof TileInventoryScanner;
    }

    public static boolean isDismantable(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && tileEntity instanceof IDismantleable;
    }

    public static boolean isIInventory(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && (tileEntity instanceof IInventory || tileEntity instanceof ISidedInventory);
    }

    public static boolean isDroneBay(World world, int x, int y, int z) {
        if (world == null)
            return false;
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && tileEntity instanceof TileDroneBay;
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

    public static boolean canShowDebugHUD() {
        if (!Reference.isDebugMode)
            return false;
        return isHoldingItem(ModItems.debugItem);
    }

    public static boolean isRenderType(TileEntity tileEntity, int type) {
        return tileEntity != null && tileEntity.getBlockType().getRenderType() == type;
    }

    public static boolean isRenderType(World world, int x, int y, int z, int type) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && isRenderType(tileEntity, type);
    }

    public static boolean isRenderType(IBlockAccess world, int x, int y, int z, int type) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        return tileEntity != null && isRenderType(tileEntity, type);
    }

    public static boolean hasItemInInventory(EntityPlayer player, ItemStack itemStack, boolean shouldConsume, ItemStack replaceStack) {
        if (shouldConsume)
            ESLogger.info("Hitting");
        int index = 0;
        for (ItemStack tempStack : player.inventory.mainInventory) {
            if (tempStack != null && ItemStack.areItemStacksEqual(itemStack, tempStack)) {
                if (shouldConsume) {
                    player.inventory.mainInventory[index] = replaceStack;
                }
                return true;
            }
            index++;
        }
        return false;
    }

    public static boolean areItemStacksEqual(ItemStack itemStack1, ItemStack itemStack2) {
        return itemStack1 != null && itemStack2 != null && itemStack1.getItemDamage() == itemStack2.getItemDamage() && itemStack1.getItem().equals(itemStack2.getItem());
    }

    @SideOnly(Side.CLIENT)
    public static boolean canFlood() {
        if (canShowDebugHUD()) {
            ItemStack tempStack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
            return ((ItemDebugTool) tempStack.getItem()).canFlood;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public static boolean correctMode(int mode) {
        if (canShowDebugHUD()) {
            ItemStack tempStack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
            return ((ItemDebugTool) tempStack.getItem()).debugMode == mode;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isHoldingItem(Item item) {
        return isHoldingItemStack(new ItemStack(item));
    }

    @SideOnly(Side.CLIENT)
    public static boolean isHoldingItemStack(ItemStack itemStack) {
        if (Minecraft.getMinecraft().thePlayer == null)
            return false;
        ItemStack tempStack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
        if (itemStack != null && tempStack != null)
            return tempStack.getItem().equals(itemStack.getItem());
        return false;
    }

    public static int getSlotFromIndex(int index) {
        switch (index) {
            case 0:
                return 3;
            case 1:
                return 2;
            case 2:
                return 1;
            case 3:
                return 0;
            default:
                return -1;
        }
    }

    public static void destroyBlock(World world, int x, int y, int z, Block block, int meta) {
        world.setBlockToAir(x, y, z);
        Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(x, y, z, block, meta);
    }

    public static void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        destroyBlock(world, x, y, z, block, meta);
    }

    public static String getLocFromXYZ(int x, int y, int z) {
        return x + ":" + y + ":" + z;
    }

    public static String getLocFromArray(int[] coords) {
        return coords[0] + ":" + coords[1] + ":" + coords[2];
    }

    public static int[] getArrayFromString(String loc) {
        if (!loc.matches("-?\\d*:-?\\d*:-?\\d*"))
            return null;
        int[] coord = new int[3];
        coord[0] = Integer.parseInt(loc.substring(0, loc.indexOf(":")));
        coord[1] = Integer.parseInt(loc.substring(loc.indexOf(":") + 1, loc.indexOf(":", loc.indexOf(":") + 1)));
        coord[2] = Integer.parseInt(loc.substring(loc.lastIndexOf(":") + 1));
        return coord;
    }

    public static void writeQueueToNBT(LinkedBlockingQueue<CoordSetF> targetSetQueue, NBTTagCompound tag) {
        tag.setInteger("queueSize", targetSetQueue.size());

        int index = 0;
        for (CoordSetF coordSet : targetSetQueue) {
            tag.setDouble("coordSetX" + index, coordSet.getX());
            tag.setDouble("coordSetY" + index, coordSet.getY());
            tag.setDouble("coordSetZ" + index, coordSet.getZ());
            index++;
        }
    }

    public static LinkedBlockingQueue<CoordSetF> readQueueFromNBT(NBTTagCompound tag) {
        int length = tag.getInteger("queueSize");

        LinkedBlockingQueue<CoordSetF> coordSetQueue = new LinkedBlockingQueue<CoordSetF>(length);

        for (int i = 0; i < length; i++) {
            double x = tag.getDouble("coordSetX" + i);
            double y = tag.getDouble("coordSetY" + i);
            double z = tag.getDouble("coordSetZ" + i);

            coordSetQueue.add(new CoordSetF(x, y, z));
        }

        return coordSetQueue;
    }

    public static void writeQueueToBuffer(LinkedBlockingQueue<CoordSetF> targetSetQueue, ByteBuf buffer) {
        buffer.writeInt(targetSetQueue.size());

        for (CoordSetF coordSet : targetSetQueue) {
            buffer.writeDouble(coordSet.getX());
            buffer.writeDouble(coordSet.getY());
            buffer.writeDouble(coordSet.getZ());
        }
    }

    public static LinkedBlockingQueue<CoordSetF> readQueueFromBuffer(ByteBuf buffer) {
        int length = buffer.readInt();

        LinkedBlockingQueue<CoordSetF> coordSetQueue = new LinkedBlockingQueue<CoordSetF>(length);

        for (int i = 0; i < length; i++) {
            double x = buffer.readDouble();
            double y = buffer.readDouble();
            double z = buffer.readDouble();

            coordSetQueue.add(new CoordSetF(x, y, z));
        }

        return coordSetQueue;
    }

    public static boolean isEntityWithin(EntityLivingBase entity, CoordSet coordSet, double value) {
        return entity.getDistanceSq(coordSet.getX(), coordSet.getY(), coordSet.getZ()) < (value * value);
    }
}