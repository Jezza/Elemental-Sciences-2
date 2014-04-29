package me.jezzadabomb.es2.common.core.network.packet.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.packet.IPacket;
import me.jezzadabomb.es2.common.core.utils.AtomicCatalystAttribute;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class AtomicCatalystDebugPacket implements IPacket {

    CoordSet coordSet;

    public AtomicCatalystDebugPacket(AtomicCatalystAttribute attribute) {
        coordSet = new CoordSet(attribute.getFortune(), attribute.getSpeed(), attribute.getStrength());
    }

    public AtomicCatalystDebugPacket() {
    }

    @Override
    public void writeBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        coordSet.writeBytes(buffer);
    }

    @Override
    public void readBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        coordSet = CoordSet.readBytes(buffer);
    }

    @Override
    public void executeClientSide(EntityPlayer player) {
        ESLogger.severe("Packet sent to the wrong side!");
    }

    @Override
    public void executeServerSide(EntityPlayer player) {
        ItemStack itemStack = player.getCurrentEquippedItem();
        if (itemStack != null && itemStack.getItem() == ModItems.atomicCatalyst && itemStack.hasTagCompound())
            new AtomicCatalystAttribute(coordSet.getZ(), coordSet.getY(), coordSet.getX()).writeToNBT(itemStack.getTagCompound());
    }

}
