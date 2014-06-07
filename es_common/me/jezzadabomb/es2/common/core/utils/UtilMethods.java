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
import net.minecraft.util.ChatComponentTranslation;
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

    public static boolean hasPressedShift() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public static boolean isRenderType(Block block, int type) {
        return block != null && block.getRenderType() == type;
    }

    public static boolean isRenderType(IBlockAccess world, CoordSet coordSet, int type) {
        Block block = coordSet.getBlock(world);
        return block != null && isRenderType(block, type);
    }

    public static boolean isEntityWithin(EntityLivingBase entity, CoordSet coordSet, double value) {
        return entity.getDistanceSq(coordSet.getX(), coordSet.getY(), coordSet.getZ()) < (value * value);
    }
}