package me.jezzadabomb.es2.common.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.ByteBufUtils;

public class PacketUtils {

    public static void writeStringByteBuffer(ByteBuf bytes, String string) {
        ByteBufUtils.writeUTF8String(bytes, string);
    }

    public static String readStringByteBuffer(ByteBuf bytes) {
        return ByteBufUtils.readUTF8String(bytes);
    }

    public static void writeItemStack(ByteBuf bytes, ItemStack itemStack) {
        ByteBufUtils.writeItemStack(bytes, itemStack);
    }

    public static ItemStack readItemStack(ByteBuf bytes) {
        return ByteBufUtils.readItemStack(bytes);
    }
}
