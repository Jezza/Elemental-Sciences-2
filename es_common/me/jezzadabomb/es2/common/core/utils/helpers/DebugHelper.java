package me.jezzadabomb.es2.common.core.utils.helpers;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.items.debug.ItemDebugTool;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DebugHelper {

    public static boolean correctMode(int mode) {
        if (canShowDebugHUD()) {
            ItemStack tempStack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
            return ((ItemDebugTool) tempStack.getItem()).debugMode == mode;
        }
        return false;
    }

    public static boolean canShowDebugHUD() {
        if (!Reference.isDebugMode)
            return false;
        return PlayerHelper.isHoldingItem(ModItems.debugItem);
    }

    public static boolean canFlood() {
        if (canShowDebugHUD()) {
            ItemStack tempStack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
            return ((ItemDebugTool) tempStack.getItem()).canFlood;
        }
        return false;
    }
}
