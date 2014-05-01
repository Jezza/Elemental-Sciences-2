package me.jezzadabomb.es2.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class FXUtils {

    public static void renderShreddedItem(double x, double y, double z, double tx, double ty, double tz, ItemStack itemStack, float speed) {
        // TODO Well, this. Thanks future me!
        Minecraft.getMinecraft().thePlayer.renderBrokenItemStack(itemStack);
    }

    public static void renderShatteredItem() {
        
    }

}
