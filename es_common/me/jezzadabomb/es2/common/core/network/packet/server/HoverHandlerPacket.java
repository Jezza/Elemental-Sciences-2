package me.jezzadabomb.es2.common.core.network.packet.server;

import java.io.IOException;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.handlers.HoverHandler;
import me.jezzadabomb.es2.common.core.handlers.HoverHandler.HoveringPlayer;
import me.jezzadabomb.es2.common.core.network.PacketUtils;
import me.jezzadabomb.es2.common.core.network.packet.IPacket;

public class HoverHandlerPacket implements IPacket {

    String username;
    int time;
    boolean hovering;
    boolean waiting;

    public HoverHandlerPacket(HoveringPlayer hoveringPlayer) {
        this.username = hoveringPlayer.getUsername();
        time = hoveringPlayer.tickCount();
        hovering = hoveringPlayer.isHovering();
        waiting = hoveringPlayer.isWaiting();
    }

    public HoverHandlerPacket() {
    }

    @Override
    public void writeBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        buffer.writeInt(time);
        buffer.writeBoolean(hovering);
        buffer.writeBoolean(waiting);

        PacketUtils.writeStringByteBuffer(buffer, username);
    }

    @Override
    public void readBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        time = buffer.readInt();
        hovering = buffer.readBoolean();
        waiting = buffer.readBoolean();

        username = PacketUtils.readStringByteBuffer(buffer);
    }

    @Override
    public void executeClientSide(EntityPlayer player) {
        Entity entity = player.worldObj.getPlayerEntityByName(username);

        if (entity == null || !(entity instanceof EntityPlayer))
            return;

        HoverHandler.getInstance().updatePlayer(((EntityPlayer) entity), time, hovering, waiting, new Random().nextInt(3));

    }

    @Override
    public void executeServerSide(EntityPlayer player) {
        ESLogger.severe("Tried to send packet to wrong side!");
    }

}
