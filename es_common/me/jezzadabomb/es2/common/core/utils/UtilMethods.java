package me.jezzadabomb.es2.common.core.utils;

import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.input.Keyboard;

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