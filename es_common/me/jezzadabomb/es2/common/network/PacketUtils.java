package me.jezzadabomb.es2.common.network;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.network.packet.IPacket;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;

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
